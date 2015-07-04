package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.model.Story;
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
    private Observable<List<News>> stories;
    private Subscription subscription = Subscriptions.empty();
    private LinearLayout progressBar;

    public NewsListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        this.progressBar = (LinearLayout) view.findViewById(R.id.news_list_progress);
        this.progressBar.setVisibility(View.VISIBLE);

        return view;
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

        newsAdapter = new NewsAdapter(this);
        newsList.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        newsList.addItemDecoration(itemDecoration);

        loadNews();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.news_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNews();
            }
        });
    }

    public void goToNewsDetail(News news) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS, Parcels.wrap(news));

        startActivity(intent);
    }

    private void loadNews() {
        newsAdapter.clearDataset();
        newsAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);

        stories = AppObservable.bindFragment(this, RestClient.service().getTopNews(20).cache());

        subscription = stories.subscribe(new Subscriber<List<News>>() {
            @Override
            public void onCompleted() {
                newsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                Log.d("stories", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("stories err", e.toString());
            }

            @Override
            public void onNext(List<News> items) {
                Log.d("stories", String.valueOf(items.size()));
                Log.d("stories content", items.toString());
                newsAdapter.addDataset(items);
            }
        });
    }

    private List<News> getMockData() {
        Comment[] comments1 = new Comment[]{
                new Comment(24, false, "comment", "eru", 1435575269, "Oh, I was hoping for a DSL for prototyping the rules to play these kind of games on a computer (and perhaps train a neural network as an AI opponent). I've been working out how to do these things recently.", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "kriro", 1435575269, "I wonder if it'd be possible to recreate Magic: the Gathering cards using this. It'd be nice to have an alternative to Magic Set Editor for high-quality cards.", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "troels", 1435575269, "Very cool. I have a bunch of html templates and imagemagick scripts for this, but always thought I would turn it into a more coherent suite at some point. Glad someone did it for me!", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "egeozcan", 1435575269, "I really like it. It's like re-inventing email templates for playing cards. OTOH, can't static site generators be a bit more flexible for such tasks? I guess I'll need to give it a try, but first I need to learn a bit ruby I suppose.", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "markmywords", 1435575269, "Thanks for sharing, this looks really nice. Does anyone know similar projects that help with developing card- and/or boardgames?", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "pepsin", 1435575269, "This is insanely awesome!", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "benlaud", 1435575269, "You force me to learn Ruby...", false, 999, new ArrayList<Integer>()),
                new Comment(24, false, "comment", "GaiusCoffee", 1435575269, "Looks cool. I don't know ruby though.. :(", false, 999, new ArrayList<Integer>())
        };

        List<Comment> commentList = Arrays.asList(comments1);

        News[] newses = new News[]{
                new Story(42, false, "story", "selvan", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "https://www.andymeneely.github.io", 42,
                        "Squib: A Ruby DSL for prototyping card and board games", commentList),

                new Story(43, false, "story", "rsgoheen", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "http://bloomberg.com/some/news?a=1", 42,
                        "With 61 Seconds in a Minute, Markets Brace for Trouble", commentList),

                new Story(42, false, "story", "ghosh", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "http://www.npr.org/123/23/42", 42,
                        "The Man Who Saved Southwest Airlines with a '10-Minute' Idea", commentList),

                new Story(42, false, "story", "IBM", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "http://bloomberg.com/some/news?a=1", 42,
                        "High-Profile Study Turns Up the Antitrust Heat on Google", commentList),

                new Story(42, false, "story", "vladiim", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "http://revolutionanalytics.com?stories=1", 42,
                        "R at Microsoft", commentList),

                new Story(42, false, "story", "gregcry", 1435575296000l, "Test satu dua tigaaaaaa", false,
                        Arrays.asList(new Integer[]{1,2,3}),"https://www.perthnow.com.au/1", 42,
                        "Perth engineer invents robotic bricklayer", commentList),

                new Story(42, false, "story", "inthewoods", 1435575296000l, "", false,
                        Arrays.asList(new Integer[]{1,2,3}), "http://moz.com/123", 42,
                        "Alleged $7.5B fraud in online advertising", commentList)
        };

        return Arrays.asList(newses);
    }

}
