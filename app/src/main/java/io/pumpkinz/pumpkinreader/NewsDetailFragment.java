package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.parceler.Parcels;

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

    private Subscription subscription = Subscriptions.empty();
    private DataSource dataSource;
    private NewsDetailAdapter newsDetailAdapter;
    private RecyclerView newsDetail;

    public NewsDetailFragment() {
        setRetainInstance(true);
        this.dataSource = new DataSource(getActivity());
    }

    @Override
    public void onDestroyView() {
        subscription.unsubscribe();
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

        News news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

        newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsDetail.setLayoutManager(layoutManager);

        newsDetailAdapter = new NewsDetailAdapter(this, news);
        newsDetail.setAdapter(newsDetailAdapter);

        loadComments(news);
    }

    private void loadComments(News news) {
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.timeout), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        Log.d("comments", String.valueOf(comments.size()));

                        newsDetailAdapter.removeItem(newsDetailAdapter.getCommentCount() - 1);
                        newsDetailAdapter.addComment(comments);

                        RecyclerView.ItemDecoration itemDecoration =
                                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
                        newsDetail.addItemDecoration(itemDecoration);
                    }
                });
    }

}
