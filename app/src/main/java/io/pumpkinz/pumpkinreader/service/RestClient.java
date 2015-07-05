package io.pumpkinz.pumpkinreader.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.News;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;


public class RestClient implements ApiService {

    private static final String HN_API_ENDPOINT = "https://hacker-news.firebaseio.com/v0";
    private static RestClient instance;
    private static RestAdapter restAdapter;
    private static ApiService apiService;

    private RestClient() {
    }

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
    public Observable<List<Integer>> getHNNewIds() {
        return getService().getHNNewIds();
    }

    @Override
    public Observable<List<Integer>> getHNTopIds() {
        return getService().getHNTopIds();
    }

    @Override
    public Observable<List<Integer>> getHNAskIds() {
        return getService().getHNAskIds();
    }

    @Override
    public Observable<List<Integer>> getHNShowIds() {
        return getService().getHNShowIds();
    }

    @Override
    public Observable<List<Integer>> getHNJobIds() {
        return getService().getHNJobIds();
    }

    @Override
    public Observable<Item> getItem(int itemId) {
        return getService().getItem(itemId);
    }

    @Override
    public Observable<News> getNews(int newsId) {
        return getService().getNews(newsId);
    }

}
