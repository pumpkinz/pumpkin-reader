package io.pumpkinz.pumpkinreader.model;


import java.util.Date;
import java.util.List;

public class News {

    private String title;
    private String body;
    private String submitter;
    private String url;
    private Date date;
    private int score;
    private List<Comment> comments;

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
