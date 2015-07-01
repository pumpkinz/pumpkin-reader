package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.service.RestClient;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


/**
 * Using RetainedFragmentActivity sample on
 * https://github.com/ReactiveX/RxAndroid/blob/0.x/sample-app/src/main/java/rx/android/samples/RetainedFragmentActivity.java
 */
public class NewsListFragment extends Fragment {

    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private List<ItemPOJO> dataset;
    private Observable<List<ItemPOJO>> stories;
    private Subscription subscription = Subscriptions.empty();

    public NewsListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: change magic number 20 to a constant
        stories = AppObservable.bindFragment(this, RestClient.service().getTopItems(20).cache());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: show loading bar
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onDestroyView() {
        subscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsList = (RecyclerView) view.findViewById(R.id.news_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        List<News> dataset = getMockData();
        newsList.setAdapter(new NewsAdapter(this, dataset));

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsList.addItemDecoration(itemDecoration);

        subscription = stories.subscribe(new Subscriber<List<ItemPOJO>>() {
            @Override
            public void onCompleted() {
                newsAdapter.notifyDataSetChanged();
                //TODO: hide loading bar
                Log.d("stories", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("stories err", e.toString());
            }

            @Override
            public void onNext(List<ItemPOJO> itemPOJOs) {
                Log.d("stories", String.valueOf(itemPOJOs.size()));
//                dataset.addAll(itemPOJOs);
            }
        });
    }

    public void goToNewsDetail(News news) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS, Parcels.wrap(news));

        startActivity(intent);
    }

    private List<News> getMockData() {
        Comment[] comments1 = new Comment[]{
                new Comment("eru", new Date(1435475296l), "Oh, I was hoping for a DSL for prototyping the rules to play these kind of games on a computer (and perhaps train a neural network as an AI opponent). I've been working out how to do these things recently."),
                new Comment("kriro", new Date(1435495296l), "I wonder if it'd be possible to recreate Magic: the Gathering cards using this. It'd be nice to have an alternative to Magic Set Editor for high-quality cards."),
                new Comment("troels", new Date(1435575296), "Very cool. I have a bunch of html templates and imagemagick scripts for this, but always thought I would turn it into a more coherent suite at some point. Glad someone did it for me!"),
                new Comment("egeozcan", new Date(1435375296), "I really like it. It's like re-inventing email templates for playing cards. OTOH, can't static site generators be a bit more flexible for such tasks? I guess I'll need to give it a try, but first I need to learn a bit ruby I suppose."),
                new Comment("markmywords", new Date(1435775296), "Thanks for sharing, this looks really nice. Does anyone know similar projects that help with developing card- and/or boardgames?"),
                new Comment("pepsin", new Date(1435275296), "This is insanely awesome!"),
                new Comment("benlaud", new Date(1435675296), "You force me to learn Ruby..."),
                new Comment("GaiusCoffee", new Date(1435575296), "Looks cool. I don't know ruby though.. :(")
        };

        List<Comment> commentList = Arrays.asList(comments1);

        News[] newses = new News[]{
                new News(
                        "Squib: A Ruby DSL for prototyping card and board games", "",
                        "selvan", "http://kaskus.co.id", new Date(1435575296000l), 91, commentList),

                new News(
                        "With 61 Seconds in a Minute, Markets Brace for Trouble", "",
                        "rsgoheen", "http://bloomberg.com/some/news?a=1", new Date(1435475296000l), 32, commentList),

                new News(
                        "The Man Who Saved Southwest Airlines with a '10-Minute' Idea", "",
                        "ghosh", "http://www.npr.org/123/23/42", new Date(1435375296000l), 33, commentList),

                new News(
                        "High-Profile Study Turns Up the Antitrust Heat on Google", "",
                        "IBM", "http://bloomberg.com/some/news?a=1", new Date(1435574296000l), 29, commentList),

                new News(
                        "R at Microsoft", "",
                        "vladiim", "http://revolutionanalytics.com?stories=1", new Date(1435565296000l), 75, commentList),

                new News(
                        "Perth engineer invents robotic bricklayer", "Test satu dua tigaaaaaa",
                        "gregcrv", "https://www.perthnow.com.au/1", new Date(1435565396000l), 51, commentList),

                new News(
                        "Alleged $7.5B fraud in online advertising", "",
                        "inthewoods", "http://moz.com/123", new Date(1435545296000l), 134, commentList)
        };

        return Arrays.asList(newses);
    }

}
