package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.List;


@Parcel
public class Comment extends Item implements Serializable {

    int parent;
    List<Integer> kids;
    Comment parentComment;
    List<Comment> comments;

    public Comment() {}

    public Comment(int id, boolean deleted, String type, String by, long time, String text,
                   boolean dead, int parent, List<Integer> kids) {
        super(id, deleted, type, by, time, text, dead);
        this.parent = parent;
        this.kids = kids;
    }

    public Comment(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.parent = itemPOJO.getParent();
        this.kids = itemPOJO.getKids();
    }

    public int getParent() {
        return parent;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
