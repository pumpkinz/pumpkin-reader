package io.pumpkinz.pumpkinreader.model;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created by timotiusnc on 6/28/15.
 */
public class HnApiMgr {
    public interface HnApi {
        @GET("/topstories.json")
        void listTopStories(Callback<List<Integer>> cb);
    }

    private static final String HN_API_ENDPOINT = "https://hacker-news.firebaseio.com/v0";
    private static RestAdapter restAdapter;
    private static HnApi hnApi;

    private static RestAdapter getAdapter() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HN_API_ENDPOINT)
                    .build();
        }

        return restAdapter;
    }

    public static HnApi getApi() {
        if (hnApi == null) {
            hnApi = getAdapter().create(HnApi.class);
        }
        return hnApi;
    }
}
