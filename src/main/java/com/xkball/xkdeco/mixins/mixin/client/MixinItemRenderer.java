package com.xkball.xkdeco.mixins.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.client.render.FirstPersonRender;
import com.xkball.xkdeco.mixins.api.IMixinPlayerRender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow
    private Minecraft mc;
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private float equippedProgress;

    @SuppressWarnings("DuplicatedCode")
    @Redirect(
        method = "renderItemInFirstPerson",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;isInvisible()Z"))
    public boolean onRenderHand(EntityClientPlayerMP instance) {
        if (!IExtendedPlayer.get(Minecraft.getMinecraft().thePlayer)
            .isLeftHandSide()) return false;
        if (FirstPersonRender.ITEM_IN_HAND_RENDERED) return true;
        var partialTick = ((MixinMinecraftAccess) mc).getTimer().renderPartialTicks;
        float f1 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTick;
        float swingProgress;
        float f6;
        float f7;
        GL11.glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        float f_ = instance.prevRenderArmPitch + (instance.renderArmPitch - instance.prevRenderArmPitch) * partialTick;
        float f1_ = instance.prevRenderArmYaw + (instance.renderArmYaw - instance.prevRenderArmYaw) * partialTick;
        GL11.glRotatef((instance.rotationPitch - f_) * 0.1F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((instance.rotationYaw - f1_) * 0.1F, 0.0F, 1.0F, 0.0F);
        // f13 = 0.8F;
        swingProgress = instance.getSwingProgress(partialTick);
        // swingProgress = 1-f1;
        // f6 = MathHelper.sin(swingProgress * (float)Math.PI);
        // f7 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float qf5 = MathHelper.sqrt_float(swingProgress);
        float f2 = -0.3F * MathHelper.sin(qf5 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(qf5 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        GL11.glTranslatef(-1 * (f2 + 0.64000005F), f3 - 0.6F + (1 - f1) * -0.6F, f4 + -0.71999997F);
        // GL11.glTranslatef(f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F) *
        // 0.4F, -f6 *
        // 0.4F);
        // GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
        GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        f6 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        f7 = MathHelper.sin(qf5 * (float) Math.PI);
        GL11.glRotatef(f7 * -70.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(f6 * 20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager()
            .bindTexture(instance.getLocationSkin());
        GL11.glTranslatef(1.0F, 3.6F, 3.5F);
        GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(-5.6F, 0.0F, 0.0F);
        var render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
        var renderPlayer = (IMixinPlayerRender) render;
        renderPlayer.xkdeco$renderFirstPersonLeftHand(this.mc.thePlayer);
        GL11.glPopMatrix();
        return true;
    }
}
