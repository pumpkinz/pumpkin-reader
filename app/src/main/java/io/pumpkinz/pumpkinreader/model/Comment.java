package io.pumpkinz.pumpkinreader.model;


import java.util.Date;

public class Comment {

    private String submitter;
    private Date date;
    private String body;

    public Comment(String submitter, Date date, String body) {
        this.submitter = submitter;
        this.date = date;
        this.body = body;
    }

    public String getSubmitter() {
        return submitter;
    }

    public Date getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

}
