package io.pumpkinz.pumpkinreader.data;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.pumpkinz.pumpkinreader.R;


public class NewsViewHolder extends RecyclerView.ViewHolder {

    public TextView text;

    public NewsViewHolder(View view) {
        super(view);
        this.text = (TextView) view.findViewById(R.id.news_title);
    }

}
