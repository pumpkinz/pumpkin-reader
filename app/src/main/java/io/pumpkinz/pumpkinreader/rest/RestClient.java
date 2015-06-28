package io.pumpkinz.pumpkinreader.rest;

import retrofit.RestAdapter;

/**
 * Created by timotiusnc on 6/28/15.
 */
public class RestClient {
    private static final String HN_API_ENDPOINT = "https://hacker-news.firebaseio.com/v0";
    private static RestAdapter restAdapter;
    private static ApiService apiService;

    private static RestAdapter getAdapter() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HN_API_ENDPOINT)
                    .build();
        }

        return restAdapter;
    }

    public static ApiService service() {
        if (apiService == null) {
            apiService = getAdapter().create(ApiService.class);
        }
        return apiService;
    }
}
