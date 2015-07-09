package io.pumpkinz.pumpkinreader.data;

import android.content.res.Resources;
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
    private OnClickListener onClickListener;

    public NewsDetailAdapter(final Fragment fragment, final News news) {
        this.fragment = fragment;
        this.news = news;
        this.dataset = new ArrayList<>();
        this.dateFormatter = new TimeAgo(fragment.getActivity());

        this.onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.news_detail);
                int position = recyclerView.getChildAdapterPosition(view);
                Comment comment = dataset.get(position - 1);

                if (position <= 0 || comment.getChildComments().size() <= 0) {
                    return;
                }

                int start = position + 1;
                int size = comment.getChildComments().size();
                int level = comment.getLevel() + 1;

                if (comment.isExpanded()) {
                    dataset.subList(position, position + size).clear();
                    notifyItemRangeRemoved(start, size);
                    comment.setExpanded(false);
                } else {
                    List<Comment> childComments = new ArrayList<>(comment.getChildComments());

                    for (Comment child : childComments) {
                        child.setLevel(level);
                    }

                    dataset.addAll(position, childComments);
                    notifyItemRangeInserted(start, size);
                    comment.setExpanded(true);
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
                return new NewsViewHolder(v);
            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
                v.setOnClickListener(this.onClickListener);
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

                break;
            case 1:
                Comment comment = this.dataset.get(i - 1);
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        commentViewHolder.getContainer().getLayoutParams();
                params.setMarginStart(Util.dpToPx(comment.getLevel() * 8));

                commentViewHolder.getContainer().setLayoutParams(params);
                commentViewHolder.getSubmitter().setText(comment.getBy());
                commentViewHolder.getDate().setText(this.dateFormatter.timeAgo(comment.getTime()));
                commentViewHolder.getBody().setText(Util.trim(Html.fromHtml(comment.getText())));

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
