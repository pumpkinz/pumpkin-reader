package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView newsList;
    RecyclerView.Adapter newsListAdapter;
    RecyclerView.LayoutManager newsListLayoutManager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.newsList = (RecyclerView) view.findViewById(R.id.news_list);
        this.newsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        this.newsListLayoutManager = layoutManager;
        this.newsList.setLayoutManager(this.newsListLayoutManager);

        List<String> dataset = Arrays.asList("Asd", "Ass", "Ads");
        this.newsListAdapter = new NewsAdapter(dataset);
        this.newsList.setAdapter(this.newsListAdapter);
    }

}
