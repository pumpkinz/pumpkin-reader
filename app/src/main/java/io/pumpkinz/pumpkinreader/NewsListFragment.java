package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
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
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.DataSource;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


/**
 * Using RetainedFragmentActivity sample on
 * https://github.com/ReactiveX/RxAndroid/blob/60741117c936b198ebc89dcc058ccaaa2b09ebfb/sample-app/src/main/java/rx/android/samples/RetainedFragmentActivity.java
 */
public class NewsListFragment extends Fragment {

    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private DataSource dataSource;
    private Observable<List<News>> stories;
    private Subscription subscription = Subscriptions.empty();
    private LinearLayout progressBar;

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
        subscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsList = (RecyclerView) view.findViewById(R.id.news_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(this);
        newsList.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsList.addItemDecoration(itemDecoration);

        loadNews();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.news_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNews();
            }
        });
    }

    public void goToNewsDetail(News news) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS, Parcels.wrap(news));

        startActivity(intent);
    }

    private void loadNews() {
        newsAdapter.clearDataset();
        newsAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);

        stories = AppObservable.bindFragment(this, dataSource.getHNTop(0, 20, false).cache());

        subscription = stories.subscribe(new Subscriber<List<News>>() {
            @Override
            public void onCompleted() {
                newsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                Log.d("stories", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("stories err", e.toString());
            }

            @Override
            public void onNext(List<News> items) {
                Log.d("stories", String.valueOf(items.size()));
                newsAdapter.addDataset(items);

                dataSource.getComments(items.get(0))
                        .subscribe(new Subscriber<List<Comment>>() {
                            @Override
                            public void onCompleted() {
                                Log.d("comment list", "complete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("comment list error", e.toString());
                            }

                            @Override
                            public void onNext(List<Comment> comments) {
                                Log.d("comment next", String.valueOf(comments.size()));
                            }
                        });
            }
        });
    }

}
