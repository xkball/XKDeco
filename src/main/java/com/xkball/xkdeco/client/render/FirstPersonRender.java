package com.xkball.xkdeco.client.render;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.mixins.mixin.client.MixinMinecraftAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class FirstPersonRender {

    public static boolean ITEM_IN_HAND_RENDERED = false;

    // 返回null阻断原版渲染右手物品
    @SuppressWarnings("SameReturnValue")
    public static Object renderFirstPerson(ItemStack stack, float equipProgress) {
        ITEM_IN_HAND_RENDERED = false;
        if (!IExtendedPlayer.get(Minecraft.getMinecraft().thePlayer)
            .isLeftHandSide()) return stack == null ? null : new Object();
        if (stack == null || stack.getItem() == null
            || Minecraft.getMinecraft() == null
            || Minecraft.getMinecraft().thePlayer == null) {
            return null;
        }
        ITEM_IN_HAND_RENDERED = true;
        GL11.glPushMatrix();
        var mc = Minecraft.getMinecraft();
        var partialTick = ((MixinMinecraftAccess) mc).getTimer().renderPartialTicks;
        var entityclientplayermp = Minecraft.getMinecraft().thePlayer;
        var f13 = 0.8f;
        equipProgress = 1 - equipProgress;
        if (entityclientplayermp.getItemInUseCount() > 0) {
            EnumAction enumaction = stack.getItemUseAction();
            if (enumaction == EnumAction.eat || enumaction == EnumAction.drink) {
                transformEatAndDrinkLeft(entityclientplayermp, stack, partialTick);
                transformSideFirstPersonLeft(equipProgress);
            }
            if (enumaction == EnumAction.bow) {
                transformSideFirstPersonLeft(equipProgress);
                transformBowLeft(stack, entityclientplayermp.getItemInUseCount(), partialTick);
            } else {
                transformSideFirstPersonLeft(equipProgress);
            }
        } else {
            var swingProgress = entityclientplayermp.getSwingProgress(partialTick);
            float f = -0.4F * MathHelper.sin(sqrt(swingProgress) * (float) Math.PI);
            float f1 = 0.2F * MathHelper.sin(sqrt(swingProgress) * ((float) Math.PI * 2F));
            float f2 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
            GL11.glTranslatef(-f, f1, f2);
            transformSideFirstPersonLeft(equipProgress);
            transformFirstPersonLeft(swingProgress);
        }
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.4f, 0.4f, 0.4f);
        if (entityclientplayermp.getItemInUseCount() > 0) {
            EnumAction enumaction = stack.getItemUseAction();
            if (enumaction == EnumAction.block) {
                GL11.glTranslatef(-0.2F, 0.2F, 0.5F);
                GL11.glRotatef(-120.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(30f, 0.0F, 1.0F, 0.0F);
            }
        }
        if (stack.getItem()
            .shouldRotateAroundWhenRendering()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
        var itemRenderer = RenderManager.instance.itemRenderer;
        if (stack.getItem()
            .requiresMultipleRenderPasses()) {
            itemRenderer.renderItem(entityclientplayermp, stack, 0, EQUIPPED_FIRST_PERSON);
            for (int x = 1; x < stack.getItem()
                .getRenderPasses(stack.getItemDamage()); x++) {
                int k1 = stack.getItem()
                    .getColorFromItemStack(stack, x);
                var f10 = (float) (k1 >> 16 & 255) / 255.0F;
                var f11 = (float) (k1 >> 8 & 255) / 255.0F;
                var f12 = (float) (k1 & 255) / 255.0F;
                GL11.glColor4f(f10, f11, f12, 1.0F);
                itemRenderer.renderItem(entityclientplayermp, stack, x, EQUIPPED_FIRST_PERSON);
            }
        } else {
            itemRenderer.renderItem(entityclientplayermp, stack, 0, EQUIPPED_FIRST_PERSON);
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        return null;
    }

    public static void transformEatAndDrinkLeft(EntityClientPlayerMP entityclientplayermp, ItemStack stack,
        float partialTick) {
        var f = (float) entityclientplayermp.getItemInUseCount() - partialTick + 1.0F;
        var f1 = f / (float) stack.getMaxItemUseDuration();
        if (f1 < 0.8F) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
            GL11.glTranslatef(0.0F, f2, 0.0F);
        }
        float f3 = 1.0F - (float) Math.pow(f1, 27.0D);
        GL11.glTranslatef(f3 * -1.3F, f3 * -0.5F, f3 * 0.75f);
        GL11.glRotatef(f3 * -90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(f3 * -30.0F, 0.0F, 0.0F, 1.0F);
    }

    public static void transformSideFirstPersonLeft(float equipProgress) {
        GL11.glTranslatef(-0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
    }

    public static void transformBowLeft(ItemStack stack, int itemUseCount, float partialTicks) {
        GL11.glTranslatef(0.2785682F, 0.18344387F, 0.15731531F);
        GL11.glRotatef(-13.935F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-35.3F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(9.785F, 0.0F, 0.0F, 1.0F);
        float f5 = (float) stack.getMaxItemUseDuration() - (itemUseCount - partialTicks + 1.0F);
        float f6 = f5 / 20.0F;
        f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        if (f6 > 0.1F) {
            float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
            float f3 = f6 - 0.1F;
            float f4 = f7 * f3;
            GL11.glTranslatef(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
        }

        GL11.glTranslatef(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
        GL11.glScalef(1.0F, 1.0F, 1.0F + f6 * 0.2F);
        GL11.glRotatef(-45.0F, 0.0F, -1.0F, 0.0F);
    }

    public static float sqrt(float value) {
        return (float) Math.sqrt(value);
    }

    private static void transformFirstPersonLeft(float swingProgress) {
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        GL11.glRotatef(-(45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(sqrt(swingProgress) * (float) Math.PI);
        GL11.glRotatef(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    }

}
