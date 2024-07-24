package com.xkball.xkdeco.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xkball.xkdeco.utils.WightLoopList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class JsonModelCandidate {
    private final List<JsonModelWrapperBlock> candidates = new ArrayList<>();
    private WightLoopList<JsonModelWrapperBlock> loopList = null;

    public JsonModelCandidate(JsonModelWrapperBlock... candidate) {
        this(Arrays.asList(candidate));
    }

    public JsonModelCandidate(Collection<JsonModelWrapperBlock> candidate) {
        candidates.addAll(candidate);
        if(candidates.size() > 1){
            loopList = new WightLoopList<>(candidates, JsonModelWrapperBlock::getWeight);
        }
    }

    @Nullable
    public JsonModelBaked getModel(){
        if (candidates.isEmpty()) return null;
        if (candidates.size() == 1) return candidates.get(0).getModel();
        return loopList.roll().getModel();
    }

    public Collection<ResourceLocation> getModelLocations() {
        return candidates.stream().map(JsonModelWrapperBlock::getLocation).collect(Collectors.toSet());
    }

    public static class Deserializer implements JsonDeserializer<JsonModelCandidate> {

        @Override
        public JsonModelCandidate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.isJsonObject()){
                var obj = json.getAsJsonObject();
                var wrapper = (JsonModelWrapperBlock) context.deserialize(obj, JsonModelWrapperBlock.class);
                return new JsonModelCandidate(wrapper);
            }
            else if(json.isJsonArray()){
                var a = json.getAsJsonArray();
                var list = new ArrayList<JsonModelWrapperBlock>(a.size());
                for(var element : a){
                    var wrapper = (JsonModelWrapperBlock) context.deserialize(element, JsonModelWrapperBlock.class);
                    list.add(wrapper);
                }
                return new JsonModelCandidate(list);
            }
            throw new JsonParseException("Cannot deserialize JsonModelCandidate from: " + json);
        }
    }
}
