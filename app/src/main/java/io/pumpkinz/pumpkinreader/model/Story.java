package io.pumpkinz.pumpkinreader.model;

import java.util.List;

/**
* @author timotiusnc
* @since 6/27/15.
*/

public class Story extends Item {
    private List<Integer> kids;
    private String url;
    private int score;
    private String title;
    private int descendants;

    private Story() {};

    public Story(int id, boolean deleted, String type, String by, long time, String text, boolean dead, List<Integer> kids, String url, int score, String title, int descendants) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
        this.descendants = descendants;
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

    @Override
    public String toString() {
        String parent = super.toString();
        StringBuilder sb = new StringBuilder(parent);
        sb.append("\nKids = " + getKids().toString())
                .append("\nURL = " + getUrl())
                .append("\nTitle = " + getTitle())
                .append("\nDescendants = " + getDescendants());

        return sb.toString();
    }
}
