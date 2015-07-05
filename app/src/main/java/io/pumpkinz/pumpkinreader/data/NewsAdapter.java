package io.pumpkinz.pumpkinreader.data;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import net.koofr.android.timeago.TimeAgo;

import java.util.ArrayList;
import java.util.List;

import io.pumpkinz.pumpkinreader.NewsListFragment;
import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;


public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Fragment fragment;
    private List<News> dataset;
    private OnClickListener onClickListener;
    private TimeAgo dateFormatter;

    public NewsAdapter(final Fragment fragment) {
        this.fragment = fragment;
        this.dataset = new ArrayList<>();
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
        newsViewHolder.getSubmitter().setText(news.getBy());
        newsViewHolder.getUrl().setText(Util.getDomainName(news.getUrl()));
        newsViewHolder.getDate().setText(this.dateFormatter.timeAgo(news.getTime()));
        newsViewHolder.getScore().setText(Integer.toString(news.getScore()));

        Resources r = this.fragment.getActivity().getResources();
        int nComment = news.getComments().size();
        String commentCountFormat = r.getQuantityString(R.plurals.comments, nComment, nComment);
        newsViewHolder.getCommentCount().setText(commentCountFormat);
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public void addDataset(List<News> dataset) {
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public void clearDataset() {
        this.dataset.clear();
        notifyDataSetChanged();
    }
}
