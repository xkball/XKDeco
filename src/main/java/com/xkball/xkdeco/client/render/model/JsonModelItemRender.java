package com.xkball.xkdeco.client.render.model;

import com.xkball.xkdeco.client.EnumTransformer;
import com.xkball.xkdeco.client.JsonModelManager;
import com.xkball.xkdeco.client.ItemModelModifiedManager;
import com.xkball.xkdeco.utils.ClientUtils;
import com.xkball.xkdeco.utils.math.render.EnumDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class JsonModelItemRender implements IItemRenderer {

    private EnumTransformer transformerContext = EnumTransformer.NONE;
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return ItemModelModifiedManager.INSTANCE.getItemMapper(item.getItem()).mapModel(item) != null;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        switch (type) {
            case ENTITY: {
                transformerContext = EnumTransformer.GROUND;
                break;
            }
            case EQUIPPED: {
                transformerContext = EnumTransformer.THIRD_PERSON_RIGHT_HAND;
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                transformerContext = EnumTransformer.FIRST_PERSON_RIGHT_HAND;
                break;
            }
            case INVENTORY: {
                transformerContext = EnumTransformer.GUI;
            }
        }
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        var model = JsonModelManager.INSTANCE.getBakedModel(
            ItemModelModifiedManager.INSTANCE.getItemMapper(item.getItem()).mapModel(item));
        if(model == null) return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
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
            model.applyToTessellatorNoCullface(tessellator, transformerContext,dir);
        }
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        transformerContext = EnumTransformer.NONE;
    }
}
