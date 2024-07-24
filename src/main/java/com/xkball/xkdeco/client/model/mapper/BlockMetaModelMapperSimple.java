package com.xkball.xkdeco.client.model.mapper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xkball.xkdeco.client.model.JsonModelBaked;
import com.xkball.xkdeco.client.model.JsonModelCandidate;
import com.xkball.xkdeco.utils.VanillaUtils;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;

public class BlockMetaModelMapperSimple implements BlockMetaModelMapper {

    private final Block block;
    private final int pass;
    private final JsonModelCandidate candidate;

    public BlockMetaModelMapperSimple(Block block, int pass, JsonModelCandidate candidate) {
        this.block = block;
        this.pass = pass;
        this.candidate = candidate;
    }

    @Override
    @Nullable
    public JsonModelBaked getModel(int meta) {
        return candidate.getModel();
    }

    @Override
    public Collection<ResourceLocation> getModelLocations() {
        return candidate.getModelLocations();
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public int getPass() {
        return pass;
    }

    public static class Deserializer implements JsonDeserializer<BlockMetaModelMapperSimple> {

        @Override
        public BlockMetaModelMapperSimple deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var obj = json.getAsJsonObject();
            var block_ = VanillaUtils.getBlock(obj.get("block_id").getAsString());
            var pass_ = obj.get("pass").getAsInt();
            if (block_ == null) throw new JsonParseException("Block not found");
            var candidate_ = (JsonModelCandidate) context.deserialize(json, JsonModelCandidate.class);
            return new BlockMetaModelMapperSimple(block_,pass_,candidate_);
        }
    }

}
