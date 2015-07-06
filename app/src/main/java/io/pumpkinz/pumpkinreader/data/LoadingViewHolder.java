package io.pumpkinz.pumpkinreader.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import io.pumpkinz.pumpkinreader.R;


public class LoadingViewHolder extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;

    public LoadingViewHolder(View view) {
        super(view);

        this.progressBar = (ProgressBar) view.findViewById(R.id.list_item_loading);
    }

}
