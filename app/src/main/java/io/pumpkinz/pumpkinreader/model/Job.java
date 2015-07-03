package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.ArrayList;


@Parcel
public class Job extends News implements Serializable {

    public Job() {
        this.comments = new ArrayList<>();
    }

    public Job(int id, boolean deleted, String type, String by, long time, String text,
               boolean dead, String url, int score, String title) {
        super(id, deleted, type, by, time, text, dead, new ArrayList<Integer>(), url, score,
                title, 0);
        this.comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        String parent = super.toString();
        return parent.toString();
    }

}
