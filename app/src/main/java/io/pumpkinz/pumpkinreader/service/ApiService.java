package io.pumpkinz.pumpkinreader.service;

import java.util.List;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;


public interface ApiService {

    @GET("/topstories.json")
    Observable<List<Integer>> listTopStories();

    @GET("/item/{item}.json")
    Observable<ItemPOJO> getItem(@Path("item") int itemId);

}
