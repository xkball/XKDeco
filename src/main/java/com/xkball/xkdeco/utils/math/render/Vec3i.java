package com.xkball.xkdeco.utils.math.render;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonArray;

@Desugar
public record Vec3i(int x, int y, int z) {

    public static Vec3i readFromJsonArray(JsonArray array) {
        assert array.size() == 3;
        return new Vec3i(
            array.get(0)
                .getAsInt(),
            array.get(1)
                .getAsInt(),
            array.get(2)
                .getAsInt());
    }
}
