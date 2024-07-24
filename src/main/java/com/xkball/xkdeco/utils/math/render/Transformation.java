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

    public static final Transformation DEFAULT = build(new Vec3f(0,0,0),new Vec3f(1f,1f,1f),new Vec4f());

    public static Transformation build(Vec3f translation, Vec3f scale, Vec3f rotationXYZ){
        var matrix = new Matrix4f();
        matrix.translate(translation.toLWJGLVector3f());
        matrix.rotate((float) Math.toRadians(rotationXYZ.z()), Axis.Z.vecPositive.toLWJGLVector3f());
        matrix.rotate((float) Math.toRadians(rotationXYZ.x()), Axis.X.vecPositive.toLWJGLVector3f());
        matrix.rotate((float) Math.toRadians(rotationXYZ.y()), Axis.Y.vecPositive.toLWJGLVector3f());
        matrix.translate(new Vector3f(-translation.x(),-translation.y(),-translation.z()));
        matrix.scale(scale.toLWJGLVector3f());
        return new Transformation(matrix);
    }

    public static Transformation build(Vec3f translation, Vec3f scale, Vec4f leftRotation) {
        var matrix = new Matrix4f();
        matrix.translate(translation.toLWJGLVector3f());
        matrix.rotate(leftRotation.w(), leftRotation.getPos().toLWJGLVector3f());
        matrix.translate(new Vector3f(-translation.x(),-translation.y(),-translation.z()));
        matrix.scale(scale.toLWJGLVector3f());
        return new Transformation(matrix);
    }

    public void applyTo(Quad quad) {
        quad.v1().applyTransformation(this);
        quad.v2().applyTransformation(this);
        quad.v3().applyTransformation(this);
        quad.v4().applyTransformation(this);
    }
}
