package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;


@Parcel
public class Job extends Item implements Serializable {

    private String url;
    private int score;
    private String title;

    public Job() {}

    public Job(int id, boolean deleted, String type, String by, long time, String text,
               boolean dead, String url, int score, String title) {
        super(id, deleted, type, by, time, text, dead);
        this.url = url;
        this.score = score;
        this.title = title;
    }

    public Job(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.url = itemPOJO.getUrl();
        this.score = itemPOJO.getScore();
        this.title = itemPOJO.getTitle();
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

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append("URL=" + getUrl())
                .append("; Score=" + getScore())
                .append("; Title=" + getTitle())
                .append("\n");

        return sb.toString();
    }

}
