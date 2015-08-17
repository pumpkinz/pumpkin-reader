package io.pumpkinz.pumpkinreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

    private News news;
    private Observable<List<Comment>> comments;
    private Subscription subscription = Subscriptions.empty();
    private DataSource dataSource;
    private NewsDetailAdapter newsDetailAdapter;
    private RecyclerView newsDetail;
    private RecyclerView.ItemDecoration itemDecoration;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Log.d("Pumpkin", "NewsDetail " + getId() + " onAttach");
        dataSource = new DataSource(getActivity());
    }

    @Override
    public void onDestroyView() {
        Log.d("Pumpkin", "NewsDetail " + getId() + " destroy");
        forceUnsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Pumpkin", "NewsDetail " + getId() + " onCreate");
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

        Log.d("Pumpkin", "NewsDetail " + getId() + " onViewCreated newsDetail.setAdapter " + (savedInstanceState == null));
        newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsDetail.setLayoutManager(layoutManager);

        if (news != null) {
            newsDetailAdapter = new NewsDetailAdapter(this, news);
            newsDetail.setAdapter(newsDetailAdapter);

            Bundle newsDetailBundle = getActivity().getIntent().getParcelableExtra(MainActivity.NEWS_DETAIL_BUNDLE);

            if (newsDetailBundle != null) {
                Log.d("Pumpkin", "NewsDetail " + getId() + " newsDetailBundle not null");
                initializeNewsDetail(newsDetailBundle);
            } else if (savedInstanceState != null) {
                Log.d("Pumpkin", "NewsDetail " + getId() + " savedInstance not null");
                initializeNewsDetail(savedInstanceState);
            }

            if (newsDetailAdapter.getDataSet().isEmpty()) {
                Log.d("Pumpkin", "NewsDetail dataset empty");
                subscription = comments.subscribe(new CommentsSubscriber(true));
            } else if (newsDetailAdapter.hasLoadingMore()) {
                Log.d("Pumpkin", "NewsDetail dataset loading");
                subscription = comments.subscribe(new CommentsSubscriber(false));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("Pumpkin", "NewsDetail " + getId() + " onSaveInstanceState");
        /*if (newsDetailAdapter != null) {
            Log.d("Pumpkin", "NewsDetail " + getId() + " onSaveInstanceState not null");
            outState.putParcelable(SAVED_DATASET, Parcels.wrap(newsDetailAdapter.getDataSet()));
            outState.putParcelable(SAVED_COMMENTS, Parcels.wrap(newsDetailAdapter.getComments()));*/
        if (newsDetailAdapter != null && !newsDetailAdapter.hasLoadingMore()) {
            outState.putParcelable(MainActivity.COMMENTS_DATASET, Parcels.wrap(CommentParcel.fromComments(newsDetailAdapter.getDataSet())));
            outState.putParcelable(Constants.COMMENT, Parcels.wrap(CommentParcel.fromComments(newsDetailAdapter.getComments())));
        }
    }

    public void loadComments(News news) {
        if (news == null) {
            //Log.d("Pumpkin", "NewsDetail " + getId() + " news null");
            this.news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

            if (this.news != null) {
                comments = AppObservable.bindFragment(this, dataSource.getComments(this.news).cache());
            }
        } else {
            //Log.d("Pumpkin", "NewsDetail " + getId() + " news NOT null");
            this.news = news;

            if (newsDetailAdapter == null) { // On landscape and no news selected
                newsDetailAdapter = new NewsDetailAdapter(this, this.news);
                newsDetail.setAdapter(newsDetailAdapter);
            } else { // On landscape and there's previous news, replace with the new one
                newsDetailAdapter.removeDatasetAndComments();
                newsDetailAdapter.setNews(news);
            }

            comments = AppObservable.bindFragment(this, dataSource.getComments(this.news).cache());
            subscription = comments.subscribe(new CommentsSubscriber(true));
        }
    }

    public News getNews() {
        return news;
    }

    public RecyclerView getNewsDetail() {
        return newsDetail;
    }

    private void forceUnsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void initializeNewsDetail(Bundle newsDetailBundle) {
        /*List<Comment> savedDataset = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_DATASET));
            List<Comment> savedComments = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_COMMENTS));
            newsDetailAdapter.addComment(savedDataset, savedComments);*/
        List<CommentParcel> savedDatasetParcel = Parcels.unwrap(newsDetailBundle.getParcelable(MainActivity.COMMENTS_DATASET));
        List<CommentParcel> savedCommentsParcel = Parcels.unwrap(newsDetailBundle.getParcelable(Constants.COMMENT));

        if (savedDatasetParcel != null && savedCommentsParcel != null) {
            List<Comment> savedDataset = CommentParcel.fromCommentParcels(savedDatasetParcel, news);
            List<Comment> savedComments = CommentParcel.fromCommentParcels(savedCommentsParcel, news);
            newsDetailAdapter.addComment(savedDataset, savedComments);

            Parcelable scrollState = newsDetailBundle.getParcelable(MainActivity.COMMENTS_SCROLLSTATE);
            if (scrollState != null) {
                newsDetail.getLayoutManager().onRestoreInstanceState(scrollState);
            }

            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            newsDetail.addItemDecoration(itemDecoration);
        }
    }

    private class CommentsSubscriber extends Subscriber<List<Comment>> {

        public CommentsSubscriber(boolean isEmpty) {
            if (isEmpty) {
                if (itemDecoration != null) {
                    newsDetail.removeItemDecoration(itemDecoration);
                }
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

            if (itemDecoration == null) {
                itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            }
            newsDetail.addItemDecoration(itemDecoration);
        }

    }

}
