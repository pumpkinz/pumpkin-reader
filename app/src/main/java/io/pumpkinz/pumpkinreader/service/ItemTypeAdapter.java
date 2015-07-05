package io.pumpkinz.pumpkinreader.service;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.Comment;
import io.pumpkinz.pumpkinreader.model.Item;
import io.pumpkinz.pumpkinreader.model.Job;
import io.pumpkinz.pumpkinreader.model.Poll;
import io.pumpkinz.pumpkinreader.model.PollOpt;
import io.pumpkinz.pumpkinreader.model.Story;


public class ItemTypeAdapter implements JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        try {
            Item.Type type = Item.Type.fromString(jo.get("type").getAsString());

            switch (type) {
                case Story:
                    return context.deserialize(json, Story.class);
                case Comment:
                    return context.deserialize(json, Comment.class);
                case Job:
                    return context.deserialize(json, Job.class);
                case Poll:
                    return context.deserialize(json, Poll.class);
                case PollOpt:
                    return context.deserialize(json, PollOpt.class);
                default:
                    throw new AssertionError("Unknown Item type: " + type);
            }
        } catch (AssertionError | JsonParseException ex) {
            Log.d(Constants.APP, ex.getMessage());
        }

        return null;
    }

}
