package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;


@Parcel
public class PollOpt extends Item implements Serializable {

    int parent;
    int score;

    public PollOpt() {}

    public PollOpt(int id, boolean deleted, String type, String by, long time, String text,
                   boolean dead, int parent, int score) {
        super(id, deleted, type, by, time, text, dead);
        this.parent = parent;
        this.score = score;
    }

    public PollOpt(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.parent = itemPOJO.getParent();
        this.score = itemPOJO.getScore();
    }

    public int getParent() {
        return parent;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        String parent = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(parent)
                .append("Parent=" + getParent())
                .append("; Score=" + getScore())
                .append("\n");

        return sb.toString();
    }

}
