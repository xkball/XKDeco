package com.xkball.xkdeco.client.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.client.model.json.DataModelOverride;
import com.xkball.xkdeco.client.model.json.DataTransformer;
import com.xkball.xkdeco.client.model.json.JsonModelElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.client.EnumTransformer;
import com.xkball.xkdeco.client.JsonModelManager;
import com.xkball.xkdeco.utils.exception.ModelParseException;

//关键的不一致: elements无法跨越多层继承
//关键的不一致: texture的路径 默认路径textures/blocks/ 而不是textures/ 注意有s
//关键不一致: element必须有uv
//关键不一致: display仅gui有效 同时旋转和高版本不同
//警告: 无法自行决定renderpass 若pass为0则没有透明度 若pass为1则有透明度且没有深度缓冲
@SideOnly(Side.CLIENT)
public class JsonModelRaw {

    //反正也加载不出来
    public static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missing");
    protected final ResourceLocation location;
    protected ResourceLocation parent;
    protected final Map<String, TextureSymbol> textures = new HashMap<>();
    protected final List<JsonModelElement> elements = new ArrayList<>();
    protected final List<DataModelOverride> overrides = new ArrayList<>();
    protected final EnumMap<EnumTransformer, DataTransformer> transformers = new EnumMap<>(EnumTransformer.class);

    public JsonModelRaw(ResourceLocation location, JsonObject modelSrc) throws ModelParseException {
        try {
            this.location = location;
            this.parent = modelSrc.has("parent") ? new ResourceLocation(modelSrc.get("parent").getAsString()) : null;
            var textureObj = modelSrc.has("textures") ? modelSrc.get("textures").getAsJsonObject() : new JsonObject();
            for (var entry : textureObj.entrySet()) {
                if (entry.getValue().getAsString().startsWith("#")) {
                    textures.put(entry.getKey(), new TextureSymbol(
                            entry.getValue().getAsString().substring(1)));
                } else textures.put(entry.getKey(), new TextureSymbol(new ResourceLocation(entry.getValue().getAsString()), entry.getKey()));
            }
            var elementArray = modelSrc.has("elements") ? modelSrc.get("elements").getAsJsonArray() : new JsonArray();
            for (JsonElement element : elementArray) {
                elements.add(new JsonModelElement(element.getAsJsonObject(), location));
            }
            if (modelSrc.has("display")) {
                var displayObj = modelSrc.get("display").getAsJsonObject();
                for (var entry : displayObj.entrySet()) {
                    var trans = EnumTransformer.getEnumTransformer(entry.getKey());
                    if (trans != null) {
                        transformers.put(trans, DataTransformer.parseFromJson(entry.getValue().getAsJsonObject()));
                    }
                }
            }
            if (modelSrc.has("gui_light")) {
                XKDeco.LOG.warn("not support gui_light yet on model:{}", location);
            }
        } catch (ModelParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ModelParseException(e);
        }

    }

    public ResourceLocation getParticle(){
        if(textures.containsKey("particle")){
            var symbol = textures.get("particle");
            return symbol.location == null ? MISSING_TEXTURE : symbol.location;
        }
        return MISSING_TEXTURE;
    }

    @Nullable
    protected JsonModelRaw getParentModel() {
        return parent == null ? null : JsonModelManager.INSTANCE.getRawModel(parent);
    }

    @Nullable
    public JsonModelBaked bake(TextureMap textureMap) throws ModelParseException {
        if(!validCheck()){
            if(!elements.isEmpty())throw new ModelParseException("invalid model: " + location);
            return null;
        }
        else{
            if(elements.isEmpty()) return null;
            var quads = elements.stream()
                .flatMap(element -> element.bake(s -> getTexture(textureMap,s)).stream())
                .collect(Collectors.toSet());
            return new JsonModelBaked(this.location,quads,transformers);
        }
    }

    protected TextureAtlasSprite getTexture(TextureMap textureMap,String symbol){
        var rl = textures.containsKey(symbol) ? textures.get(symbol).location : null;
        return textureMap.getAtlasSprite(rl != null ? rl.toString(): "missingno");
    }

    //会改变自己状态
    protected boolean validCheck() {
        // check loop
        var c = this;
        while (c != null) {
            if (this.equals(c.getParentModel())) {
                return false;
            }
            c = c.getParentModel();
        }
        this.resolveParent();
        // check textures exist
        elements.stream()
            .flatMap(elements -> elements.directions.entrySet().stream())
             .map(entry -> entry.getValue().texture())
            .forEach(t -> {
                if(textures.containsKey(t) && textures.get(t).location == null ) {
                    textures.get(t).location = new ResourceLocation("");
                }
            });
        var missingTextures = elements.stream()
            .flatMap(
                elements -> elements.directions.entrySet().stream())
            .map(
                entry -> entry.getValue().texture())
            .mapToInt(texture -> textures.containsKey(texture) && textures.get(texture).location != null ? 0 : 1)
            .sum();
        // 后面还可能加逻辑
        // noinspection RedundantIfStatement
        //if (missingTextures > 0) return false;
        return true;
    }

    protected void resolveParent() {
        var c = this.getParentModel();
        while (c != null) {
            this.combine(c);
            c = c.getParentModel();
        }
        this.parent = null;
    }

    //不应该修改other
    protected void combine(JsonModelRaw other) {
        if(this.elements.isEmpty()) this.elements.addAll(other.elements);
        for(var transformer : EnumTransformer.values()) {
            if(!this.transformers.containsKey(transformer) && other.transformers.containsKey(transformer)){
                this.transformers.put(transformer,other.transformers.get(transformer));
            }
        }
        this.textures.entrySet().stream()
            .filter(entry -> entry.getValue().location == null && other.textures.containsKey(entry.getKey()) && other.textures.get(entry.getKey()).location != null)
            .forEach(
            entry -> entry.getValue().location = other.textures.get(entry.getKey()).location);
        other.textures.entrySet().stream()
            .filter(entry -> !this.textures.containsKey(entry.getKey()) && entry.getValue().location != null)
            .forEach(entry -> this.textures.put(entry.getKey(),entry.getValue()));

    }

    public Map<String, TextureSymbol> getTextures() {
        return textures;
    }


    public ResourceLocation getParent() {
        return parent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonModelRaw that = (JsonModelRaw) o;
        return Objects.equals(location, that.location) && Objects.equals(parent, that.parent)
            && Objects.equals(textures, that.textures)
            && Objects.equals(elements, that.elements)
            && Objects.equals(overrides, that.overrides)
            && Objects.equals(transformers, that.transformers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, parent, textures, elements, overrides, transformers);
    }
}
