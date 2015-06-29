package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import io.pumpkinz.pumpkinreader.R;


public class NewsViewHolder extends RecyclerView.ViewHolder {

    public TextView title, newsScore, newsSubmitter, newsCommentCount, newsUrl;

    public NewsViewHolder(View view) {
        super(view);
        title= (TextView) view.findViewById(R.id.news_title);
        newsScore = (TextView) view.findViewById(R.id.news_score);
        newsSubmitter = (TextView) view.findViewById(R.id.news_submitter);
        newsCommentCount = (TextView) view.findViewById(R.id.news_comment_count);
        newsUrl = (TextView) view.findViewById(R.id.news_url);
    }

}
