package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Parcel
public class Comment extends Item implements Serializable {

    int parent;
    List<Integer> kids;
    Comment parentComment;
    List<Comment> childComments;

    public Comment() {
        this.kids = new ArrayList<>();
    }

    public Comment(int id, boolean deleted, String type, String by, long time, String text,
                   boolean dead, int parent, List<Integer> kids) {
        super(id, deleted, type, by, time, text, dead);
        this.parent = parent;
        this.kids = kids;
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

    public List<Comment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<Comment> childComments) {
        this.childComments = childComments;
    }

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append(";Kids=" + getKids().toString())
                .append("Text=" + getText().substring(0, Math.min(getText().length(), 20)))
                .append("\n");

        return sb.toString();
    }
}
