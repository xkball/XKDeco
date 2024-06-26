package com.xkball.xkdeco.utils.math.render;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonArray;
import com.xkball.xkdeco.utils.math.MathUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Matrix4f;

@Desugar
public record Vec4f(float x, float y, float z, float w) {

    public Vec4f(){
        this(0,0,0,0);
    }

    public static Vec4f readFromJsonArray(JsonArray array) {
        assert array.size() == 6;
        return new Vec4f(
            array.get(0)
                .getAsFloat(),
            array.get(1)
                .getAsFloat(),
            array.get(2)
                .getAsFloat(),
            array.get(3)
                .getAsFloat());
    }

    public Vec4f(Vec3f pos, float w) {
        this(pos.x(), pos.y(), pos.z(), w);
    }

    public Vec3f getPos() {
        return new Vec3f(x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public Vec4f mul(Matrix4f mat){
        float x = this.x, y = this.y, z = this.z, w = this.w;
        var dest_x = MathUtils.fma(mat.m00, x, MathUtils.fma(mat.m10, y, MathUtils.fma(mat.m20, z, mat.m30 * w)));
        var dest_y = MathUtils.fma(mat.m01, x, MathUtils.fma(mat.m11, y, MathUtils.fma(mat.m21, z, mat.m31 * w)));
        var dest_z = MathUtils.fma(mat.m02, x, MathUtils.fma(mat.m12, y, MathUtils.fma(mat.m22, z, mat.m32 * w)));
        var dest_w = MathUtils.fma(mat.m03, x, MathUtils.fma(mat.m13, y, MathUtils.fma(mat.m23, z, mat.m33 * w)));
        return new Vec4f(dest_x, dest_y, dest_z, dest_w);
    }

    public Vec4f div(float scalar) {
        float inv = 1.0f / scalar;
        var x_ = x * inv;
        var y_ = y * inv;
        var z_ = z * inv;
        var w_ = w * inv;
        return new Vec4f(x_, y_, z_, w_);
    }

    public float at(int index){
        assert index >= 0 && index < 4;
        return switch (index){
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            case 3 -> w;
            default -> throw new IllegalArgumentException("index out of bounds: " + index);
        };
    }
}
