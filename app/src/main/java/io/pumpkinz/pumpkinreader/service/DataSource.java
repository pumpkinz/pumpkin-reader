package io.pumpkinz.pumpkinreader.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class DataSource {

    private Context ctx;

    public DataSource(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Get Top News from. Maximum because the JSON retrieved
     * might not be able to be instantiated into News object due to incomplete JSON.
     *
     * @param from      first index to be returned
     * @param count     maximum number of News to be retrieved
     * @param isRefresh whether we should request Top News from API or look up the SharedPreferences first
     * @return an Observable of List of News
     */
    public Observable<List<News>> getTop(final int from, final int count, boolean isRefresh) {
        return listTop(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getNew(final int from, final int count, boolean isRefresh) {
        return listNew(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    private Observable<List<Integer>> listTop(boolean isRefresh) {
        List<Integer> retval = getNewsFromSp(Constants.TOP_FILE_SP, Constants.TOP_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().listTop()
                    .doOnNext(new putToSpAction(ctx, Constants.TOP_FILE_SP, Constants.TOP_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private Observable<List<Integer>> listNew(boolean isRefresh) {
        List<Integer> retval = getNewsFromSp(Constants.NEW_FILE_SP, Constants.NEW_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().listNew()
                    .doOnNext(new putToSpAction(ctx, Constants.NEW_FILE_SP, Constants.NEW_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    //TODO: add listAsk, listJob, and listShow

    private List<Integer> getNewsFromSp(String newsFileKey, String newsValKey, Context context) {
        List<Integer> retval = new ArrayList<>();

        SharedPreferences topStoriesSp = context.getSharedPreferences(
                newsFileKey, Context.MODE_PRIVATE);
        String topStories = topStoriesSp.getString(newsValKey, "");

        if (!topStories.isEmpty()) {
            retval = Util.split(topStories, "|");
        }

        return retval;
    }

    private class putToSpAction implements Action1<List<Integer>> {

        private Context context;
        private String SP_FILE_KEY;
        private String SP_VAL_KEY;

        public putToSpAction(Context context, String SP_FILE_KEY, String SP_VAL_KEY) {
            this.context = context;
            this.SP_FILE_KEY = SP_FILE_KEY;
            this.SP_VAL_KEY = SP_VAL_KEY;
        }

        @Override
        public void call(List<Integer> integers) {
            String input = Util.join(integers, '|');
            SharedPreferences topStoriesSp = context.getSharedPreferences(
                    SP_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = topStoriesSp.edit();

            editor.putString(SP_VAL_KEY, input);
            editor.commit();
        }

    }

    private class NewsTransformer implements Observable.Transformer<List<Integer>, List<News>> {

        final List<Integer> topNewsIds = new ArrayList<>();
        final Dictionary<Integer, News> dict = new Hashtable<>();
        private int from;
        private int count;

        public NewsTransformer(int from, int count) {
            this.from = from;
            this.count = count;
        }

        @Override
        public Observable<List<News>> call(Observable<List<Integer>> integers) {
            return integers
                    .map(new Func1<List<Integer>, List<Integer>>() {
                        @Override // Get a subset from Top News IDs and save it for later lookup
                        public List<Integer> call(List<Integer> integers) {
                            topNewsIds.addAll(integers.subList(from, from + count));
                            return topNewsIds;
                        }
                    })
                    .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                        @Override // Emit Top News IDs one at a time
                        public Observable<Integer> call(List<Integer> integers) {
                            return Observable.from(integers);
                        }
                    })
                    .flatMap(new Func1<Integer, Observable<News>>() {
                        @Override // Get News body
                        public Observable<News> call(Integer integer) {
                            return RestClient.service().getNews(integer)
                                    .onErrorReturn(new Func1<Throwable, News>() {
                                        @Override //If API returns error, return null News
                                        public News call(Throwable throwable) {
                                            return null;
                                        }
                                    });
                        }
                    })
                    .filter(new Func1<News, Boolean>() {
                        @Override //Filter out the NULL News (from any parse error)
                        public Boolean call(News news) {
                            return (news != null);
                        }
                    })
                    .doOnNext(new Action1<News>() {
                        @Override // Put the News into dictionary for faster lookup
                        public void call(News news) {
                            dict.put(news.getId(), news);
                        }
                    })
                    .toList()
                    .map(new Func1<List<News>, List<News>>() {
                        @Override
                        // Discard the formed List<News> (what?!) and use the ones on dictionary instead
                        public List<News> call(List<News> newses) {
                            List<News> retval = new ArrayList<>();
                            for (Integer topStory : topNewsIds) {
                                retval.add(dict.get(topStory));
                            }
                            return retval;
                        }
                    });
        }
    }
    
}
