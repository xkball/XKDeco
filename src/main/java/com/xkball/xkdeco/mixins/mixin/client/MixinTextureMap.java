package com.xkball.xkdeco.mixins.mixin.client;

import net.minecraft.client.renderer.texture.TextureMap;

import net.minecraft.client.resources.IResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.xkball.xkdeco.client.JsonModelManager;

@Mixin(TextureMap.class)
public class MixinTextureMap {

    @Shadow
    @Final
    private String basePath;

    @Inject(method = "registerIcons", at = @At("RETURN"))
    public void afterRegisterIcons(CallbackInfo ci) {
        if(!"textures/blocks".equals(basePath)) return;
        JsonModelManager.INSTANCE.loadRawModels((TextureMap)(Object)this);
    }

    @Inject(method = "loadTexture", at = @At("RETURN"))
    public void afterLoadTexture(IResourceManager resourceManager, CallbackInfo ci){
        if(!"textures/blocks".equals(basePath)) return;
        JsonModelManager.INSTANCE.bakeRawModels((TextureMap)(Object)this);
    }

}
