package io.pumpkinz.pumpkinreader.rest;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by timotiusnc on 6/28/15.
 */
public interface ApiService {
    @GET("/topstories.json")
    Observable<List<Integer>> listTopStories();

    @GET("/item/{item}.json")
    Observable<ItemPOJO> getItem(@Path("item") int itemId);
}
