package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;


public class CommentViewHolder extends RecyclerView.ViewHolder {

    private FrameLayout container;
    private TextView submitter;
    private TextView date;
    private TextView body;
    private TextView childCount;

    public CommentViewHolder(View view) {
        super(view);
        this.container = (FrameLayout) view.findViewById(R.id.comment_item_container);
        this.submitter = (TextView) view.findViewById(R.id.comment_submitter);
        this.date = (TextView) view.findViewById(R.id.comment_age);
        this.body = (TextView) view.findViewById(R.id.comment_body);
        this.childCount = (TextView) view.findViewById(R.id.child_comment_count);
    }

    public FrameLayout getContainer() {
        return container;
    }

    public TextView getSubmitter() {
        return submitter;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getBody() {
        return body;
    }

    public TextView getChildCount() {
        return childCount;
    }

}
