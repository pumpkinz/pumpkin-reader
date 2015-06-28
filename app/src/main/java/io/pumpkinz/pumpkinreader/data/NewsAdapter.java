package io.pumpkinz.pumpkinreader.data;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.pumpkinz.pumpkinreader.R;


public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    List<String> dataset;

    public NewsAdapter(List<String> dataset) {
        this.dataset = dataset;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        newsViewHolder.text.setText(this.dataset.get(i));
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

}
