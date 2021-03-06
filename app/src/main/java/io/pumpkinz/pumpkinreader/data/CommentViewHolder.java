package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;


public class CommentViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout container;
    private TextView submitter;
    private TextView date;
    private TextView body;
    private TextView childCount;
    private View colorCode;

    public CommentViewHolder(View view) {
        super(view);
        this.container = (LinearLayout) view.findViewById(R.id.comment_item_container);
        this.submitter = (TextView) view.findViewById(R.id.comment_submitter);
        this.date = (TextView) view.findViewById(R.id.comment_age);
        this.body = (TextView) view.findViewById(R.id.comment_body);
        this.childCount = (TextView) view.findViewById(R.id.child_comment_count);
        this.colorCode = view.findViewById(R.id.color_code);
    }

    public LinearLayout getContainer() {
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

    public View getColorCode() {
        return colorCode;
    }

}
