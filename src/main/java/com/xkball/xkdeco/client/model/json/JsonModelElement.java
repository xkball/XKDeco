package com.xkball.xkdeco.client.model.json;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.client.render.Quad;


import com.xkball.xkdeco.utils.math.render.EnumDirection;
import com.xkball.xkdeco.utils.math.render.Vec3f;
import com.xkball.xkdeco.utils.math.render.Vertex;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.exception.ModelParseException;

import javax.annotation.Nonnull;

public class JsonModelElement {

    public final Vec3f from;
    public final Vec3f to;
    public final DataRotation rotation;
    public final EnumMap<EnumDirection, DataFace> directions = new EnumMap<>(EnumDirection.class);

    public JsonModelElement(JsonObject elementSrc, ResourceLocation location) {
        this.from = Vec3f.readFromJsonArray(elementSrc.get("from").getAsJsonArray());
        this.to = Vec3f.readFromJsonArray(elementSrc.get("to").getAsJsonArray());
        this.rotation = DataRotation.readOptional(elementSrc);
        if (elementSrc.has("shade")) {
            XKDeco.LOG.warn("not support shade yet on model:{}", location);
        }
        var faceElement = elementSrc.get("faces")
            .getAsJsonObject();
        for (var entry : faceElement.entrySet()) {
            var direction = EnumDirection.readFromString(entry.getKey());
            if (direction == null)
                throw new ModelParseException("cannot read cullface: " + entry.getKey() + "on model:" + location);
            var face = DataFace.parseFromJson(entry.getValue().getAsJsonObject(),direction);
            directions.put(direction , face);
        }
    }

    public List<Quad> bake(Function<String, TextureAtlasSprite> textureGetter){
        List<Quad> quads = new ArrayList<>(6);
        var transformation = rotation.toTransformation();
        for(var entry : directions.entrySet()) {
            var face = entry.getValue();
            var texture = Objects.requireNonNull(textureGetter.apply(face.texture()));
            var u1 = face.uv().at(face.rotation());
            var v1 = face.uv().at((face.rotation()+1)%4);
            var u2 = face.uv().at((face.rotation()+2)%4);
            var v2 = face.uv().at((face.rotation()+3)%4);
            var start = new Vertex(startVertexOnFace(entry.getKey()),u1,v1);
            var end = new Vertex(endVertexOnFace(entry.getKey()),u2,v2);
            var quad = Quad.face(start,end,entry.getKey(),texture,face.cullface());
            transformation.applyTo(quad);
            quad.reUV();
            quads.add(quad);
        }
        return quads;
    }

    //你检查重复代码是真不看地方啊
    @SuppressWarnings("DuplicatedCode")
    protected Vec3f startVertexOnFace(@Nonnull EnumDirection direction) {
        return switch (direction) {
            case DOWN -> new Vec3f(from.x(),from.y(),to.z(), 1/16f);
            case UP, WEST -> new Vec3f(from.x(),to.y(),from.z(), 1/16f);
            case NORTH -> new Vec3f(to.x(),to.y(),from.z(), 1/16f);
            case SOUTH -> new Vec3f(from.x(),to.y(),to.z(), 1/16f);
            case EAST -> new Vec3f(to.x(),to.y(),to.z(), 1/16f);
        };
    }

    @SuppressWarnings("DuplicatedCode")
    protected Vec3f endVertexOnFace(@Nonnull EnumDirection direction) {
        return switch (direction){
            case DOWN, EAST -> new Vec3f(to.x(),from.y(),from.z(), 1/16f);
            case UP -> new Vec3f(to.x(),to.y(),to.z(), 1/16f);
            case NORTH -> new Vec3f(from.x(),from.y(),from.z(), 1/16f);
            case SOUTH -> new Vec3f(to.x(),from.y(),to.z(), 1/16f);
            case WEST -> new Vec3f(from.x(),from.y(),to.z(), 1/16f);
        };
    }
}
