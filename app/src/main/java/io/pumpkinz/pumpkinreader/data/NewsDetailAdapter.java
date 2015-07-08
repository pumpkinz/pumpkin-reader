package io.pumpkinz.pumpkinreader.data;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.koofr.android.timeago.TimeAgo;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;


public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private News dataset;
    private TimeAgo dateFormatter;

    public NewsDetailAdapter(final Fragment fragment, final News dataset) {
        this.fragment = fragment;
        this.dataset = dataset;
        this.dateFormatter = new TimeAgo(fragment.getActivity());
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
                return new NewsViewHolder(v);
            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
                return new CommentViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                News news = this.dataset;
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

                break;
            case 1:
                /*Comment comment = this.dataset.getTopLevelComments().get(i - 1);
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;

                commentViewHolder.getSubmitter().setText(comment.getBy());
                commentViewHolder.getDate().setText(this.dateFormatter.timeAgo(comment.getTime()));
                commentViewHolder.getBody().setText(comment.getText());*/

                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.getTotalComments() + 1;
    }

}
