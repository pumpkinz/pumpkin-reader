package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Parcel
public class News implements Serializable {

    String title;
    String body;
    String submitter;
    String url;
    Date date;
    int score;
    List<Comment> comments;

    public News() {
    }

    public News(String title, String body, String submitter, String url, Date date, int score, List<Comment> comments) {
        this.title = title;
        this.body = body;
        this.submitter = submitter;
        this.url = url;
        this.date = date;
        this.score = score;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getSubmitter() {
        return submitter;
    }

    public String getUrl() {
        return url;
    }

    public Date getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
