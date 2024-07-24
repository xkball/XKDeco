package com.xkball.xkdeco.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xkball.xkdeco.client.JsonModelManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

@SideOnly(Side.CLIENT)
public class JsonModelWrapperBlock {

    private JsonModelBaked model = null;
    private final ResourceLocation location;
    private final boolean uvlock;
    private final int x;
    private final int y;
    private final int weight;

    public JsonModelWrapperBlock(ResourceLocation location, boolean uvlock, int x, int y, int weight) {
        this.location = location;
        this.uvlock = uvlock;
        this.x = x;
        this.y = y;
        this.weight = weight;
        JsonModelManager.INSTANCE.addAfterLoaded( lookup -> this.setModel(lookup.apply(location)));
    }

    public void afterBake(){

    }

    public void setModel(JsonModelBaked model) {
        this.model = model;
    }

    @Nullable
    public JsonModelBaked getModel() {
        return model;
    }

    public int getWeight() {
        return weight;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public static class Deserializer implements JsonDeserializer<JsonModelWrapperBlock> {

        @Override
        public JsonModelWrapperBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var obj = json.getAsJsonObject();
            var rl = new ResourceLocation(obj.get("model").getAsString());
            var uvlock = obj.has("uvlock") && obj.get("uvlock").getAsBoolean();
            var x = obj.has("x") ? obj.get("x").getAsInt() : 0;
            var y = obj.has("y") ? obj.get("y").getAsInt() : 0;
            var weight = obj.has("weight") ? obj.get("weight").getAsInt() : 1;
            return new JsonModelWrapperBlock(rl, uvlock, x, y, weight);
        }
    }
}
