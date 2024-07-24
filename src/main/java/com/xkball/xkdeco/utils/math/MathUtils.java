package com.xkball.xkdeco.utils.math;

public class MathUtils {
    public static float fma(float a, float b,float c) {
        return a * b + c;
    }

    public static float clamp(float a,float max,float min) {
        return a > max ? max : Math.max(a, min);
    }

    public static int sign(int a){
        return a < 0 ? -1 : 1;
    }
}
