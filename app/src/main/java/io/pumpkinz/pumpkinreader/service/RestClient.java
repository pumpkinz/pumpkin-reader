package io.pumpkinz.pumpkinreader.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.News;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Path;
import rx.Observable;
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
        return getService().getNews(newsId);
    }

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
            .toList();
    }
}
