package io.pumpkinz.pumpkinreader.model;

/**
 * @author timotiusnc
 * @since 6/27/15.
 */

public abstract class Item {
    private int id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String text;
    private boolean dead;

    public Item() {}

    public Item(int id, boolean deleted, String type, String by, long time, String text, boolean dead) {
        this.id = id;
        this.deleted = deleted;
        this.type = type;
        this.by = by;
        this.time = time;
        this.text = text;
        this.dead = dead;
    }

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
        return dead;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("ID = " + getId())
                .append("\nType = " + getType())
                .append("\nBy = " + getBy())
                .append("\nTime = " + getTime())
                .append("\nText = " + getText());
        return sb.toString();
    }
}


