package com.xkball.xkdeco.client.model.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemMetaModelMapperImpl implements ItemMetaModelMapper {
    public static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(ItemMetaModelMapperImpl.class,new ItemMetaModelMapperImpl.Deserializer())
        .registerTypeAdapter(SimpleMetaMapper.class,new SimpleMetaMapper.Deserializer())
        .create();
    public final String id;
    private final List<SimpleMetaMapper> simpleMetaMappers;

    public ItemMetaModelMapperImpl(String id, List<SimpleMetaMapper> simpleMetaMappers) {
        this.id = id;
        this.simpleMetaMappers = simpleMetaMappers;
    }

    @Nullable
    @Override
    public ResourceLocation mapModel(ItemStack itemStack) {
        if(itemStack.getItem() == null || !id.equals(itemStack.getItem().unlocalizedName)) return null;
        var meta = itemStack.getItemDamage();
        for (var metaMapper : simpleMetaMappers) {
            if(metaMapper.accept(meta)){
                return metaMapper.rl;
            }
        }
        return null;
    }

    @Override
    public Collection<ResourceLocation> possibleResourceLocations() {
        var result = new ArrayList<ResourceLocation>();
        for (var metaMapper : simpleMetaMappers) {
            result.add(metaMapper.rl);
        }
        return result;
    }

    static class Deserializer implements JsonDeserializer<ItemMetaModelMapperImpl> {

        @Override
        public ItemMetaModelMapperImpl deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var obj = json.getAsJsonObject();
            var id = obj.get("item_id").getAsString();
            var list = new ArrayList<SimpleMetaMapper>();
            if(obj.has("model")){
                var rl = new ResourceLocation(obj.get("model").getAsString());
                var mapper = new SimpleMetaMapper(0,32768,rl);

                list.add(mapper);
                return new ItemMetaModelMapperImpl(id,list);
            }
            var mapperArray = obj.get("mapper").getAsJsonArray();
            for (JsonElement jsonElement : mapperArray) {
                list.add(context.deserialize(jsonElement, SimpleMetaMapper.class));
            }
            return new ItemMetaModelMapperImpl(id,list);
        }
    }

    public static class SimpleMetaMapper{
        public final int start; //include
        public final int end;   //exclude
        public final ResourceLocation rl;

        SimpleMetaMapper(int start, int end, ResourceLocation rl) {
            this.start = start;
            this.end = end;
            this.rl = rl;
        }

        public boolean accept(int meta){
            return start <= meta && meta < end;
        }

        static class Deserializer implements JsonDeserializer<SimpleMetaMapper> {

            @Override
            public SimpleMetaMapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                var obj = json.getAsJsonObject();
                var start = obj.get("start").getAsInt();
                var end = obj.get("end").getAsInt();
                var rl = new ResourceLocation(obj.get("model").getAsString());
                return new SimpleMetaMapper(start, end, rl);
            }
        }
    }
}
