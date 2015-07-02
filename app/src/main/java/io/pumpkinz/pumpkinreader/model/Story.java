package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Story extends News implements Serializable {

    public Story() {}

    public Story(int id, boolean deleted, String type, String by, long time, String text,
                 boolean dead, List<Integer> kids, String url, int score, String title,
                 int descendants, List<Comment> comments) {
        super(id, deleted, type, by, time, text, dead, kids, url, score, title, descendants);
        this.comments = comments;
    }

    public Story(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.comments = new ArrayList<>();
    }

}
