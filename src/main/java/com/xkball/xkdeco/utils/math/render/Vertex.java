package com.xkball.xkdeco.utils.math.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class Vertex {
    public float x;
    public float y;
    public float z;
    public float u;
    public float v;

    public Vertex(float x, float y, float z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public Vertex(Vec3f vec3f, float u, float v){
        this(vec3f.x(),vec3f.y(),vec3f.z(),u,v);
    }

    @SideOnly(Side.CLIENT)
    public void apply(Transformation transformation){
        var vec4 = new Vec4f(x, y, z, 1.0f);
        vec4 = vec4.mul(transformation.matrix());
        vec4 = vec4.div(vec4.w());
        this.x = vec4.x();
        this.y = vec4.y();
        this.z = vec4.z();
    }
}
