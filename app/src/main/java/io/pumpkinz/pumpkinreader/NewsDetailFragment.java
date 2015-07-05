package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.data.NewsDetailAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.News;


public class NewsDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        News news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

        RecyclerView newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);
        newsDetail.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsDetail.setLayoutManager(layoutManager);

        newsDetail.setAdapter(new NewsDetailAdapter(this, news));

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsDetail.addItemDecoration(itemDecoration);
    }

}
