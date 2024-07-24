package com.xkball.xkdeco.client.model.json;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.math.MathUtils;
import com.xkball.xkdeco.utils.math.render.Transformation;
import com.xkball.xkdeco.utils.math.render.Vec3f;

@Desugar
public record DataTransformer(Vec3f scale, Vec3f rotation, Vec3f translation) {

    public static DataTransformer parseFromJson(JsonObject json) {
        var scale = json.has("scale") ? Vec3f.readFromJsonArray(
            json.get("scale").getAsJsonArray()) : new Vec3f(1,1,1);
        var rawRotation = json.has("rotation") ? Vec3f.readFromJsonArray(
            json.get("rotation").getAsJsonArray()) : new Vec3f(0,0,0);
        var rawTranslation = json.has("translation") ? Vec3f.readFromJsonArray(
            json.get("translation").getAsJsonArray()) : new Vec3f(0,0,0);
        var trans = new Vec3f(MathUtils.clamp(rawTranslation.x(),80,-80)/16f,MathUtils.clamp(rawTranslation.y(),80,-80)/16f,MathUtils.clamp(rawTranslation.z(),80,-80)/16f);
        return new DataTransformer(scale, rawRotation,trans);
    }

    public Transformation toTransformation() {
        return Transformation.build(translation,scale,rotation);
    }
}
