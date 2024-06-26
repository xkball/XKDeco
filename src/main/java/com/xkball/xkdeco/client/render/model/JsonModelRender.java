package com.xkball.xkdeco.client.render.model;

import com.xkball.xkdeco.client.JsonModelManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import org.lwjgl.opengl.GL11;

public class JsonModelRender implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation Globe = new ResourceLocation("xkdeco", "models/block/furniture/tech_screen.json");

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        var color = 16777215;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        tessellator.setColorOpaque_F(red, green, blue);
        //tessellator.setBrightness(15);
        //tessellator.setColorOpaque_F(1f, 1f,1f);
        var model = JsonModelManager.INSTANCE.getBakedModel(Globe);
        if(model != null) {
            for (var quad : model.quads()){
                tessellator.addVertexWithUV(quad.v1().x,quad.v1().y,quad.v1().z,quad.v1().u,quad.v1().v);
                tessellator.addVertexWithUV(quad.v2().x,quad.v2().y,quad.v2().z,quad.v2().u,quad.v2().v);
                tessellator.addVertexWithUV(quad.v3().x,quad.v3().y,quad.v3().z,quad.v3().u,quad.v3().v);
                tessellator.addVertexWithUV(quad.v4().x,quad.v4().y,quad.v4().z,quad.v4().u,quad.v4().v);
            }
        }
        tessellator.draw();
        GL11.glPopMatrix();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        var tessellator = Tessellator.instance;
        int color = block.colorMultiplier(world, x, y, z);
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y+1, z));
        tessellator.setColorOpaque_F(red, green, blue);
        //GL11.glPushMatrix();
        //GL11.glEnable(GL11.GL_BLEND);
        var model = JsonModelManager.INSTANCE.getBakedModel(Globe);
        if(model != null) {
            for (var quad : model.quads()){
                tessellator.addVertexWithUV(quad.v1().x+x,quad.v1().y+y,quad.v1().z+z,quad.v1().u,quad.v1().v);
                tessellator.addVertexWithUV(quad.v2().x+x,quad.v2().y+y,quad.v2().z+z,quad.v2().u,quad.v2().v);
                tessellator.addVertexWithUV(quad.v3().x+x,quad.v3().y+y,quad.v3().z+z,quad.v3().u,quad.v3().v);
                tessellator.addVertexWithUV(quad.v4().x+x,quad.v4().y+y,quad.v4().z+z,quad.v4().u,quad.v4().v);
            }
        }
        //GL11.glPopMatrix();
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 114514;
    }
}
