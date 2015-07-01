package io.pumpkinz.pumpkinreader.model;

import java.util.Date;


public abstract class Item {

    int id;
    boolean deleted;
    Item.Type type;
    String by;
    Date time;
    String text;
    boolean dead;

    public Item() {}

    public Item(int id, boolean deleted, String type, String by, long time, String text,
                boolean dead) {
        this.id = id;
        this.deleted = deleted;
        this.type = Item.Type.fromString(type);
        this.by = by;
        this.time = new Date(time * 1000);
        this.text = text;
        this.dead = dead;
    }

    public Item(ItemPOJO itemPOJO) {
        this.id = itemPOJO.getId();
        this.deleted = itemPOJO.getDeleted();
        this.type = Item.Type.fromString(itemPOJO.getType());
        this.by = itemPOJO.getBy();
        this.time = new Date(itemPOJO.getTime() * 1000);
        this.text = itemPOJO.getText();
        this.dead = itemPOJO.getDead();
    }

    public static Item valueOf(ItemPOJO itemPOJO) {
        Item.Type type = Item.Type.fromString(itemPOJO.getType());
        switch(type) {
            case News: return new News(itemPOJO);
            case Comment: return new Comment(itemPOJO);
            case Job: return new Job(itemPOJO);
            case Poll: return new Poll(itemPOJO);
            case PollOpt: return new PollOpt(itemPOJO);
            default: throw new AssertionError("Unknown type: " + type);
        }
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
    public Item.Type getType() {
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
        News("story"),
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
