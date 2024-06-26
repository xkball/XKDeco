package com.xkball.xkdeco.mixins.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(Minecraft.class)
public interface MixinMinecraftAccess {

    @Accessor
    Timer getTimer();
}
