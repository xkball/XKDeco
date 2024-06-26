package com.xkball.xkdeco.mixins.mixin.client;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.mixins.api.IMixinPlayerRender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mixin(RenderPlayer.class)
@SideOnly(Side.CLIENT)
public class MixinRenderPlayer implements IMixinPlayerRender {

    @Shadow
    public ModelBiped modelBipedMain;
    @Unique
    public EntityPlayer xkdeco$context;

    // @Inject(method = "renderFirstPersonArm",at = @At("HEAD"),cancellable = true)
    // public void onRenderFirstPersonHand(EntityPlayer p_82441_1_, CallbackInfo ci){
    // float f = 1.0F;
    // GL11.glColor3f(f, f, f);
    // this.modelBipedMain.onGround = 0.0F;
    // this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_82441_1_);
    // this.modelBipedMain.bipedLeftArm.render(0.0625F);
    // ci.cancel();
    // }

    public void xkdeco$renderFirstPersonLeftHand(EntityPlayer player) {
        float f = 1.0F;
        GL11.glColor3f(f, f, f);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        this.modelBipedMain.bipedLeftArm.render(0.0625F);
    }

    // 哇哦 可以用事件代替
    // @Redirect(method = "renderEquippedItems(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V",
    // at = @At(value = "FIELD",
    // target = "Lnet/minecraftforge/client/event/RenderPlayerEvent$Specials$Pre;renderItem:Z",
    // opcode = Opcodes.GETFIELD))
    // public boolean onRenderThirdPerson(RenderPlayerEvent.Specials.Pre event){
    //
    // }

    @Redirect(
        method = "renderEquippedItems(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/model/ModelBiped;bipedRightArm:Lnet/minecraft/client/model/ModelRenderer;",
            opcode = Opcodes.GETFIELD))
    public ModelRenderer onPrepareHand(ModelBiped modelBipedMain) {
        if (xkdeco$context == null || IExtendedPlayer.get(xkdeco$context)
            .isLeftHandSide()) return modelBipedMain.bipedRightArm;
        return modelBipedMain.bipedLeftArm;
    }

    @Redirect(
        method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/model/ModelBiped;heldItemRight:I",
            opcode = Opcodes.PUTFIELD))
    public void handleHeldHand(ModelBiped instance, int value) {
        if (xkdeco$context == null || IExtendedPlayer.get(xkdeco$context)
            .isLeftHandSide()) {
            instance.heldItemRight = value;
        } else {
            instance.heldItemRight = 0;
            instance.heldItemLeft = value;
        }
    }

    @Inject(method = "renderEquippedItems(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V", at = @At("HEAD"))
    public void beforeRenderEquip(AbstractClientPlayer p_77029_1_, float p_77029_2_, CallbackInfo ci) {
        xkdeco$context = p_77029_1_;
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("HEAD"))
    public void beforeRender(AbstractClientPlayer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
        float p_76986_8_, float p_76986_9_, CallbackInfo ci) {
        xkdeco$context = p_76986_1_;
    }
}
