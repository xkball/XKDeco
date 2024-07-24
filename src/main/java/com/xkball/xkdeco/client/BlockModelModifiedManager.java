package com.xkball.xkdeco.client;

import com.google.common.collect.Maps;
import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.api.client.block.IJsonModelBlock;
import com.xkball.xkdeco.client.model.mapper.BlockMetaModelMapper;
import com.xkball.xkdeco.utils.FileUtils;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum BlockModelModifiedManager {
    INSTANCE;

    private final List<ResourceLocation> usedModel = new ArrayList<>();
    private Map<Block, BlockMetaModelMapper> blockMapper = Maps.newHashMap();

    public void reload(){
        this.deInit();
        Map<Block, BlockMetaModelMapper> newMap = Maps.newHashMap();
        try {
            if(XKDeco.configDir == null) return;
            var itemDir = new File(XKDeco.configDir, "/block_model_mapper");
            if(!itemDir.exists()){
                if (!itemDir.mkdirs()) {
                    XKDeco.LOG.warn("could not create directory: {}", itemDir.getAbsolutePath());
                }
            }
            FileUtils.recursionVisitFile(itemDir,(file) -> {
                if(!file.getName().endsWith(".bmm")) return;
                try (var fileReader = new FileReader(file)) {
                    var mapper = BlockMetaModelMapper.GSON.fromJson(fileReader, BlockMetaModelMapper.class);
                    if(mapper == null){
                        XKDeco.LOG.warn("could not parse model mapper file: {}", file.getAbsolutePath());
                        return;
                    }
                    var fileName = file.getName().replace(".bmm", "");
                    if(!fileName.equals(mapper.getBlock().unlocalizedName)) XKDeco.LOG.warn("mapper file name not match the item name, this is not suggested : {}", fileName);
                    newMap.put(mapper.getBlock(),mapper);
                    IJsonModelBlock.cast(mapper.getBlock()).xkdeco$setBlockJsonModelMapper(mapper);
                }
                catch (IOException e) {
                    XKDeco.LOG.error("cannot read file",e);
                }
            });
        }catch (Exception e){
            //todo 报错
            return;
        }
        blockMapper = newMap;
        this.collectModelResourceLocation();
    }

    public void deInit(){
        for(var entry : blockMapper.entrySet()){
            IJsonModelBlock.cast(entry.getKey()).xkdeco$setBlockJsonModelMapper(null);
        }
    }

    private void collectModelResourceLocation(){
        usedModel.clear();
        for(var entry : blockMapper.entrySet()){
            usedModel.addAll(entry.getValue().getModelLocations());
        }
    }

    public List<ResourceLocation> getUsedModel() {
        return usedModel;
    }

    @Nullable
    public BlockMetaModelMapper getBlockMapper(Block block){
        return blockMapper.get(block);
    }
}
