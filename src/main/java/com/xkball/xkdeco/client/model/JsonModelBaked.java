package com.xkball.xkdeco.client.model;

import com.xkball.xkdeco.client.EnumTransformer;
import com.xkball.xkdeco.client.model.json.DataTransformer;
import com.xkball.xkdeco.client.render.Quad;
import com.xkball.xkdeco.client.render.QuadList;
import com.xkball.xkdeco.utils.math.render.EnumDirection;
import com.xkball.xkdeco.utils.math.render.Transformation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;

@SideOnly(Side.CLIENT)
public class JsonModelBaked {

    private final ResourceLocation location;
    private final EnumMap<EnumTransformer,QuadList> quadMap = new EnumMap<>(EnumTransformer.class);

    public JsonModelBaked(ResourceLocation location,Collection<Quad> quads,EnumMap<EnumTransformer, DataTransformer> transformers) {
        this.location = location;
        var nonTransList = new QuadList(quads);
        quadMap.put(EnumTransformer.NONE,nonTransList);
        for(var transType : EnumTransformer.values()) {
            if(transType == EnumTransformer.NONE) continue;
            var transData = transformers.get(transType);
            if(transData == null){
                quadMap.put(transType,nonTransList);
                continue;
            }
            var trans =  transData.toTransformation();
            if(Transformation.DEFAULT.equals(trans)) quadMap.put(transType,nonTransList);
            else quadMap.put(transType,new QuadList(nonTransList).applyTransformation(trans));
        }
    }

    public void applyToTessellator(Tessellator tessellator, EnumTransformer transformer, @Nullable EnumDirection cullfaceDirection) {
        quadMap.get(transformer).applyTessellator(tessellator,cullfaceDirection);
    }

    public void applyToTessellatorNoCullface(Tessellator tessellator, EnumTransformer transformer, EnumDirection direction) {
        quadMap.get(transformer).applyTessellatorNoCullface(tessellator,direction);
    }

    public QuadList getQuadList(EnumTransformer transformer) {
        return quadMap.get(transformer);
    }

    public ResourceLocation getLocation() {
        return location;
    }

}
