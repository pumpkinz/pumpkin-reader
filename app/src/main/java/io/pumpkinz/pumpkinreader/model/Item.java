package io.pumpkinz.pumpkinreader.model;

import org.parceler.Parcel;
import java.io.Serializable;
import java.util.Date;


/**
 * Base class for a Hacker News item
 *
 * @author timotiusnc
 * @since 01/07/15
 */

public abstract class Item {

    int id;
    boolean deleted;
    String type;
    String by;
    Date time;
    String text;
    boolean dead;

    public Item() {}

    public Item(int id, boolean deleted, String type, String by, long time, String text, boolean dead) {
        this.id = id;
        this.deleted = deleted;
        this.type = type;
        this.by = by;
        this.time = new Date(time);
        this.text = text;
        this.dead = dead;
    }

    /** The item's unique ID */
    public int getId() {
        return id;
    }

    /** true if the item is deleted */
    public boolean isDeleted() {
        return deleted;
    }

    /** The type of item */
    public String getType() {
        return type;
    }

    /** The username of the item's author */
    public String getBy() {
        return by;
    }

    /** Creation date of the item, in java.util.Date */
    public Date getTime() {
        return time;
    }

    /** The comment, story or poll text. HTML. */
    public String getText() {
        return text;
    }

    /** true if the item is dead */
    public boolean isDead() {
        return dead;
    }
}
