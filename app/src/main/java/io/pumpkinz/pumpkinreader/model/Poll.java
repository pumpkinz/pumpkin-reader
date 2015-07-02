package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Parcel
public class Poll extends News implements Serializable {

    List<Integer> parts;
    List<PollOpt> pollOpts;

    public Poll() {}

    public Poll(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, int score, String url, String title,
                List<Integer> parts, int descendants) {
        super(id, deleted, type, by, time, text, dead, kids, url, score, title, descendants);
        this.parts = parts;
        this.comments = new ArrayList<>();
    }

    public Poll(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.kids = itemPOJO.getKids();
        this.parts = itemPOJO.getParts();
        this.descendants = itemPOJO.getDescendants();
        this.comments = new ArrayList<>();
    }

    public List<Integer> getParts() {
        return parts;
    }

    public List<PollOpt> getPollOpts() {
        return pollOpts;
    }

    public void setPollOpts(List<PollOpt> pollOpts) {
        this.pollOpts = pollOpts;
    }

    @Override
    public String toString() {
        String parent = super.toString();
        return parent.toString();
    }

}
