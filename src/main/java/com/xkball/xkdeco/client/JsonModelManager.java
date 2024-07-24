package com.xkball.xkdeco.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.api.event.RegisterJsonModelEvent;
import com.xkball.xkdeco.client.model.JsonModelBaked;
import com.xkball.xkdeco.utils.exception.ModelParseException;
import net.minecraft.block.Block;
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
    private final List<Consumer<Function<ResourceLocation, JsonModelBaked>>> afterLoadedFunctions = new ArrayList<>();
    private TextureMap textureMap;

    public void loadRawModels(IIconRegister iconRegister) {
        XKDeco.LOG.info("XKDeco: Start loading JsonModels");
        long time = System.currentTimeMillis();
        rawModels.clear();
        bakedModels.clear();
        var resourceManager = Minecraft.getMinecraft().getResourceManager();
        var loadQueue = new ArrayDeque<>(registerModel());
        var jsonParser = new JsonParser();
        while (!loadQueue.isEmpty()) {
            var location = loadQueue.poll();
            if(rawModels.containsKey(location)) continue;
            try {
                var resource = resourceManager.getResource(fixUpResourceLocation(location));
                var json = jsonParser.parse(new InputStreamReader(resource.getInputStream()));
                var modelRaw = new JsonModelRaw(location, json.getAsJsonObject());
                rawModels.put(location, modelRaw);
                if(modelRaw.getParent() != null){
                    loadQueue.addFirst(modelRaw.getParent());
                }
            }
            catch (IllegalStateException | ModelParseException e){
                XKDeco.LOG.error("XKDeco: Error parsing JsonModel{}", location);
                XKDeco.LOG.error(e);
            }
            catch (IOException e) {
                XKDeco.LOG.error("XKDeco: Error reading JsonModel {}", location);
                XKDeco.LOG.error(e);
            }

        }
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
        afterLoadedFunctions.forEach(c -> c.accept(this::getBakedModel));
        time = System.currentTimeMillis() - time;
        XKDeco.LOG.info("XKDeco: Baked {} JsonModels in {}ms", bakedModels.size(), time);
    }

    private void registerIcons(IIconRegister iconRegister){
        textures.clear();
        this.rawModels.entrySet().stream()
            .flatMap(entry -> entry.getValue().getTextures().entrySet().stream())
            .filter(texture -> texture.getValue().location != null)
            .map( texture -> texture.getValue().location)
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
        result.add(new ResourceLocation("xkdeco", "block/furniture/globe"));
        result.add(new ResourceLocation("xkdeco", "block/charger_ae"));
        result.add(new ResourceLocation("xkdeco", "block/furniture/fish_bowl"));
        result.add(new ResourceLocation("xkdeco", "block/furniture/dark_fish_bowl"));
        result.add(new ResourceLocation("xkdeco", "block/furniture/tech_screen"));
        result.addAll(ItemModelModifiedManager.INSTANCE.getUsedModel());
        result.addAll(BlockModelModifiedManager.INSTANCE.getUsedModel());
        var event = new RegisterJsonModelEvent(new ArrayList<>());
        MinecraftForge.EVENT_BUS.post(event);
        result.addAll(event.append);
        return result;
    }

    @Nullable
    public JsonModelRaw getRawModel(@Nullable ResourceLocation location) {
        if(location == null) return null;
        return rawModels.getOrDefault(location, null);
    }

    @Nullable
    public JsonModelBaked getBakedModel(@Nullable ResourceLocation location) {
        if(location == null) return null;
        return bakedModels.getOrDefault(location, null);
    }

    @Nullable
    public JsonModelBaked getBakedModel(Block block){
        //todo
        return null;
    }

    public void addAfterLoaded(Consumer<Function<ResourceLocation, JsonModelBaked>> function) {
        afterLoadedFunctions.add(function);
    }

    public static ResourceLocation fixUpResourceLocation(ResourceLocation location) {
        var path = location.getResourcePath();
        if(!path.endsWith(".json")) path += ".json";
        if(!path.startsWith("models/")) path = "models/" + path;
        return new ResourceLocation(location.getResourceDomain(), path);
    }

}
