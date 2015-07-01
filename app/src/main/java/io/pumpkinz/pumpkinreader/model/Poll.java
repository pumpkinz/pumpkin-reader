package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.List;


@Parcel
public class Poll extends Item implements Serializable {

    List<Integer> kids;
    String url;
    int score;
    String title;
    List<Integer> parts;
    int descendants;

    public Poll() {}

    public Poll(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, int score, String url, String title,
                List<Integer> parts, int descendants) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
        this.parts = parts;
        this.descendants = descendants;
    }

    public Poll(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.kids = itemPOJO.getKids();
        this.url = itemPOJO.getUrl();
        this.score = itemPOJO.getScore();
        this.title = itemPOJO.getTitle();
        this.parts = itemPOJO.getParts();
        this.descendants = itemPOJO.getDescendants();
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

    public List<Integer> getParts() {
        return parts;
    }

    public int getDescendants() {
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
                .append("; Descendants=" + getDescendants())
                .append("\n");

        return sb.toString();
    }

}
