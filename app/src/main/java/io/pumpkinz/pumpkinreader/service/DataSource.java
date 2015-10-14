package io.pumpkinz.pumpkinreader.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.exception.EndOfListException;
import io.pumpkinz.pumpkinreader.model.Comment;
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

    public Observable<List<News>> getHNSaved(final int from, final int count) {
        return getHNSavedIds()
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getHNNew(final int from, final int count, boolean isRefresh) {
        return getHNNewIds(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getHNTop(final int from, final int count, boolean isRefresh) {
        return getHNTopIds(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getHNAsk(final int from, final int count, boolean isRefresh) {
        return getHNAskIds(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getHNShow(final int from, final int count, boolean isRefresh) {
        return getHNShowIds(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<News>> getHNJob(final int from, final int count, boolean isRefresh) {
        return getHNJobIds(isRefresh)
                .compose(new NewsTransformer(from, count));
    }

    public Observable<List<Comment>> getComments(final News news) {
        return Observable.from(news.getCommentIds())
                .flatMap(new Func1<Integer, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Integer commentId) {
                        return RestClient.service().getComment(commentId)
                                .onErrorReturn(new Func1<Throwable, Comment>() {
                                    @Override
                                    public Comment call(Throwable throwable) {
                                        return null;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<Comment, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Comment comment) {
                        return getInnerComments(comment);
                    }
                })
                .timeout(Constants.CONN_TIMEOUT_SEC, TimeUnit.SECONDS)
                .filter(new Func1<Comment, Boolean>() {
                    @Override
                    public Boolean call(Comment comment) {
                        return (comment != null) && !comment.isDeleted() && !comment.isDead();
                    }
                })
                .toList()
                .map(new Func1<List<Comment>, List<Comment>>() {
                    @Override
                    public List<Comment> call(List<Comment> comments) {
                        Dictionary<Integer, Comment> commentDict = new Hashtable<>();
                        List<Comment> retval = new ArrayList<>();

                        for (Comment comment : comments) {
                            commentDict.put(comment.getId(), comment);
                        }

                        for (Integer commentId : news.getCommentIds()) {
                            Comment comment = commentDict.get(commentId);
                            if (comment != null) {
                                retval.add(getCommentWithChild(0, comment, commentDict));
                            }
                        }

                        return flattenComments(retval);
                    }
                })
                .map(new Func1<List<Comment>, List<Comment>>() {
                    @Override
                    public List<Comment> call(List<Comment> comments) {
                        for (Comment comment : comments) {
                            comment.setAllChildCount(getAllChildCount(comment));
                        }

                        return comments;
                    }
                });
    }

    private Observable<List<Integer>> getHNSavedIds() {
        List<Integer> retval = getNewsIdsFromSp(Constants.SAVED_FILE_SP, Constants.SAVED_VAL_SP, ctx);
        return Observable.just(retval);
    }

    private Observable<List<Integer>> getHNNewIds(boolean isRefresh) {
        List<Integer> retval = getNewsIdsFromSp(Constants.NEW_FILE_SP, Constants.NEW_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().getHNNewIds()
                    .doOnNext(new putToSpAction(ctx, Constants.NEW_FILE_SP, Constants.NEW_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private Observable<List<Integer>> getHNTopIds(boolean isRefresh) {
        List<Integer> retval = getNewsIdsFromSp(Constants.TOP_FILE_SP, Constants.TOP_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().getHNTopIds()
                    .doOnNext(new putToSpAction(ctx, Constants.TOP_FILE_SP, Constants.TOP_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private Observable<List<Integer>> getHNAskIds(boolean isRefresh) {
        List<Integer> retval = getNewsIdsFromSp(Constants.ASK_FILE_SP, Constants.ASK_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().getHNAskIds()
                    .doOnNext(new putToSpAction(ctx, Constants.ASK_FILE_SP, Constants.ASK_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private Observable<List<Integer>> getHNShowIds(boolean isRefresh) {
        List<Integer> retval = getNewsIdsFromSp(Constants.SHOW_FILE_SP, Constants.SHOW_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().getHNShowIds()
                    .doOnNext(new putToSpAction(ctx, Constants.SHOW_FILE_SP, Constants.SHOW_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private Observable<List<Integer>> getHNJobIds(boolean isRefresh) {
        List<Integer> retval = getNewsIdsFromSp(Constants.JOB_FILE_SP, Constants.JOB_VAL_SP, ctx);

        if (retval.isEmpty() || isRefresh) {
            return RestClient.service().getHNJobIds()
                    .doOnNext(new putToSpAction(ctx, Constants.JOB_FILE_SP, Constants.JOB_VAL_SP));
        } else {
            return Observable.just(retval);
        }
    }

    private List<Integer> getNewsIdsFromSp(String newsFileKey, String newsValKey, Context context) {
        List<Integer> retval = new ArrayList<>();

        SharedPreferences topStoriesSp = context.getSharedPreferences(
                newsFileKey, Context.MODE_PRIVATE);
        String topStories = topStoriesSp.getString(newsValKey, "");

        if (!topStories.isEmpty()) {
            retval = Util.splitNews(topStories);
        }

        return retval;
    }

    private Observable<Comment> getInnerComments(Comment comment) {
        if (comment != null && comment.getCommentIds().size() > 0) {
            return Observable.merge(
                    Observable.just(comment),
                    Observable.from(comment.getCommentIds())
                            .flatMap(new Func1<Integer, Observable<Comment>>() {
                                @Override
                                public Observable<Comment> call(Integer commentId) {
                                    return RestClient.service().getComment(commentId)
                                            .onErrorReturn(new Func1<Throwable, Comment>() {
                                                @Override
                                                public Comment call(Throwable throwable) {
                                                    return null;
                                                }
                                            });
                                }
                            })
                            .flatMap(new Func1<Comment, Observable<Comment>>() {
                                @Override
                                public Observable<Comment> call(Comment comment) {
                                    return getInnerComments(comment);
                                }
                            })
            );
        }

        return Observable.just(comment);
    }

    private Comment getCommentWithChild(int level, Comment comment, Dictionary<Integer, Comment> commentDict) {
        if (comment.getCommentIds().size() == 0) {
            comment.setLevel(level);
            return comment;
        }

        for (Integer commentId : comment.getCommentIds()) {
            Comment childComment = commentDict.get(commentId);
            if (childComment != null) {
                childComment.setParentComment(comment);
                comment.addChildComment(getCommentWithChild(level + 1, childComment, commentDict));
            }
        }

        comment.setLevel(level);
        return comment;
    }

    private List<Comment> flattenComments(List<Comment> comments) {
        List<Comment> retval = new ArrayList<>();

        for (Comment comment : comments) {
            retval.add(comment);
            if (comment.getCommentIds().size() > 0) {
                retval.addAll(flattenComments(comment.getChildComments()));
            }
        }

        return retval;
    }

    private int getAllChildCount(Comment comment) {
        int size = comment.getChildComments().size();

        if (size > 0) {
            for (Comment childComment : comment.getChildComments()) {
                size += getAllChildCount(childComment);
            }
        }

        return size;
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
            String input = Util.joinNews(integers);
            SharedPreferences topStoriesSp = context.getSharedPreferences(
                    SP_FILE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = topStoriesSp.edit();

            editor.putString(SP_VAL_KEY, input).commit();
        }
    }

    private class NewsTransformer implements Observable.Transformer<List<Integer>, List<News>> {

        final List<Integer> subNewsIds = new ArrayList<>();
        private int from;
        private int to;

        public NewsTransformer(int from, int count) {
            this.from = from;
            this.to = from + count;
        }

        @Override
        public Observable<List<News>> call(Observable<List<Integer>> newsIds) {
            return newsIds
                    .map(new Func1<List<Integer>, List<Integer>>() {
                        @Override // Get a subset from Top News IDs and save it for later lookup
                        public List<Integer> call(List<Integer> newsIds) {
                            if (from >= newsIds.size()) {
                                throw new EndOfListException();
                            }

                            if (to > newsIds.size()) {
                                to = newsIds.size();
                            }

                            subNewsIds.addAll(newsIds.subList(from, to));
                            return subNewsIds;
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
                    .timeout(Constants.CONN_TIMEOUT_SEC, TimeUnit.SECONDS)
                    .filter(new Func1<News, Boolean>() {
                        @Override //Filter out the NULL News (from any parse error)
                        public Boolean call(News news) {
                            return (news != null) && !news.isDeleted() && !news.isDead();
                        }
                    })
                    .toList()
                    .map(new Func1<List<News>, List<News>>() {
                        @Override
                        public List<News> call(List<News> newses) {
                            List<News> retval = new ArrayList<>();
                            Dictionary<Integer, News> dict = new Hashtable<>();

                            for (News news : newses) {
                                dict.put(news.getId(), news);
                            }

                            for (Integer topStory : subNewsIds) {
                                retval.add(dict.get(topStory));
                            }

                            return retval;
                        }
                    });
        }
    }

}
