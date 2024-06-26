package com.xkball.xkdeco.utils.math.render;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.xkdeco.utils.exception.ModelParseException;

@Desugar
public record Face(String texture, Vec4f uv, int rotation, int tintindex, Direction cullface) {

    public static Face parseFromJson(JsonObject json) throws ModelParseException {
        var texture = json.get("texture")
            .getAsString();
        assert texture.startsWith("#");
        texture = texture.substring(1);

        var uv = Vec4f.readFromJsonArray(
            json.get("uv")
                .getAsJsonArray());

        var rotation = json.has("rotation") ? json.get("rotation")
            .getAsInt() : 0;
        assert rotation % 90 == 0;
        rotation = rotation / 90;

        var tintindex = json.has("tintindex") ? json.get("tintindex")
            .getAsInt() : -1;
        var cullface = json.has("cullface") ? Direction.readFromString(
            json.get("cullface")
                .getAsString())
            : Direction.NONE;
        return new Face(texture, uv, rotation, tintindex, cullface);
    }
}
