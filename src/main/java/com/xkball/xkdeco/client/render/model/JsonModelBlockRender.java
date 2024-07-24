package com.xkball.xkdeco.client.render.model;

import com.xkball.xkdeco.api.client.block.IJsonModelBlock;
import com.xkball.xkdeco.client.EnumTransformer;
import com.xkball.xkdeco.utils.ClientUtils;
import com.xkball.xkdeco.utils.math.render.EnumDirection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import org.lwjgl.opengl.GL11;

public class JsonModelBlockRender implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation Globe = new ResourceLocation("xkdeco", "block/furniture/globe");

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        var mapper = IJsonModelBlock.cast(block).xkdeco$getBlockJsonModelMapper();
        if(mapper == null) return;
        //todo 暂时不能获取meta
        var model = mapper.getModel(0);
        if(model == null) return;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.7F, -0.5F, -0F);
        GL11.glScalef(1f/0.625f, 1/0.625f, 1/0.625f);
        var tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        ClientUtils.useDefaultColor();
        for(var dir : EnumDirection.values()){
            var normal = dir.toVec3f();
            tessellator.setNormal(normal.x(), normal.y(), normal.z());
            model.applyToTessellatorNoCullface(tessellator, EnumTransformer.GUI,dir);
        }
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        var mapper = IJsonModelBlock.cast(block).xkdeco$getBlockJsonModelMapper();
        if(mapper == null) return false;
        var model = mapper.getModel(world.getBlockMetadata(x,y,z));
        if (model == null) return false;
        var tessellator = Tessellator.instance;
        int color = block.colorMultiplier(world, x, y, z);
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        boolean rendered = false;
        //todo 光照(包括从模型改变光照)
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y+1, z));
        tessellator.setColorOpaque_F(red, green, blue);
        var x_ = x%16;
        tessellator.addTranslation(x,y,z);
        for (var i = 0; i < 6; i++) {
            if (block.shouldSideBeRendered(world, x+EnumFacing.values()[i].getFrontOffsetX(), y+EnumFacing.values()[i].getFrontOffsetY(), z+EnumFacing.values()[i].getFrontOffsetZ(), i)) {
                if (!rendered) {
                    rendered = true;
                }
                model.applyToTessellator(tessellator, EnumTransformer.NONE, EnumDirection.values()[i]);
            }
        }
        var nDQuads = model.getQuadList(EnumTransformer.NONE).getNonDirectionQuads();
        if (!nDQuads.isEmpty()) {
            rendered = true;
            for(var quad : nDQuads) {
                quad.applyTessellator(tessellator);
            }
        }
        tessellator.addTranslation(-x,-y,-z);
        return rendered;
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
