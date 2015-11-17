package io.pumpkinz.pumpkinreader.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.etc.Constants;


public class NewsViewHolder extends RecyclerView.ViewHolder implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView title;
    private TextView body;
    private TextView submitter;
    private TextView url;
    private TextView date;
    private TextView score;
    private TextView commentCount;
    private LinearLayout newsItemContainer;
    private TextView commentsText;

    private SharedPreferences preferences;

    public NewsViewHolder(View view) {
        super(view);

        this.title = (TextView) view.findViewById(R.id.news_title);
        this.body = (TextView) view.findViewById(R.id.news_body);
        this.submitter = (TextView) view.findViewById(R.id.news_submitter);
        this.url = (TextView) view.findViewById(R.id.news_url);
        this.date = (TextView) view.findViewById(R.id.news_age);
        this.score = (TextView) view.findViewById(R.id.news_score);
        this.commentCount = (TextView) view.findViewById(R.id.news_comment_count);
        this.newsItemContainer = (LinearLayout) view.findViewById(R.id.news_item_container);
        this.commentsText = (TextView) view.findViewById(R.id.comments_text);

        this.preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        this.preferences.registerOnSharedPreferenceChangeListener(this);

        setTitleTextSize();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case Constants.CONFIG_SMALLER_TEXT:
                setTitleTextSize();
                break;
        }
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

    public LinearLayout getNewsItemContainer() {
        return newsItemContainer;
    }

    public TextView getCommentsText() {
        return commentsText;
    }

    private void setTitleTextSize() {
        if (this.preferences.getBoolean(Constants.CONFIG_SMALLER_TEXT, false)) {
            this.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            this.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

}
