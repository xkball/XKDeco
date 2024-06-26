package com.xkball.xkdeco.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.api.event.RegisterJsonModelEvent;
import com.xkball.xkdeco.client.model.JsonModelBaked;
import com.xkball.xkdeco.utils.exception.ModelParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonParser;
import com.xkball.xkdeco.client.model.JsonModelRaw;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public enum JsonModelManager {

    INSTANCE;

    private final Map<ResourceLocation, JsonModelRaw> rawModels = new HashMap<>();
    private final Map<ResourceLocation, JsonModelBaked> bakedModels = new HashMap<>();
    private final Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();
    private final List<ResourceLocation> textures = new ArrayList<>();
    private TextureMap textureMap;

    public void loadRawModels(IIconRegister iconRegister) {
        XKDeco.LOG.info("XKDeco: Start loading JsonModels");
        long time = System.currentTimeMillis();
        rawModels.clear();
        bakedModels.clear();
        var resourceManager = Minecraft.getMinecraft()
            .getResourceManager();
        for (var location : registerModel()) {
            try {
                var resource = resourceManager.getResource(location);
                var json = new JsonParser().parse(new InputStreamReader(resource.getInputStream()));
                rawModels.put(location, new JsonModelRaw(location, json.getAsJsonObject()));
            }
            catch (ModelParseException e) {
                XKDeco.LOG.error("XKDeco: Error parsing JsonModel{}", location);
                XKDeco.LOG.error(e);
            }
            catch (IOException e) {
                XKDeco.LOG.error("XKDeco: Error reading JsonModel {}", location);
                XKDeco.LOG.error(e);
            }
        }
        //todo 分析需要加载的上级模型
        this.registerIcons(iconRegister);
        time = System.currentTimeMillis() - time;
        XKDeco.LOG.info("XKDeco: Loaded {} JsonModels in {}ms", rawModels.size(), time);
    }

    public void bakeRawModels(TextureMap textureMap) {
        XKDeco.LOG.info("XKDeco: Start baking JsonModels");
        long time = System.currentTimeMillis();
        bakedModels.clear();
        this.textureMap = textureMap;
        sprites.clear();
        textures.stream()
            .filter(rl -> textureMap.getAtlasSprite(rl.toString()) != null)
            .forEach(rl -> sprites.put(rl, textureMap.getAtlasSprite(rl.toString())));
        for(var entry : rawModels.entrySet()) {
            try {
                var baked = entry.getValue().bake(textureMap);
                if(baked != null) bakedModels.put(entry.getKey(),baked);
            }catch (ModelParseException e){
                XKDeco.LOG.error("XKDeco: Error baking JsonModel {}", entry.getKey());
                XKDeco.LOG.error(e);
            }
        }
        time = System.currentTimeMillis() - time;
        XKDeco.LOG.info("XKDeco: Baked {} JsonModels in {}ms", bakedModels.size(), time);
    }

    private void registerIcons(IIconRegister iconRegister){
        textures.clear();
        this.rawModels.entrySet().stream()
            .flatMap(entry -> entry.getValue().getTextures().entrySet().stream())
            .filter(texture -> texture.getValue().location != null)
            .map( texture -> texture.getValue().location)
            .collect(Collectors.toSet())
            .forEach(texture -> {
                textures.add(texture);
                iconRegister.registerIcon(texture.toString());
                XKDeco.LOG.debug("XKDeco: Registered Icon {}", texture);
            });

    }

    public IIcon getIcon(ResourceLocation location) {
        return sprites.get(location);
    }

    public List<ResourceLocation> registerModel() {
        var result = new ArrayList<ResourceLocation>();
        result.add(new ResourceLocation("xkdeco", "models/block/furniture/globe.json"));
        result.add(new ResourceLocation("xkdeco", "models/block/charger_ae.json"));
        result.add(new ResourceLocation("xkdeco", "models/block/furniture/fish_bowl.json"));
        result.add(new ResourceLocation("xkdeco", "models/block/furniture/dark_fish_bowl.json"));
        result.add(new ResourceLocation("xkdeco", "models/block/furniture/tech_screen.json"));
        var event = new RegisterJsonModelEvent(new ArrayList<>());
        MinecraftForge.EVENT_BUS.post(event);
        result.addAll(event.append);
        return result;
    }

    @Nullable
    public JsonModelRaw getRawModel(ResourceLocation location) {
        return rawModels.getOrDefault(location, null);
    }

    @Nullable
    public JsonModelBaked getBakedModel(ResourceLocation location) {
        return bakedModels.getOrDefault(location, null);
    }

}
