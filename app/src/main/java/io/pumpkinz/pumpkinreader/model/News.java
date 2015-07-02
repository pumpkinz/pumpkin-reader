package io.pumpkinz.pumpkinreader.model;

import java.util.List;


public abstract class News extends Item {

    List<Integer> kids;
    String url;
    int score;
    String title;
    int descendants;
    List<Comment> comments;

    public News() {}

    public News(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, String url, int score, String title,
                int descendants) {
        super(id, deleted, type, by, time, text, dead);
        this.kids = kids;
        this.url = url;
        this.score = score;
        this.title = title;
        this.descendants = descendants;
    }

    public News(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.kids = itemPOJO.getKids();
        this.url = itemPOJO.getUrl();
        this.score = itemPOJO.getScore();
        this.title = itemPOJO.getTitle();
        this.descendants = itemPOJO.getDescendants();
    }

    public static News valueOf(ItemPOJO itemPOJO) {
        Item.Type type = Item.Type.fromString(itemPOJO.getType());
        switch(type) {
            case Story: return new Story(itemPOJO);
            case Job: return new Job(itemPOJO);
            case Poll: return new Poll(itemPOJO);
            default: throw new AssertionError("Unknown News type: " + type);
        }
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
