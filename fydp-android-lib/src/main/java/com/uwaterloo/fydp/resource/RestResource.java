package com.uwaterloo.fydp.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public abstract class RestResource {

    protected static Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampDeserializer()).create();
}

class TimestampDeserializer implements JsonDeserializer<Timestamp>
{
    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        long time = Long.parseLong(json.getAsString());
        return new Timestamp(time);
    }
}
