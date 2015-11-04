package io.pumpkinz.pumpkinreader.model;

import java.util.Date;


public abstract class Item {

    int id;
    boolean deleted;
    String type;
    String by;
    long time;
    String text;
    boolean dead;

    public Item() {
    }

    public Item(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public Item.Type getType() {
        return Item.Type.fromString(type);
    }

    public String getBy() {
        return by;
    }

    public Date getTime() {
        return new Date(time * 1000);
    }

    public long getTimeInSeconds() {
        return time;
    }

    public String getText() {
        return text;
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nID = " + getId())
                .append("; Type=" + getType())
                .append("; By=" + getBy())
                .append("; Time=" + getTime())
                .append("\n");

        return sb.toString();
    }

    public enum Type {
        Story("story"),
        Comment("comment"),
        Job("job"),
        Poll("poll"),
        PollOpt("pollopt");

        private String text;

        Type(String text) {
            this.text = text;
        }

        public static Type fromString(String text) {
            if (text != null) {
                for (Type type : Type.values()) {
                    if (text.equalsIgnoreCase(type.text)) {
                        return type;
                    }
                }
            }

            throw new IllegalArgumentException("No constant with text " + text + " found");
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
