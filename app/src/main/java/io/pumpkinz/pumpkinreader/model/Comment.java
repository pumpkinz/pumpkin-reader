package io.pumpkinz.pumpkinreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.pumpkinz.pumpkinreader.util.CommentParcel;


public class Comment extends Item implements Serializable {

    int parent;
    List<Integer> kids;
    List<Comment> childComments;
    boolean expanded;
    boolean hidden;
    int level;
    int allChildCount;
    Comment parentComment;

    public Comment() {
        this.kids = new ArrayList<>();
        this.childComments = new ArrayList<>();
        this.expanded = true;
        this.hidden = false;
        this.level = 0;
        this.allChildCount = 0;
    }

    public Comment(int id, boolean deleted, String type, String by, long time, String text,
                   boolean dead, int parent, List<Integer> kids) {
        super(id, deleted, type, by, time, text, dead);
        this.parent = parent;
        this.kids = kids;
        this.childComments = new ArrayList<>();
        this.expanded = true;
        this.hidden = false;
        this.level = 0;
    }

    public Comment(CommentParcel commentParcel) {
        super(commentParcel.getId(), commentParcel.isDeleted(),
                commentParcel.getType().toString(), commentParcel.getBy(),
                commentParcel.getTimeInSeconds(), commentParcel.getText(),
                commentParcel.isDead());
        parent = commentParcel.getParentId();
        kids = commentParcel.getCommentIds();
        childComments = new ArrayList<>();
        expanded = commentParcel.isExpanded();
        hidden = commentParcel.isHidden();
        level = commentParcel.getLevel();
        allChildCount = commentParcel.getAllChildCount();
    }

    public int getParentId() {
        return parent;
    }

    public List<Integer> getCommentIds() {
        return kids;
    }

    public List<Comment> getChildComments() {
        return childComments;
    }

    public void addChildComment(Comment childComment) {
        this.childComments.add(childComment);
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAllChildCount() {
        return this.allChildCount;
    }

    public void setAllChildCount(int allChildCount) {
        this.allChildCount = allChildCount;
    }

    public Comment getParentComment() {
        return this.parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Comment)) return false;

        return this.id == ((Comment) o).getId();
    }

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append("Level=" + String.valueOf(getLevel()))
                .append("; Kids=" + getCommentIds().toString())
                .append("; All Kids=" + String.valueOf(getAllChildCount()))
                .append(" Text=" + getText().substring(0, Math.min(getText().length(), 20)))
                .append("\n");

        return sb.toString();
    }

}
