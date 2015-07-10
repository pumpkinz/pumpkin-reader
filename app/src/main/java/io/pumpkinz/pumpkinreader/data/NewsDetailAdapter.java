package io.pumpkinz.pumpkinreader.data;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import net.koofr.android.timeago.TimeAgo;

import java.util.ArrayList;
import java.util.List;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;


public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private News news;
    private List<Comment> dataset;
    private TimeAgo dateFormatter;
    private OnClickListener newsOnClickListener;

    public NewsDetailAdapter(final Fragment fragment, final News news) {
        this.fragment = fragment;
        this.news = news;
        this.dataset = new ArrayList<>();
        this.dateFormatter = new TimeAgo(fragment.getActivity());

        this.newsOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (news.getUrl() != null && !news.getUrl().isEmpty()) {
                    Uri uri = Uri.parse(news.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    fragment.startActivity(intent);
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        switch (viewType) {
            case 0:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_detail, viewGroup, false);
                v.setOnClickListener(this.newsOnClickListener);
                return new NewsViewHolder(v);
            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
                return new CommentViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;

                newsViewHolder.getTitle().setText(news.getTitle());
                newsViewHolder.getSubmitter().setText(news.getBy());
                newsViewHolder.getUrl().setText(Util.getDomainName(news.getUrl()));
                newsViewHolder.getDate().setText(this.dateFormatter.timeAgo(news.getTime()));
                newsViewHolder.getScore().setText(Integer.toString(news.getScore()));

                Resources r = this.fragment.getActivity().getResources();
                int nComment = news.getTotalComments();
                String commentCountFormat = r.getQuantityString(R.plurals.comments, nComment, nComment);
                newsViewHolder.getCommentCount().setText(commentCountFormat);

                if (news.getText() != null && !news.getText().isEmpty()) {
                    newsViewHolder.getBody().setText(Html.fromHtml(news.getText()));
                } else {
                    newsViewHolder.getBody().setVisibility(View.GONE);
                }

                newsViewHolder.getNewsItemContainer().setBackground(null);

                break;
            case 1:
                Comment comment = this.dataset.get(i - 1);
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;

                commentViewHolder.getSubmitter().setText(comment.getBy());
                commentViewHolder.getDate().setText(this.dateFormatter.timeAgo(comment.getTime()));
                commentViewHolder.getBody().setText(Html.fromHtml(comment.getText()));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }

    public void addDataset(List<Comment> dataset) {
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

}
