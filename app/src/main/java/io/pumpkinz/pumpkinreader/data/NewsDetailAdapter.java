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
import java.util.ListIterator;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;


public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAX_COMMENT_LEVEL = 5;

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
                if (view.getId() == R.id.comment_body) {
                    view = (View) view.getParent().getParent();
                }

                RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.news_detail);
                int position = recyclerView.getChildAdapterPosition(view);
                Comment comment = dataset.get(position - 1);

                if (position <= 0 || comment.getChildComments().size() <= 0) {
                    return;
                }

                if (comment.isExpanded()) {
                    collapseComments(comment, position);
                } else {
                    expandComments(comment, position);
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }

        if (this.dataset.get(position - 1) == null) {
            return 2;
        }

        return 1;
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
            case 2:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
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

                commentViewHolder.getContainer().setOnClickListener(this.onClickListener);
                commentViewHolder.getBody().setOnClickListener(this.onClickListener);

                break;
            case 2:
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

    public void addItem(Comment comment) {
        this.dataset.add(comment);
        notifyItemInserted(this.dataset.size());
    }

    public void removeItem(int position) {
        this.dataset.remove(position - 1);
        notifyItemRemoved(position);
    }

    public void expandAllComments() {
        for (int i = 0; i < MAX_COMMENT_LEVEL; i++) {
            for (int j = 0; j < this.dataset.size(); j++) {
                if (this.dataset.get(j).getLevel() == i) {
                    int position = j + 1;
                    expandComments(this.dataset.get(j), position);
                }
            }
        }
    }

    private void collapseComments(Comment comment, int position) {
        ListIterator it = dataset.listIterator(position);
        int count = 0;

        while (it.hasNext()) {
            Comment com = (Comment) it.next();

            if (com.getLevel() <= comment.getLevel()) {
                break;
            }

            it.remove();
            count++;
        }

        notifyItemRangeRemoved(position + 1, count);
        comment.setExpanded(false);
    }

    private void expandComments(Comment comment, int position) {
        int start = position + 1;
        int size = comment.getChildComments().size();
        int level = comment.getLevel() + 1;

        List<Comment> childComments = new ArrayList<>(comment.getChildComments());

        for (Comment child : childComments) {
            child.setLevel(level);
        }

        dataset.addAll(position, childComments);
        notifyItemRangeInserted(start, size);
        comment.setExpanded(true);
    }

}
