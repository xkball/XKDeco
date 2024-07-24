package com.xkball.xkdeco.client.model.mapper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xkball.xkdeco.client.model.JsonModelBaked;
import com.xkball.xkdeco.client.model.JsonModelCandidate;
import com.xkball.xkdeco.utils.VanillaUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class BlockMetaModelMapperImpl implements BlockMetaModelMapper {

    private final Block block;
    private final int pass;
    private final JsonModelCandidate[] metaToCandidates = new JsonModelCandidate[16];

    public BlockMetaModelMapperImpl(Block block, int pass, Map<Integer, JsonModelCandidate> metaToCandidates) {
        this.block = block;
        this.pass = pass;
        for(var entry : metaToCandidates.entrySet()) {
            if(entry.getKey() >= 0 && entry.getKey() < 16) {
                this.metaToCandidates[entry.getKey()] = entry.getValue();
            }
        }
    }

    @Nullable
    @Override
    public JsonModelBaked getModel(int meta) {
        assert meta>=0 && meta<16;
        return metaToCandidates[meta].getModel();
    }

    @Override
    public Collection<ResourceLocation> getModelLocations() {
        return Arrays.stream(metaToCandidates)
            .flatMap(candidate -> candidate.getModelLocations().stream())
            .collect(Collectors.toSet());
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public int getPass() {
        return pass;
    }

    public static class Deserializer implements JsonDeserializer<BlockMetaModelMapperImpl> {

        @Override
        public BlockMetaModelMapperImpl deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var obj = json.getAsJsonObject();
            var block_ = VanillaUtils.getBlock(obj.get("block_id").getAsString());
            var pass_ = obj.get("pass").getAsInt();
            if (block_ == null) throw new JsonParseException("Block not found");
            var mapperArray = obj.get("mapper").getAsJsonArray();
            var map = new HashMap<Integer, JsonModelCandidate>();
            for(var mapperElement : mapperArray) {
                var mapper = mapperElement.getAsJsonObject();
                var meta = mapper.get("meta");
                var candidate = (JsonModelCandidate)context.deserialize(mapper.get("candidate"), JsonModelCandidate.class);
                if(meta.isJsonArray()){
                    for(var m : meta.getAsJsonArray()) {
                        map.put(m.getAsInt(), candidate);
                    }
                }
                else{
                    map.put(meta.getAsInt(), candidate);
                }
            }
            return new BlockMetaModelMapperImpl(block_, pass_,map);
        }
    }
}
