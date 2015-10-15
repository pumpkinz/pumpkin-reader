package io.pumpkinz.pumpkinreader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.TimeoutException;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.exception.EndOfListException;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.DataSource;
import io.pumpkinz.pumpkinreader.util.PreferencesUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


public class NewsListFragment extends Fragment {

    private static final int N_NEWS_PER_LOAD = 50;
    private static final int LOADING_THRESHOLD = 15;
    private static final String SAVED_NEWS = "io.pumpkinz.pumpkinreader.model.saved_news";

    private RecyclerView newsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private DataSource dataSource;
    private Observable<List<News>> stories;
    private Subscription subscription = Subscriptions.empty();
    private boolean isLoading = false;
    private int newsType = R.string.top;

    //To check if the news is unsaved after user is back from newsDetailFragment
    //If it's unsaved, remove it from newsAdapter
    private News openedNews;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataSource = new DataSource(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        stories = AppObservable.bindFragment(this, loadNewsData(0, N_NEWS_PER_LOAD, true));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (newsType != R.string.saved) {
            return;
        }

        if (openedNews != null && !PreferencesUtil.isNewsSaved(getActivity(), openedNews)) {
            newsAdapter.removeNews(openedNews);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_list_refresh_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.pumpkin_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!swipeRefreshLayout.canChildScrollUp()) {
                    forceRefresh();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        forceUnsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNewsList(view);

        if (savedInstanceState != null) {
            List<News> savedNews = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_NEWS));
            if (savedNews.size() > 0) {
                newsAdapter.addDataset(savedNews);
            }
        }

        if (newsAdapter.getDataSet().isEmpty()) { //Config changes in the middle of refreshing News
            subscription = stories.subscribe(new StoriesSubscriber(true));
        } else if (newsAdapter.hasLoadingMore()) { //Config changes in the middle of loading more News
            subscription = stories.subscribe(new StoriesSubscriber(false));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_NEWS, Parcels.wrap(newsAdapter.getDataSet()));
    }

    public void forceUnsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void goToNewsDetail(final News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Intent intent;

        boolean shouldOpenLink = pref.getBoolean(Constants.CONFIG_SHOW_LINK, true);

        if (pref.getBoolean(Constants.CONFIG_EXTERNAL_BROWSER, false)) {
            intent = new Intent(getActivity(), NewsCommentsActivity.class);
            boolean isURLValid = news.getUrl() != null && !news.getUrl().isEmpty();

            if (shouldOpenLink && isURLValid) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = Uri.parse(news.getUrl());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }, 300);
            }
        } else {
            if (shouldOpenLink) {
                intent = new Intent(getActivity(), NewsDetailActivity.class);
            } else {
                intent = new Intent(getActivity(), NewsCommentsActivity.class);
            }
        }

        openedNews = news;
        intent.putExtra(Constants.NEWS, Parcels.wrap(news));
        startActivity(intent);
    }

    public void setNewsType(int newsTypeId) {
        this.newsType = newsTypeId;
    }

    public void forceRefresh() {
        loadNews(0, N_NEWS_PER_LOAD, true);
    }

    private void loadNews(int from, int count, boolean refresh) {
        stories = AppObservable.bindFragment(this, loadNewsData(from, count, refresh));
        subscription = stories.subscribe(new StoriesSubscriber(refresh));
    }

    private Observable<List<News>> loadNewsData(int from, int count, boolean refresh) {
        Observable<List<News>> ret;

        switch (this.newsType) {
            case R.string.saved:
                ret = dataSource.getHNSaved(from, count);
                break;
            case R.string.top:
                ret = dataSource.getHNTop(from, count, refresh);
                break;
            case R.string.recent:
                ret = dataSource.getHNNew(from, count, refresh);
                break;
            case R.string.ask_hn:
                ret = dataSource.getHNAsk(from, count, refresh);
                break;
            case R.string.show_hn:
                ret = dataSource.getHNShow(from, count, refresh);
                break;
            case R.string.job:
                ret = dataSource.getHNJob(from, count, refresh);
                break;
            default:
                ret = dataSource.getHNTop(from, count, refresh);
        }

        return ret.cache();
    }

    private void setUpNewsList(View view) {
        newsList = (RecyclerView) view.findViewById(R.id.news_list);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(this);
        newsList.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsList.addItemDecoration(itemDecoration);

        newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (newsAdapter.getItemCount() < N_NEWS_PER_LOAD) {
                    //Impossible to load more, just return
                    return;
                }

                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = lm.getItemCount();
                int firstVisibleItem = lm.findFirstVisibleItemPosition();

                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + LOADING_THRESHOLD)) {
                    ((NewsAdapter) recyclerView.getAdapter()).addItem(null);
                    loadNews(recyclerView.getAdapter().getItemCount(), N_NEWS_PER_LOAD, false);
                    Log.v("stories", "Load next " + N_NEWS_PER_LOAD + " news");
                }
            }
        });
    }

    private void setRefreshingState(final boolean isRefreshing) {
        swipeRefreshLayout.post(new Runnable() { // Use post to make sure it takes effect
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    private class StoriesSubscriber extends Subscriber<List<News>> {

        public StoriesSubscriber(boolean isRefresh) {
            if (isRefresh) {
                setRefreshingState(true);
                newsAdapter.clearDataset();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            isLoading = true;
        }

        @Override
        public void onCompleted() {
            Log.d("stories", "completed");
            isLoading = false;
            setRefreshingState(false);
        }

        @Override
        public void onError(Throwable e) {
            Log.d("stories", e.toString());
            forceUnsubscribe();
            setRefreshingState(false);
            newsAdapter.removeLoadingMoreItem();

            Toast toast = Toast.makeText(getActivity(), R.string.unknown, Toast.LENGTH_LONG);
            if (e.getClass() == TimeoutException.class) {
                toast = Toast.makeText(getActivity(), R.string.timeout, Toast.LENGTH_LONG);
            } else if (e.getClass() == EndOfListException.class) {
                toast = Toast.makeText(getActivity(), R.string.endoflist, Toast.LENGTH_LONG);
            }

            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        @Override
        public void onNext(List<News> items) {
            Log.d("stories", String.valueOf(items.size()));
            newsAdapter.removeLoadingMoreItem();
            newsAdapter.addDataset(items);
        }

    }

}
