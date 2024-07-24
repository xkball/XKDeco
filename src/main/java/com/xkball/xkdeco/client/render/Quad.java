package com.xkball.xkdeco.client.render;

import com.github.bsideup.jabel.Desugar;
import com.xkball.xkdeco.utils.math.render.EnumDirection;
import com.xkball.xkdeco.utils.math.render.Vertex;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import javax.annotation.Nullable;

@Desugar
@SideOnly(Side.CLIENT)
public record Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, TextureAtlasSprite texture,@Nullable EnumDirection cullface,@Nullable EnumDirection direction) {

    public Quad(Quad quad){
        this(new Vertex(quad.v1),new Vertex(quad.v2),new Vertex(quad.v3),new Vertex(quad.v4),quad.texture,quad.cullface,quad.direction);
    }

    @SuppressWarnings("DuplicatedCode")
    public static Quad face(Vertex start, Vertex end, EnumDirection direction, TextureAtlasSprite texture,@Nullable EnumDirection cullface) {
        //此时的uv还不是真正的uv
        return switch (direction){
            case DOWN, UP -> new Quad(start, new Vertex(start.x, start.y, end.z, start.u, end.v),
                end,new Vertex(end.x,start.y,start.z,end.u,start.v),texture,cullface,direction);
            case NORTH, SOUTH -> new Quad(start,new Vertex(start.x, end.y, start.z, start.u, end.v),
                end,new Vertex(end.x,start.y,start.z,end.u,start.v),texture,cullface,direction);
            case EAST, WEST -> new Quad(start,new Vertex(start.x,end.y,start.z,start.u,end.v),
                end,new Vertex(start.x, start.y, end.z, end.u, start.v),texture,cullface,direction);
        };
    }

    public void reUV(){
        var du = texture.getMaxU() - texture.getMinU();
        var dv = texture.getMaxV() - texture.getMinV();
        reVU(v1,du,dv,texture.getMinU(),texture.getMinV());
        reVU(v2,du,dv,texture.getMinU(),texture.getMinV());
        reVU(v3,du,dv,texture.getMinU(),texture.getMinV());
        reVU(v4,du,dv,texture.getMinU(),texture.getMinV());
    }

    private void reVU(Vertex vertex, float du, float dv, float minU, float minV){
        vertex.u = minU + vertex.u*du/16;
        vertex.v = minV + vertex.v*dv/16;
    }

    @SideOnly(Side.CLIENT)
    public void applyTessellator(Tessellator tessellator){
        tessellator.addVertexWithUV(this.v1().x,this.v1().y,this.v1().z,this.v1().u,this.v1().v);
        tessellator.addVertexWithUV(this.v2().x,this.v2().y,this.v2().z,this.v2().u,this.v2().v);
        tessellator.addVertexWithUV(this.v3().x,this.v3().y,this.v3().z,this.v3().u,this.v3().v);
        tessellator.addVertexWithUV(this.v4().x,this.v4().y,this.v4().z,this.v4().u,this.v4().v);
    }

}
