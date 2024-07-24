package com.xkball.xkdeco.client.render;

import com.xkball.xkdeco.utils.math.render.EnumDirection;
import com.xkball.xkdeco.utils.math.render.Transformation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class QuadList {

    private final EnumMap<EnumDirection,List<Quad>> cullfaceQuadsByDirection = new EnumMap<>(EnumDirection.class);
    private final EnumMap<EnumDirection,List<Quad>> quadsByDirection = new EnumMap<>(EnumDirection.class);
    private final List<Quad> nonDirectionQuads = new ArrayList<>(1);

    public QuadList(Collection<Quad> quads) {
        for(var direction : EnumDirection.values()) {
            cullfaceQuadsByDirection.put(direction, new ArrayList<>());
            quadsByDirection.put(direction, new ArrayList<>());
        }
        for(var quad : quads) {
            quadsByDirection.get(quad.direction()).add(new Quad(quad));
           if(quad.cullface() != null) cullfaceQuadsByDirection.get(quad.cullface()).add(quad);
           else nonDirectionQuads.add(quad);
        }
    }

    public QuadList(QuadList quadList) {
        for(var direction : EnumDirection.values()) {
            cullfaceQuadsByDirection.put(direction, new ArrayList<>());
            quadsByDirection.put(direction, new ArrayList<>());
        }
        for(var entry : quadList.cullfaceQuadsByDirection.entrySet()) {
            for(var quad : entry.getValue()) {
                cullfaceQuadsByDirection.get(quad.cullface()).add(new Quad(quad));
            }
        }
        for(var quad : quadList.quadsByDirection.entrySet()) {
            for(var quad1 : quad.getValue()) {
                quadsByDirection.get(quad1.direction()).add(new Quad(quad1));
            }
        }
        for(var quad : quadList.nonDirectionQuads) {
            nonDirectionQuads.add(new Quad(quad));
        }
    }

    public QuadList applyTransformation(Transformation transformation) {
        for(var dir : EnumDirection.values()) {
            for(var quad : cullfaceQuadsByDirection.get(dir)) {
                transformation.applyTo(quad);
            }
            for(var quad : quadsByDirection.get(dir)) {
                transformation.applyTo(quad);
            }
        }
        for(var quad : nonDirectionQuads) {
            transformation.applyTo(quad);
        }
        return this;
    }

    public void applyTessellator(Tessellator tessellator,EnumDirection cullfaceDirection){
        if(cullfaceDirection == null){
            for(var dir : EnumDirection.values()) {
                for(var quad : cullfaceQuadsByDirection.get(dir)) {
                    quad.applyTessellator(tessellator);
                }
            }
            for(var quad : nonDirectionQuads) {
                quad.applyTessellator(tessellator);
            }
        }
        else {
            for(var quad : cullfaceQuadsByDirection.get(cullfaceDirection)) {
                quad.applyTessellator(tessellator);
            }
        }
    }

    public void applyTessellatorNoCullface(Tessellator tessellator,EnumDirection direction) {
        for(var quad : quadsByDirection.get(direction)) {
            quad.applyTessellator(tessellator);
        }
    }

    public List<Quad> getNonDirectionQuads() {
        return nonDirectionQuads;
    }

    public EnumMap<EnumDirection, List<Quad>> getCullfaceQuadsByDirection() {
        return cullfaceQuadsByDirection;
    }

    public EnumMap<EnumDirection, List<Quad>> getQuadsByDirection() {
        return quadsByDirection;
    }


}
