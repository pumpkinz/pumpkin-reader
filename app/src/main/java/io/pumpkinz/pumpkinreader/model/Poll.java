package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Parcel
public class Poll extends News implements Serializable {

    List<Integer> parts;
    List<PollOpt> pollOpts;

    public Poll() {
    }

    public Poll(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead, List<Integer> kids, int score, String url, String title,
                List<Integer> parts) {
        super(id, deleted, type, by, time, text, dead, kids, url, score, title);
        this.parts = parts;
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

}
