package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.Date;


@Parcel
public class Comment implements Serializable {

    String submitter;
    Date date;
    String body;

    public Comment() {
    }

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
