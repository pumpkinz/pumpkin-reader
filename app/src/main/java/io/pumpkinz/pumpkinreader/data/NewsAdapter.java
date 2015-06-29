package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private List<ItemPOJO> dataset;

    public NewsAdapter(List<ItemPOJO> dataset) {
        this.dataset = dataset;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        ItemPOJO item = dataset.get(i);

        newsViewHolder.title.setText(item.getTitle());
        newsViewHolder.newsScore.setText(String.valueOf(item.getScore()));
        newsViewHolder.newsSubmitter.setText(item.getBy());
        newsViewHolder.newsCommentCount.setText(String.valueOf(item.getDescendants()));
        newsViewHolder.newsUrl.setText(item.getUrl());
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }
}
