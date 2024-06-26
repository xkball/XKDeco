package com.xkball.xkdeco.utils.math.render;

import org.lwjgl.util.vector.Vector3f;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonArray;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Desugar
public record Vec3f(float x, float y, float z) {
    public Vec3f(float x, float y, float z,float scale) {
        this(x*scale, y*scale, z*scale);
    }
    public static Vec3f readFromJsonArray(JsonArray array) {
        assert array.size() == 3;
        return new Vec3f(
            array.get(0)
                .getAsFloat(),
            array.get(1)
                .getAsFloat(),
            array.get(2)
                .getAsFloat());
    }

    @SideOnly(Side.CLIENT)
    public Vector3f toLWJGLVector3f() {
        return new Vector3f(x, y, z);
    }
}
