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
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;


public class NewsListFragment extends Fragment {

    RecyclerView newsList;

    public NewsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.newsList = (RecyclerView) view.findViewById(R.id.news_list);
        this.newsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.newsList.setLayoutManager(layoutManager);

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
        this.newsList.setAdapter(new NewsAdapter(dataset));

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        this.newsList.addItemDecoration(itemDecoration);
    }

}
