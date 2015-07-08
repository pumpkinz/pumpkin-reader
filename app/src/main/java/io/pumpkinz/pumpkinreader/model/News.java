package io.pumpkinz.pumpkinreader.model;

import java.util.ArrayList;
import java.util.List;

import io.pumpkinz.pumpkinreader.service.CommentSource;
import rx.Observable;


public abstract class News extends Item {

    List<Integer> kids;
    String url;
    int score;
    String title;
    int descendants;

    public News() {
        this.kids = new ArrayList<>();
        this.descendants = 0;
    }

    public News(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, String url, int score, String title) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
        this.kids = new ArrayList<>();
        this.descendants = 0;
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

    public Observable<List<Comment>> getTopLevelComments() {
        return CommentSource.service().getComments(this);
    }

    public int getTotalComments() {
        return descendants;
    }

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append("URL=" + getUrl())
                .append("; Score=" + getScore())
                .append("; Title=" + getTitle())
                .append("; Descendants=" + getTotalComments())
                .append("; Kids=" + getKids().toString())
                .append("\n");

        return sb.toString();
    }

}
