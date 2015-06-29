package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsDetailAdapter;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;


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

        this.newsDetail = (RecyclerView) view.findViewById(R.id.news_detail);
        this.newsDetail.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.newsDetail.setLayoutManager(layoutManager);

        List<String> dataset = Arrays.asList(
                "The Three Great Virtues of a Programmer: Laziness, Impatience, and Hubris",
                "Philanthropy for Hackers",
                "Ello mocks Facebook by being creepy",
                "Show HN: Hyperlax.tv – a real-time feed of Instagram's latest Hyperlapse videos",
                "As We May Think (1945)",
                "Running Lisp in Production",
                "How side projects saved our startup",
                "Ask HN: How big does an open-source project need to be for a lifestyle business?",
                "Fighting spam with Haskell"
        );
        this.newsDetail.setAdapter(new NewsDetailAdapter(this, dataset));

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        this.newsDetail.addItemDecoration(itemDecoration);
    }

}
