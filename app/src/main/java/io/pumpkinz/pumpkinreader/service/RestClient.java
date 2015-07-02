package io.pumpkinz.pumpkinreader.service;

import java.util.List;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import io.pumpkinz.pumpkinreader.model.News;
import retrofit.RestAdapter;
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
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HN_API_ENDPOINT)
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
    public Observable<ItemPOJO> getItem(@Path("item") int itemId) {
        return getService().getItem(itemId);
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
            .flatMap(new Func1<Integer, Observable<ItemPOJO>>() {
                @Override
                public Observable<ItemPOJO> call(Integer integer) {
                    return getItem(integer);
                }
            })
            .map(new Func1<ItemPOJO, News>() {
                @Override
                public News call(ItemPOJO itemPOJO) {
                    return News.valueOf(itemPOJO);
                }
            })
            .toList();
    }

}
