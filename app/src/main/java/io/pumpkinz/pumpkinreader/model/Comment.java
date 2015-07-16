package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Parcel
public class Comment extends Item implements Serializable {

    int parent;
    List<Integer> kids;
    List<Comment> childComments;
    boolean expanded;
    boolean hidden;
    int level;
    int allChildCount;

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
                .append(";\tLevel=" + String.valueOf(getLevel()))
                .append(";\tKids=" + getCommentIds().toString())
                .append(";\tAll Kids=" + String.valueOf(getAllChildCount()))
                .append("\tText=" + getText().substring(0, Math.min(getText().length(), 20)))
                .append("\n");

        return sb.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

}
