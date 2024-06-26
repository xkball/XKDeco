package com.xkball.xkdeco.utils.math.render;

import org.lwjgl.util.vector.Matrix4f;

import com.github.bsideup.jabel.Desugar;
import com.xkball.xkdeco.client.render.Quad;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

@Desugar
@SideOnly(Side.CLIENT)
public record Transformation(Matrix4f matrix) {

    public static Transformation build(Vec3f translation, Vec3f scale, Vec4f leftRotation) {
        var matrix = new Matrix4f();
        matrix.translate(translation.toLWJGLVector3f());
        //matrix.translate(new Vector3f(0,1,0));
        //matrix.translate(new Vector3f(dx/32f, dy/32f, dz/32f));
        matrix.rotate(
            leftRotation.w(),
            leftRotation.getPos()
                .toLWJGLVector3f());
        matrix.translate(new Vector3f(-translation.x(),-translation.y(),-translation.z()));
        //matrix.translate(new Vector3f(-dx/32f, -dy/32f, -dz/32f));
        matrix.scale(scale.toLWJGLVector3f());
        return new Transformation(matrix);
    }

    public void apply(Quad quad) {
        quad.v1().apply(this);
        quad.v2().apply(this);
        quad.v3().apply(this);
        quad.v4().apply(this);
    }
}
