package io.pumpkinz.pumpkinreader.data;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;


public class NewsViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView body;
    private TextView submitter;
    private TextView url;
    private TextView date;
    private TextView score;
    private TextView commentCount;

    public NewsViewHolder(View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.news_title);
        this.body = (TextView) view.findViewById(R.id.news_body);
        this.submitter = (TextView) view.findViewById(R.id.news_submitter);
        this.url = (TextView) view.findViewById(R.id.news_url);
        this.date = (TextView) view.findViewById(R.id.news_age);
        this.score = (TextView) view.findViewById(R.id.news_score);
        this.commentCount = (TextView) view.findViewById(R.id.news_comment_count);
    }


    public TextView getTitle() {
        return title;
    }

    public TextView getBody() {
        return body;
    }

    public TextView getSubmitter() {
        return submitter;
    }

    public TextView getUrl() {
        return url;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getScore() {
        return score;
    }

    public TextView getCommentCount() {
        return commentCount;
    }

}
