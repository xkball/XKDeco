package com.xkball.xkdeco.mixins.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.xkball.xkdeco.api.player.IExtendedPlayer;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IExtendedPlayer {

    @Unique
    NBTTagCompound xkdeco$appendTag = new NBTTagCompound();

    @Override
    public NBTTagCompound xkdeco$getAppendTag() {
        return xkdeco$appendTag;
    }

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    public void onRead(NBTTagCompound tagCompound, CallbackInfo ci) {
        xkdeco$appendTag = tagCompound.getCompoundTag("xkdeco$appendTag");
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    public void onWrite(NBTTagCompound tagCompound, CallbackInfo ci) {
        tagCompound.setTag("xkdeco$appendTag", xkdeco$appendTag);
    }

}
