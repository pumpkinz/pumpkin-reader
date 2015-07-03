package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    private RecyclerView newsDetail;

    public NewsDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        News news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));

        setUpFAB(view, news);

        this.newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);
        this.newsDetail.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.newsDetail.setLayoutManager(layoutManager);

        this.newsDetail.setAdapter(new NewsDetailAdapter(this, news));

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        this.newsDetail.addItemDecoration(itemDecoration);
    }

    public void goToWebView(String url) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(Constants.LINK, url);

        startActivity(intent);
    }

    private void setUpFAB(View view, final News news) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.news_detail_fab);

        if (news.getUrl() != null && !news.getUrl().isEmpty()) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToWebView(news.getUrl());
                }
            });
        } else {
            fab.setEnabled(false);
        }
    }

}
