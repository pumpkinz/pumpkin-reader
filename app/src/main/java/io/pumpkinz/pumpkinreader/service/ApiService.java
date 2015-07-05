package io.pumpkinz.pumpkinreader.service;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.News;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;


public interface ApiService {

    @GET("/newstories.json")
    Observable<List<Integer>> getHNNewIds();

    @GET("/topstories.json")
    Observable<List<Integer>> getHNTopIds();

    @GET("/askstories.json")
    Observable<List<Integer>> getHNAskIds();

    @GET("/showstories.json")
    Observable<List<Integer>> getHNShowIds();

    @GET("/jobstories.json")
    Observable<List<Integer>> getHNJobIds();

    @GET("/item/{item}.json")
    Observable<Item> getItem(@Path("item") int itemId);

    @GET("/item/{news}.json")
    Observable<News> getNews(@Path("news") int newsId);

}
