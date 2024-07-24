package com.xkball.xkdeco.client.model.json;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.exception.ModelParseException;
import com.xkball.xkdeco.utils.math.render.EnumDirection;
import com.xkball.xkdeco.utils.math.render.Vec4f;

import javax.annotation.Nullable;

@Desugar
public record DataFace(String texture, Vec4f uv, int rotation, int tintindex, @Nullable EnumDirection cullface,@Nullable EnumDirection direction) {

    public static DataFace parseFromJson(JsonObject json,EnumDirection direction) throws ModelParseException {
        var texture = json.get("texture").getAsString();
        assert texture.startsWith("#");
        texture = texture.substring(1);

        var uv = json.has("uv") ? Vec4f.readFromJsonArray(json.get("uv").getAsJsonArray()) : new Vec4f(0,0,1,1);

        var rotation = json.has("rotation") ? json.get("rotation").getAsInt() : 0;
        assert rotation % 90 == 0;
        rotation = rotation / 90;

        var tintindex = json.has("tintindex") ? json.get("tintindex").getAsInt() : -1;
        var cullface = json.has("cullface") ? EnumDirection.readFromString(json.get("cullface").getAsString()) : null;
        return new DataFace(texture, uv, rotation, tintindex, cullface,direction);
    }
}
