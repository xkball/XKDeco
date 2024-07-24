package com.xkball.xkdeco.client;

import com.google.common.collect.Maps;
import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.client.model.mapper.ItemMetaModelMapper;
import com.xkball.xkdeco.client.model.mapper.ItemMetaModelMapperImpl;
import com.xkball.xkdeco.client.render.model.JsonModelItemRender;
import com.xkball.xkdeco.utils.FileUtils;
import com.xkball.xkdeco.utils.VanillaUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public enum ItemModelModifiedManager {
    INSTANCE;

    private final List<ResourceLocation> usedModel = new ArrayList<>();
    private Map<Item, ItemMetaModelMapper> itemMappers = Maps.newIdentityHashMap();
    private static final JsonModelItemRender RENDER = new JsonModelItemRender();

    public void reload(){
        Map<Item, ItemMetaModelMapper> newItemMap = Maps.newIdentityHashMap();
        try {
            if(XKDeco.configDir == null) return;
            var itemDir = new File(XKDeco.configDir, "/item_model_mapper");
            if(!itemDir.exists()){
                if (!itemDir.mkdirs()) {
                    XKDeco.LOG.warn("could not create directory: {}", itemDir.getAbsolutePath());
                }
            }
            FileUtils.recursionVisitFile(itemDir,(file) -> {
                if(!file.getName().endsWith(".imm")) return;
                try (var fileReader = new FileReader(file)) {
                    var mapper = ItemMetaModelMapperImpl.GSON.fromJson(fileReader, ItemMetaModelMapperImpl.class);
                    if(mapper == null){
                        XKDeco.LOG.warn("could not parse model mapper file: {}", file.getAbsolutePath());
                        return;
                    }
                    var fileName = file.getName().replace(".imm", "");
                    if(!fileName.equals(mapper.id)) XKDeco.LOG.warn("mapper file name not match the item name, this is not suggested : {}", fileName);
                    newItemMap.put(VanillaUtils.getItem(mapper.id),mapper);
                }
                catch (IOException e) {
                    XKDeco.LOG.error("cannot read file",e);
                }
            });
        }catch (Exception e){
            //todo 报错
            return;
        }
        itemMappers = newItemMap;
        this.collectModelResourceLocation();
    }

    private void collectModelResourceLocation(){
        usedModel.clear();
        for(var entry : itemMappers.entrySet()){
            usedModel.addAll(entry.getValue().possibleResourceLocations());
        }
    }

    public List<ResourceLocation> getUsedModel() {
        return usedModel;
    }

    public ItemMetaModelMapper getItemMapper(Item item){
        return itemMappers.get(item);
    }

    public void regRenderToForge(){
        for(var key : itemMappers.keySet()){
            MinecraftForgeClient.registerItemRenderer(key,RENDER);
        }
    }
}
