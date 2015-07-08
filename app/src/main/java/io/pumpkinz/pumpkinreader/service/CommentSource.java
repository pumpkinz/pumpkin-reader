package io.pumpkinz.pumpkinreader.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.News;
import rx.Observable;
import rx.functions.Func1;


public class CommentSource {

    private static CommentSource instance;

    private CommentSource() {
    }

    public static CommentSource service() {
        if (instance == null) {
            instance = new CommentSource();
        }

        return instance;
    }

    public Observable<List<Comment>> getComments(final News news) {
        return Observable.from(news.getKids())
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

                        for (Integer commentId : news.getKids()) {
                            Comment comment = commentDict.get(commentId);
                            if (comment != null) {
                                retval.add(getCommentWithChild(comment, commentDict));
                            }
                        }

                        return retval;
                    }
                });
    }

    private Observable<Comment> getInnerComments(Comment comment) {
        if (comment.getKids().size() > 0) {
            return Observable.merge(
                    Observable.just(comment),
                    Observable.from(comment.getKids())
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

    private Comment getCommentWithChild(Comment comment, Dictionary<Integer, Comment> commentDict) {
        if (comment.getKids().size() == 0) {
            return comment;
        }

        for (Integer commentId : comment.getKids()) {
            Comment childComment = commentDict.get(commentId);
            if (childComment != null) {
                childComment.setParentComment(comment);
                comment.addChildComment(getCommentWithChild(childComment, commentDict));
            }
        }
        return comment;
    }

}
