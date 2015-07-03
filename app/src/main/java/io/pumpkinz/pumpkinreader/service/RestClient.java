package io.pumpkinz.pumpkinreader.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.model.Story;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Path;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class RestClient implements ApiService {

    private static final String HN_API_ENDPOINT = "https://hacker-news.firebaseio.com/v0";
    private static RestClient instance;
    private static RestAdapter restAdapter;
    private static ApiService apiService;

    private RestClient() {}

    public static RestClient service() {
        if (instance == null) {
            instance = new RestClient();
        }

        return instance;
    }

    private static RestAdapter getAdapter() {
        if (restAdapter == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Item.class, new ItemTypeAdapter())
                    .registerTypeAdapter(News.class, new ItemTypeAdapter())
                    .create();

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HN_API_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .build();
        }

        return restAdapter;
    }

    private static ApiService getService() {
        if (apiService == null) {
            apiService = getAdapter().create(ApiService.class);
        }
        return apiService;
    }

    @Override
    public Observable<List<Integer>> listTopStories() {
        return getService().listTopStories();
    }

    @Override
    public Observable<Item> getItem(@Path("item") int itemId) {
        return getService().getItem(itemId);
    }

    @Override
    public Observable<News> getNews(@Path("news") int newsId) {
        return getService().getNews(newsId)
                .onErrorReturn(new Func1<Throwable, News>() {
                    @Override //If the API returning error, just return null
                    public News call(Throwable throwable) {
                        return null;
                    }
                });
    }

    /**
     * Get a maximum of N number of News. Maximum because the JSON retrieved
     * might not be able to be instantiated into News object due to incomplete JSON.
     *
     * @param N maximum number of News to be retrieved
     * @return an Observable of List of News
     */
    public Observable<List<News>> getTopNews(int N) {
        //TODO: save top stories into local storage
        //TODO: add offset variable for pagination/infinite scroll feature
        return listTopStories()
            .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                @Override
                public Observable<Integer> call(List<Integer> integers) {
                    return Observable.from(integers);
                }
            })
            .take(N)
            .flatMap(new Func1<Integer, Observable<News>>() {
                @Override
                public Observable<News> call(Integer integer) {
                    return getNews(integer);
                }
            })
            .filter(new Func1<News, Boolean>() { //Filter out the NULL news from any parse error
                @Override
                public Boolean call(News news) {
                    return (news != null);
                }
            })
            .toList();
    }
}
