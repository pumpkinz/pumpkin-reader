package io.pumpkinz.pumpkinreader.model;

import java.util.List;

/**
 * @author timotiusnc
 * @since 6/27/15.
 * This is a POJO (Plain Old Java Object) to contain the JSON returned by HnApiService getItem
 * (The JSON returned will be automatically converted into this POJO)
 *
 * A Hacker News item can be one of the following (https://github.com/HackerNews/API):
 * 1. Story
 * 2. Comment
 * 3. Job
 * 4. Poll
 * 5. Pollopt
 */

public class ItemPOJO {
    // Common fields
    private int id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String text;
    private boolean dead;

    // Specific fields
    private int parent;
    private List<Integer> kids;
    private String url;
    private int score;
    private String title;
    private int descendants;

    // Poll only fields
    private List<Integer> parts;

    public int getId() {
        return id;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public String getType() {
        return type;
    }

    public String getBy() {
        return by;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public boolean getDead() {
        return isDead();
    }

    public boolean isDead() {
        return dead;
    }

    public int getParent() {
        return parent;
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

    public List<Integer> getParts() {
        return parts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("ID = " + getId())
                .append("\nType = " + getType())
                .append("\nBy = " + getBy())
                .append("\nTime = " + getTime())
                .append("\nText = " + getText())
                .append("\nURL = " + getUrl())
                .append("\nDescendants = " + getDescendants())
                .append("\nKids = " + getKids().toString());
        return sb.toString();
    }
}


