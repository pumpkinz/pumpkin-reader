package io.pumpkinz.pumpkinreader.service;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.Comment;
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

    @GET("/item/{news}.json")
    Observable<News> getNews(@Path("news") int newsId);

    @GET("/item/{comment}.json")
    Observable<Comment> getComment(@Path("comment") int commentId);

}
