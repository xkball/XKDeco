package com.xkball.xkdeco.client.model.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xkball.xkdeco.client.model.JsonModelBaked;
import com.xkball.xkdeco.client.model.JsonModelCandidate;
import com.xkball.xkdeco.client.model.JsonModelWrapperBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;

@SideOnly(Side.CLIENT)
public interface BlockMetaModelMapper {

    Gson GSON = new GsonBuilder()
        .registerTypeAdapter(BlockMetaModelMapper.class,new BlockMetaModelMapper.Deserializer())
        .registerTypeAdapter(BlockMetaModelMapperImpl.class,new BlockMetaModelMapperImpl.Deserializer())
        .registerTypeAdapter(BlockMetaModelMapperSimple.class,new BlockMetaModelMapperSimple.Deserializer())
        .registerTypeAdapter(JsonModelCandidate.class,new JsonModelCandidate.Deserializer())
        .registerTypeAdapter(JsonModelWrapperBlock.class,new JsonModelWrapperBlock.Deserializer())
        .create();

    //block信息不在此处 换言之一种block对应一个实现
    @Nullable
    JsonModelBaked getModel(int meta);

    Collection<ResourceLocation> getModelLocations();

    Block getBlock();

    int getPass();

    class Deserializer implements JsonDeserializer<BlockMetaModelMapper> {

        @Override
        public BlockMetaModelMapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var obj = json.getAsJsonObject();
            if(obj.has("model")) {
                return context.deserialize(json, BlockMetaModelMapperSimple.class);
            }
            return context.deserialize(json, BlockMetaModelMapperImpl.class);
        }
    }

}
