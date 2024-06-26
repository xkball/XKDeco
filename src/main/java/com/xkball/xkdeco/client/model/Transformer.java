package com.xkball.xkdeco.client.model;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.math.render.Vec3f;

@Desugar
public record Transformer(Vec3f scale, Vec3f rotation, Vec3f translation) {

    public static Transformer parseFromJson(JsonObject json) {
        var scale = json.has("scale") ? Vec3f.readFromJsonArray(
            json.get("scale")
                .getAsJsonArray()) : new Vec3f(1,1,1);
        var rawRotation = json.has("rotation") ? Vec3f.readFromJsonArray(
            json.get("rotation")
                .getAsJsonArray()) : new Vec3f(0,0,0);
        var rawTranslation = json.has("translation") ? Vec3f.readFromJsonArray(
            json.get("translation")
                .getAsJsonArray()) : new Vec3f(0,0,0);
        return new Transformer(scale, rawRotation, rawTranslation);
    }
}
