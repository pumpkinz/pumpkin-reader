package io.pumpkinz.pumpkinreader.data;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.pumpkinz.pumpkinreader.R;


public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private List<String> dataset;

    public NewsDetailAdapter(final Fragment fragment, final List<String> dataset) {
        this.fragment = fragment;
        this.dataset = dataset;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
//                newsViewHolder.text.setText(this.dataset.get(i));
                break;
            case 1:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
