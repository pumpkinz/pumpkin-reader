package io.pumpkinz.pumpkinreader.model;

import java.util.ArrayList;
import java.util.List;


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

    public List<Integer> getCommentIds() {
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
                .append("; Kids=" + getCommentIds().toString())
                .append("\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof News)) {
            return false;
        }

        News news = (News) o;
        return news.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getId();

        return result;
    }

}
