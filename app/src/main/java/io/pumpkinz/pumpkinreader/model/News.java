package io.pumpkinz.pumpkinreader.model;

import java.util.ArrayList;
import java.util.List;


public abstract class News extends Item {

    List<Integer> kids;
    String url;
    int score;
    String title;
    List<Comment> comments;

    public News() {
        this.comments = new ArrayList<>();
        this.kids = new ArrayList<>();
    }

    public News(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, String url, int score, String title) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public String getUrl() {
        return url;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append("URL=" + getUrl())
                .append("; Score=" + getScore())
                .append("; Title=" + getTitle())
                .append("; Kids=" + getKids().toString())
                .append("\n");

        return sb.toString();
    }

}
