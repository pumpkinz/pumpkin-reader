package io.pumpkinz.pumpkinreader.data;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import io.pumpkinz.pumpkinreader.NewsListFragment;
import io.pumpkinz.pumpkinreader.R;


public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Fragment fragment;
    private List<String> dataset;
    private OnClickListener onClickListener;

    public NewsAdapter(final Fragment fragment, final List<String> dataset) {
        this.fragment = fragment;
        this.dataset = dataset;

        this.onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.news_list);
                int position = recyclerView.getChildAdapterPosition(view);

                ((NewsListFragment) fragment).goToNewsDetail(dataset.get(position));
            }
        };
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        v.setOnClickListener(this.onClickListener);
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
