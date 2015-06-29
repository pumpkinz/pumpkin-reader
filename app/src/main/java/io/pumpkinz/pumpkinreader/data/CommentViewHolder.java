package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;


public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView commentSubmitter;
    public TextView commentAge;
    public TextView commentBody;

    public CommentViewHolder(View view) {
        super(view);
        this.commentSubmitter = (TextView) view.findViewById(R.id.comment_submitter);
        this.commentAge = (TextView) view.findViewById(R.id.comment_age);
        this.commentBody = (TextView) view.findViewById(R.id.comment_body);
    }

}
