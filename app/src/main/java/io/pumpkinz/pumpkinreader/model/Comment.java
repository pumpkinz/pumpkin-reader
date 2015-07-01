package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.List;


@Parcel
public class Comment extends Item implements Serializable {

    int parent;
    List<Integer> kids;

    public Comment() {}

    public Comment(int id, boolean deleted, String type, String by, long time, String text,
                   boolean dead, int parent, List<Integer> kids) {
        super(id, deleted, type, by, time, text, dead);
        this.parent = parent;
        this.kids = kids;
    }

    public Comment(ItemPOJO itemPOJO) {
        super(itemPOJO);
        this.parent = itemPOJO.getParent();
        this.kids = itemPOJO.getKids();
    }

    public int getParent() {
        return parent;
    }

    public List<Integer> getKids() {
        return kids;
    }

}
