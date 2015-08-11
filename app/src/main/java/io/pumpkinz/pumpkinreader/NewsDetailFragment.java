package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.pumpkinz.pumpkinreader.data.NewsDetailAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.DataSource;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


public class NewsDetailFragment extends Fragment {

    private static final String SAVED_NEWS = "io.pumpkinz.pumpkinreader.model.saved_news";
    private static final String SAVED_DATASET = "io.pumpkinz.pumpkinreader.model.saved_dataset";
    private static final String SAVED_COMMENTS = "io.pumpkinz.pumpkinreader.model.saved_comments";

    private Subscription subscription = Subscriptions.empty();
    private DataSource dataSource;
    private NewsDetailAdapter newsDetailAdapter;
    private RecyclerView newsDetail;

    public NewsDetailFragment() {
        this.dataSource = new DataSource(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        Log.d("Pumpkin", "NewsDetailFragment: destroyed");
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Pumpkin", "NewsDetailFragment: onViewCreated");

        newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsDetail.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            Log.d("Pumpkin", "NewsDetailFragment: saved instance NOT null");
            News news = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_NEWS));
            List<Comment> savedDataset = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_DATASET));
            List<Comment> savedComments = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_COMMENTS));

            newsDetailAdapter = new NewsDetailAdapter(this, news);
            newsDetail.setAdapter(newsDetailAdapter);
            newsDetailAdapter.addComment(savedDataset, savedComments);
        } else {
            Log.d("Pumpkin", "NewsDetailFragment: saved instance null");
            News news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

            if (news != null) {
                newsDetailAdapter = new NewsDetailAdapter(this, news);
                newsDetail.setAdapter(newsDetailAdapter);
                loadComments(news);
            }
        }

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsDetail.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (newsDetailAdapter != null) {
            outState.putParcelable(SAVED_NEWS, Parcels.wrap(newsDetailAdapter.getNews()));
            outState.putParcelable(SAVED_DATASET, Parcels.wrap(newsDetailAdapter.getDataSet()));
            outState.putParcelable(SAVED_COMMENTS, Parcels.wrap(newsDetailAdapter.getComments()));
        }
    }

    public void updateNewsDetail(News news) {
        Log.d("Pumpkin", "NewsDetailFragment: updateNewsDetail");

        if (newsDetailAdapter == null) {
            Log.d("Pumpkin", "NewsDetailFragment: newsDetailAdapter null");
            newsDetailAdapter = new NewsDetailAdapter(this, news);
            newsDetail.setAdapter(newsDetailAdapter);
        } else {
            Log.d("Pumpkin", "NewsDetailFragment: newsDetailAdapter NOT null");
            newsDetailAdapter.setNews(news);
        }

        loadComments(news);
    }

    private void loadComments(News news) {
        Log.d("Pupmkin", "NewsDetailFragment: loadComments");
        newsDetailAdapter.addComment((Comment) null);

        subscription = AppObservable.bindFragment(this, dataSource.getComments(news))
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("comments", "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("comments", e.toString());

                        if (e.getClass() == TimeoutException.class) {
                            if (newsDetailAdapter.getItemCount() > 1) {
                                newsDetailAdapter.removeItem(newsDetailAdapter.getCommentCount() - 1);
                            }

                            Toast toast = Toast.makeText(getActivity(), R.string.timeout, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        Log.d("comments", String.valueOf(comments.size()));

                        newsDetailAdapter.removeItem(newsDetailAdapter.getCommentCount() - 1);

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                        if (pref.getBoolean(Constants.CONFIG_AUTO_EXPAND_COMMENTS, false)) {
                            newsDetailAdapter.addComment(comments);
                        } else {
                            List<Comment> dataset = new ArrayList<>();

                            for (Comment c : comments) {
                                if (c.getLevel() == 0) {
                                    c.setExpanded(false);
                                    dataset.add(c);
                                }
                            }

                            newsDetailAdapter.addComment(dataset, comments);
                        }
                    }
                });
    }

}
