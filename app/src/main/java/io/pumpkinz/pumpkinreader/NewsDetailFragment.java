package io.pumpkinz.pumpkinreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.pumpkinz.pumpkinreader.data.NewsDetailAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.DataSource;
import io.pumpkinz.pumpkinreader.util.CommentParcel;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


public class NewsDetailFragment extends Fragment {

    private static final String SAVED_DATASET = "io.pumpkinz.pumpkinreader.model.saved_dataset";
    private static final String SAVED_COMMENTS = "io.pumpkinz.pumpkinreader.model.saved_comments";

    private News news;
    private Observable<List<Comment>> comments;
    private Subscription subscription = Subscriptions.empty();
    private DataSource dataSource;
    private NewsDetailAdapter newsDetailAdapter;
    private RecyclerView newsDetail;
    private SwipeRefreshLayout refreshLayout;
    RecyclerView.ItemDecoration itemDecoration;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataSource = new DataSource(getActivity());
    }

    @Override
    public void onDestroyView() {
        forceUnsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        loadComments(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);
        newsDetail.getItemAnimator().setRemoveDuration(100);
        newsDetail.getItemAnimator().setChangeDuration(0);
        newsDetail.getItemAnimator().setMoveDuration(100);
        newsDetail.getItemAnimator().setAddDuration(100);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsDetail.setLayoutManager(layoutManager);

        if (news != null) {
            newsDetailAdapter = new NewsDetailAdapter(this, news);
            newsDetail.setAdapter(newsDetailAdapter);
        }

        itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        if (news != null && savedInstanceState != null) {
            List<CommentParcel> savedDatasetParcel = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_DATASET));
            List<CommentParcel> savedCommentsParcel = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_COMMENTS));

            if (savedDatasetParcel != null && savedCommentsParcel != null) {
                List<Comment> savedDataset = CommentParcel.fromCommentParcels(savedDatasetParcel, news);
                List<Comment> savedComments = CommentParcel.fromCommentParcels(savedCommentsParcel, news);
                newsDetailAdapter.addComment(savedDataset, savedComments);

                RecyclerView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
                newsDetail.addItemDecoration(itemDecoration);
            }
        }

        if (news != null && newsDetailAdapter.getDataSet().isEmpty()) {
            subscription = comments.subscribe(new CommentsSubscriber(true));
        } else if (news != null && newsDetailAdapter.hasLoadingMore()) {
            subscription = comments.subscribe(new CommentsSubscriber(false));
        }

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_detail_refresh_container);
        refreshLayout.setColorSchemeResources(R.color.pumpkin_accent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsDetail.removeItemDecoration(itemDecoration);

                loadComments(news);
                newsDetailAdapter.notifyDataSetChanged();

                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (newsDetailAdapter != null && !newsDetailAdapter.hasLoadingMore()) {
            outState.putParcelable(SAVED_DATASET, Parcels.wrap(CommentParcel.fromComments(newsDetailAdapter.getDataSet())));
            outState.putParcelable(SAVED_COMMENTS, Parcels.wrap(CommentParcel.fromComments(newsDetailAdapter.getComments())));
        }
    }

    public void loadComments(News news) {
        if (news == null) {
            this.news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

            if (this.news != null) {
                comments = AppObservable.bindFragment(this, dataSource.getComments(this.news).cache());
            }
        } else {
            this.news = news;

            //Use the new news, so replace the newsDetailAdapter
            newsDetailAdapter = new NewsDetailAdapter(this, this.news);
            newsDetail.setAdapter(newsDetailAdapter);

            comments = AppObservable.bindFragment(this, dataSource.getComments(this.news).cache());
            subscription = comments.subscribe(new CommentsSubscriber(true));
        }
    }

    private void forceUnsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private class CommentsSubscriber extends Subscriber<List<Comment>> {

        public CommentsSubscriber(boolean isEmpty) {
            if (isEmpty) {
                newsDetailAdapter.addComment((Comment) null);
            }
        }

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

            newsDetailAdapter.removeLoadingItem();

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

            newsDetail.addItemDecoration(itemDecoration);
        }

    }

}
