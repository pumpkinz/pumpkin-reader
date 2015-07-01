package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Parcel
public class News extends Item implements Serializable {

    List<Integer> kids;
    String url;
    int score;
    String title;
    int descendants;
    List<Comment> comments;

    public News() {}

    public News(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, String url, int score, String title,
                int descendants, List<Comment> comments) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
        this.descendants = descendants;
        this.comments = comments;
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

    public int getDescendants() {
        return descendants;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) { this.comments = comments; }

}
