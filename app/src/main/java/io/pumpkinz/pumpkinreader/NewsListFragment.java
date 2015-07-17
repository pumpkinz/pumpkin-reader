package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.DataSource;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


public class NewsListFragment extends Fragment {

    private static final int N_NEWS_PER_LOAD = 50;
    private static final int LOADING_THRESHOLD = 15;

    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private DataSource dataSource;
    private Subscription subscription = Subscriptions.empty();
    private LinearLayout progressBar;
    private boolean isLoading = false;
    private int newsType = R.string.top;

    public NewsListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new DataSource(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        this.progressBar = (LinearLayout) view.findViewById(R.id.news_list_progress);
        this.progressBar.setVisibility(View.VISIBLE);

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
        loadNews(0, N_NEWS_PER_LOAD, true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.news_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNews(0, N_NEWS_PER_LOAD, true);
            }
        });
    }

    public void forceUnsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void goToNewsDetail(final News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Intent intent;

        if (pref.getBoolean(Constants.CONFIG_EXTERNAL_BROWSER, false)) {
            intent = new Intent(getActivity(), NewsCommentsActivity.class);

            if (news.getUrl() != null && !news.getUrl().isEmpty()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = Uri.parse(news.getUrl());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }, 300);
            }
        } else {
            intent = new Intent(getActivity(), NewsDetailActivity.class);
        }

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
        if (refresh) {
            newsAdapter.clearDataset();
            newsAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
        }

        isLoading = true;
        Observable<List<News>> stories =
                AppObservable.bindFragment(this, loadNewsData(from, count, refresh));

        subscription = stories.subscribe(new Subscriber<List<News>>() {
            @Override
            public void onCompleted() {
                progressBar.setVisibility(View.GONE);
                isLoading = false;
                Log.d("stories", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("stories", e.toString());
            }

            @Override
            public void onNext(List<News> items) {
                Log.d("stories", String.valueOf(items.size()));

                NewsAdapter adapter = (NewsAdapter) newsList.getAdapter();
                int lastItemIdx = adapter.getItemCount() - 1;

                if (lastItemIdx >= 0 && adapter.getItem(lastItemIdx) == null) {
                    adapter.removeItem(lastItemIdx);
                }

                newsAdapter.addDataset(items);
            }
        });
    }

    private Observable<List<News>> loadNewsData(int from, int count, boolean refresh) {
        Observable<List<News>> ret;

        switch (this.newsType) {
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

}
