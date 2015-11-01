package io.pumpkinz.pumpkinreader.util;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.News;


@Parcel
public class CommentParcel extends Item {

    int parent;
    List<Integer> kids;
    boolean expanded;
    boolean hidden;
    int level;
    int allChildCount;

    public CommentParcel() {
    }

    public CommentParcel(Comment comment) {
        super(comment.getId(), comment.isDeleted(), comment.getType().toString(), comment.getBy(),
                comment.getTimeInSeconds(), comment.getText(), comment.isDead());
        parent = comment.getParentId();
        kids = comment.getCommentIds();
        expanded = comment.isExpanded();
        hidden = comment.isHidden();
        level = comment.getLevel();
        allChildCount = comment.getAllChildCount();
    }

    public static List<CommentParcel> fromComments(List<Comment> comments) {
        ArrayList<CommentParcel> retval = new ArrayList<>();
        for (Comment comment : comments) {
            retval.add(new CommentParcel(comment));
        }

        return retval;
    }

    public static List<Comment> fromCommentParcels(List<CommentParcel> commentParcels, News news) {
        List<Comment> retval = new ArrayList<>();
        List<Comment> comments = convertToComments(commentParcels);

        Dictionary<Integer, Comment> commentDict = new Hashtable<>();
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

    private static List<Comment> convertToComments(List<CommentParcel> commentParcels) {
        ArrayList<Comment> retval = new ArrayList<>();
        for (CommentParcel commentParcel : commentParcels) {
            retval.add(new Comment(commentParcel));
        }

        return retval;
    }

    private static Comment getCommentWithChild(int level, Comment comment, Dictionary<Integer, Comment> commentDict) {
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

    private static List<Comment> flattenComments(List<Comment> comments) {
        List<Comment> retval = new ArrayList<>();

        for (Comment comment : comments) {
            retval.add(comment);
            if (comment.getCommentIds().size() > 0) {
                retval.addAll(flattenComments(comment.getChildComments()));
            }
        }

        return retval;
    }

    public int getParentId() {
        return parent;
    }

    public List<Integer> getCommentIds() {
        return kids;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getLevel() {
        return level;
    }

    public int getAllChildCount() {
        return allChildCount;
    }

}
