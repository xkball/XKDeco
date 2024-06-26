package com.xkball.xkdeco.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ThirdPersonRender {

    // 用mixin只有改一句 减少对mc原版逻辑的破坏
    // @SubscribeEvent
    // public void onRenderEquipPre(RenderPlayerEvent.Specials.Pre event) {
    // event.renderItem = false;
    // var render = event.renderer;
    // var player = event.entityPlayer;
    // GL11.glPushMatrix();
    // //render.modelBipedMain.bipedRightArm.postRender(-0.0625F);
    // render.modelBipedMain.bipedLeftArm.postRender(0.0625F);
    // GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
    // var itemstack = player.inventory.getCurrentItem();
    // if (player.fishEntity != null)
    // {
    // itemstack = new ItemStack(Items.stick);
    // }
    //
    // EnumAction enumaction = null;
    //
    // if (player.getItemInUseCount() > 0)
    // {
    // enumaction = itemstack.getItemUseAction();
    // }
    //
    // IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack,
    // IItemRenderer.ItemRenderType.EQUIPPED);
    // boolean is3D = (customRenderer != null &&
    // customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack,
    // IItemRenderer.ItemRendererHelper.BLOCK_3D));
    // var f2 = 0f;
    // var f4 = 0f;
    // if (is3D || itemstack.getItem() instanceof ItemBlock &&
    // RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
    // {
    // f2 = 0.5F;
    // GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
    // f2 *= 0.75F;
    // GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
    // GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    // GL11.glScalef(-f2, -f2, f2);
    // }
    // else if (itemstack.getItem() == Items.bow)
    // {
    // f2 = 0.625F;
    // GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
    // GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
    // GL11.glScalef(f2, -f2, f2);
    // GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
    // GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    // }
    // else if (itemstack.getItem().isFull3D())
    // {
    // f2 = 0.625F;
    //
    // if (itemstack.getItem().shouldRotateAroundWhenRendering())
    // {
    // GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
    // GL11.glTranslatef(0.0F, -0.125F, 0.0F);
    // }
    //
    // if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
    // {
    // GL11.glTranslatef(0.05F, 0.0F, -0.1F);
    // GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
    // GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
    // GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
    // }
    //
    // GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
    // GL11.glScalef(f2, -f2, f2);
    // GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
    // GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    // }
    // else
    // {
    // f2 = 0.375F;
    // GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
    // GL11.glScalef(f2, f2, f2);
    // GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
    // GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
    // GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
    // }
    //
    // float f3;
    // int k;
    // float f12;
    //
    // if (itemstack.getItem().requiresMultipleRenderPasses())
    // {
    // for (k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++k)
    // {
    // int i = itemstack.getItem().getColorFromItemStack(itemstack, k);
    // f12 = (float)(i >> 16 & 255) / 255.0F;
    // f3 = (float)(i >> 8 & 255) / 255.0F;
    // f4 = (float)(i & 255) / 255.0F;
    // GL11.glColor4f(f12, f3, f4, 1.0F);
    // RenderManager.instance.itemRenderer.renderItem(player, itemstack, k);
    // }
    // }
    // else
    // {
    // k = itemstack.getItem().getColorFromItemStack(itemstack, 0);
    // float f11 = (float)(k >> 16 & 255) / 255.0F;
    // f12 = (float)(k >> 8 & 255) / 255.0F;
    // f3 = (float)(k & 255) / 255.0F;
    // GL11.glColor4f(f11, f12, f3, 1.0F);
    // RenderManager.instance.itemRenderer.renderItem(player, itemstack, 0);
    // }
    //
    // GL11.glPopMatrix();
    //
    // }
}
