package io.pumpkinz.pumpkinreader.data;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import net.koofr.android.timeago.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.List;

import io.pumpkinz.pumpkinreader.NewsListFragment;
import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.News;


public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Fragment fragment;
    private List<News> dataset;
    private OnClickListener onClickListener;
    private TimeAgo dateFormatter;

    public NewsAdapter(final Fragment fragment, final List<News> dataset) {
        this.fragment = fragment;
        this.dataset = dataset;
        this.dateFormatter = new TimeAgo(fragment.getActivity());

        this.onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.news_list);
                int position = recyclerView.getChildAdapterPosition(view);

                ((NewsListFragment) fragment).goToNewsDetail(dataset.get(position));
            }
        };
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        v.setOnClickListener(this.onClickListener);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        News news = this.dataset.get(i);

        newsViewHolder.getTitle().setText(news.getTitle());
        newsViewHolder.getSubmitter().setText(news.getSubmitter());
        newsViewHolder.getUrl().setText(news.getUrl());
        newsViewHolder.getDate().setText(this.dateFormatter.timeAgo(news.getDate()));
        newsViewHolder.getScore().setText(Integer.toString(news.getScore()));
        newsViewHolder.getCommentCount().setText(Integer.toString(news.getComments().size()) + " comments");
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

}
