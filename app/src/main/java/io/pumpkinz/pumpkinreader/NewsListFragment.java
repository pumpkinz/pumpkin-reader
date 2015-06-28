package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import io.pumpkinz.pumpkinreader.rest.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class NewsListFragment extends Fragment {

    RecyclerView newsList;
    List<String> dataset;
    NewsAdapter newsAdapter;

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

        dataset = Arrays.asList(
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
        newsAdapter = new NewsAdapter(dataset);
        this.newsList.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        this.newsList.addItemDecoration(itemDecoration);

        getStories();
    }

    private void getStories() {
        RestClient.service().listTopStories()
                .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<Integer> integers) {
                        Log.d("integer", integers.toString());
                        return Observable.from(integers);
                    }
                })
                .take(10)
                .flatMap(new Func1<Integer, Observable<ItemPOJO>>() {
                    @Override
                    public Observable<ItemPOJO> call(Integer integer) {
                        Log.d("integer", integer.toString());
                        return RestClient.service().getItem(integer);
                    }
                })
                .toList()
                .subscribe(new Action1<List<ItemPOJO>>() {
                    @Override
                    public void call(List<ItemPOJO> itemPOJOs) {
                        for (ItemPOJO item : itemPOJOs) {
                            Log.d("topstories", item.toString());
                        }
                    }
                });
    }
}
