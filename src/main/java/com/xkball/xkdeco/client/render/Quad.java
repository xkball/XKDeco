package com.xkball.xkdeco.client.render;

import com.github.bsideup.jabel.Desugar;
import com.xkball.xkdeco.utils.math.render.Direction;
import com.xkball.xkdeco.utils.math.render.Vertex;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Desugar
public record Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, TextureAtlasSprite texture) {
    @SuppressWarnings("DuplicatedCode")
    public static Quad face(Vertex start, Vertex end, Direction direction, TextureAtlasSprite texture) {
        //此时的uv还不是真正的uv
        return switch (direction){
            case DOWN, UP -> new Quad(start, new Vertex(start.x, start.y, end.z, start.u, end.v),
                end,new Vertex(end.x,start.y,start.z,end.u,start.v),texture);
            case NORTH, SOUTH -> new Quad(start,new Vertex(start.x, end.y, start.z, start.u, end.v),
                end,new Vertex(end.x,start.y,start.z,end.u,start.v),texture);
            case EAST, WEST -> new Quad(start,new Vertex(start.x,end.y,start.z,start.u,end.v),
                end,new Vertex(start.x, start.y, end.z, end.u, start.v),texture);
            default ->  throw new IllegalArgumentException("can not build face without direction");
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

}
