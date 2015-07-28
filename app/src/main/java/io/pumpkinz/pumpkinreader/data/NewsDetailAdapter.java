package io.pumpkinz.pumpkinreader.data;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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

    private Fragment fragment;
    private News news;
    private List<Comment> comments;
    private List<Comment> dataset;
    private TimeAgo dateFormatter;
    private OnClickListener newsOnClickListener;
    private OnClickListener onClickListener;

    public NewsDetailAdapter(final Fragment fragment, final News news) {
        this.fragment = fragment;
        this.news = news;
        this.comments = new ArrayList<>();
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

        this.onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.comment_body) {
                    view = (View) view.getParent().getParent();
                }

                RecyclerView recyclerView = (RecyclerView) fragment.getView();

                if (recyclerView == null) return;

                int position = recyclerView.getChildAdapterPosition(view);
                int idx = posToIdx(position);
                Comment comment = dataset.get(idx);

                if (position <= 0) {
                    return;
                }

                if (comment.isExpanded()) {
                    collapseComments(comment, idx);
                } else {
                    expandComments(comment, idx);
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
                newsViewHolder.getDate().setText(this.dateFormatter.timeAgo(news.getTime()));
                newsViewHolder.getScore().setText(Integer.toString(news.getScore()));

                Resources r = this.fragment.getActivity().getResources();
                int nComment = news.getTotalComments();
                String commentCountFormat = r.getQuantityString(R.plurals.comments, nComment, nComment);
                newsViewHolder.getCommentCount().setText(commentCountFormat);

                if (news.getText() != null && !news.getText().isEmpty()) {
                    newsViewHolder.getBody().setText(Util.trim(Html.fromHtml(news.getText())));
                } else {
                    newsViewHolder.getBody().setVisibility(View.GONE);
                }

                if (news.getUrl() != null && !news.getUrl().isEmpty()) {
                    newsViewHolder.getUrl().setText(Util.getDomainName(news.getUrl()));
                    newsViewHolder.getLinkButton().setOnClickListener(this.newsOnClickListener);
                } else {
                    newsViewHolder.getLinkButton().setVisibility(View.INVISIBLE);
                }

                break;
            case 1:
                Comment comment = this.dataset.get(i - 1);
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        commentViewHolder.getContainer().getLayoutParams();
                params.setMarginStart(Util.dpToPx(comment.getLevel() * 4));

                commentViewHolder.getContainer().setLayoutParams(params);
                commentViewHolder.getSubmitter().setText(comment.getBy());
                commentViewHolder.getDate().setText(this.dateFormatter.timeAgo(comment.getTime()));
                commentViewHolder.getBody().setText(Util.trim(Html.fromHtml(comment.getText())));
                commentViewHolder.getBody().setMovementMethod(LinkMovementMethod.getInstance());
                commentViewHolder.getBody().setAutoLinkMask(Linkify.WEB_URLS);

                if (comment.getCommentIds().size() > 0 && !comment.isExpanded()) {
                    commentViewHolder.getChildCount().setText("+" + comment.getAllChildCount() + " comments");
                    commentViewHolder.getChildCount().setVisibility(View.VISIBLE);
                } else {
                    commentViewHolder.getChildCount().setVisibility(View.GONE);
                }

                int level = comment.getLevel();

                if (level > 0) {
                    commentViewHolder.getColorCode().setBackgroundColor(getColorCode(level));
                    commentViewHolder.getColorCode().setVisibility(View.VISIBLE);
                } else {
                    commentViewHolder.getColorCode().setVisibility(View.GONE);
                }

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

    public int getCommentCount() {
        return dataset.size();
    }

    public void addComment(List<Comment> dataset) {
        this.comments.addAll(dataset);
        this.dataset.addAll(dataset);

        notifyDataSetChanged();
    }

    public void addComment(List<Comment> dataset, List<Comment> allComments) {
        this.comments.addAll(allComments);
        this.dataset.addAll(dataset);

        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        this.dataset.add(comment);
        notifyItemInserted(idxToPos(this.dataset.size() - 1));
    }

    public void addComment(int idx, Comment comment) {
        this.dataset.add(idx, comment);
        notifyItemInserted(idxToPos(idx));
    }

    public void removeItem(int idx) {
        this.dataset.remove(idx);
        notifyItemRemoved(idxToPos(idx));
    }

    private void collapseComments(Comment comment, int idx) {
        ListIterator<Comment> it = dataset.listIterator(idx + 1);
        int count = 0;

        while (it.hasNext()) {
            Comment com = it.next();

            if (com.getLevel() <= comment.getLevel()) {
                break;
            }

            if (com.getLevel() == comment.getLevel() + 1) {
                comments.get(comments.indexOf(com)).setHidden(true);
            }

            it.remove();
            count++;
        }

        comment.setExpanded(false);

        notifyItemRangeRemoved(idxToPos(idx) + 1, count);

        final int pos = idxToPos(idx);
        Handler handler = new Handler();

        // Also notify the parent to show child count badge
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(pos);
            }
        }, 100);
    }

    private void expandComments(Comment comment, int idx) {
        comment.setExpanded(true);
        comment.setHidden(false);

        int currLevel = comment.getLevel();
        ListIterator<Comment> it = comments.listIterator(comments.indexOf(comment) + 1);

        int currIdx = idx;

        while (it.hasNext()) {
            Comment currComment = it.next();

            if (currComment.getLevel() <= currLevel) {
                break;
            }

            if (currComment.getLevel() == currLevel + 1) {
                currComment.setHidden(false);
                addComment(++currIdx, currComment);
            } else {
                if (currComment.isHidden()) continue;
                if (!isParentsExpanded(currComment)) continue;

                addComment(++currIdx, currComment);
            }
        }

        // Notify parent to show child count badge
        notifyItemChanged(idxToPos(idx));
    }

    private int getColorCode(int level) {
        int color;

        switch (level % 5) {
            case 0:
                color = fragment.getResources().getColor(R.color.amber_400);
                break;
            case 1:
                color = fragment.getResources().getColor(R.color.blue_400);
                break;
            case 2:
                color = fragment.getResources().getColor(R.color.green_400);
                break;
            case 3:
                color = fragment.getResources().getColor(R.color.red_400);
                break;
            case 4:
                color = fragment.getResources().getColor(R.color.purple_400);
                break;
            default:
                color = fragment.getResources().getColor(R.color.purple_400);
                break;
        }

        return color;
    }

    private boolean isParentsExpanded(Comment comment) {
        Comment parent = comment.getParentComment();
        boolean expand = true;

        while (parent != null) {
            if (parent.isHidden() || !parent.isExpanded()) {
                expand = false;
            }

            parent = parent.getParentComment();
        }

        return expand;
    }

    private int idxToPos(int idx) {
        return idx + 1;
    }

    private int posToIdx(int pos) {
        return pos - 1;
    }

}
