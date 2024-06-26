package com.xkball.xkdeco.utils.math.render;

public enum Axis {

    X(new Vec3f(1, 0, 0), new Vec3f(-1, 0, 0)),
    Y(new Vec3f(0, 1, 0), new Vec3f(0, -1, 0)),
    Z(new Vec3f(0, 0, 1), new Vec3f(0, 0, -1)),;

    public final Vec3f vecPositive;
    public final Vec3f vecNegative;

    Axis(Vec3f vecPositive, Vec3f vecNegative) {
        this.vecPositive = vecPositive;
        this.vecNegative = vecNegative;
    }
}
