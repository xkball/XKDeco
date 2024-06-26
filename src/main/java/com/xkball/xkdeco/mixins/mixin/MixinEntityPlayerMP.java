package com.xkball.xkdeco.mixins.mixin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.ServerConfigurationManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.network.GCNetworkManager;
import com.xkball.xkdeco.network.packets.PlayerConfigPacket;

@Mixin(ServerConfigurationManager.class)
public class MixinEntityPlayerMP {

    @Inject(method = "initializeConnectionToPlayer", at = @At("RETURN"), remap = false)
    public void afterLoad(NetworkManager netManager, EntityPlayerMP player, NetHandlerPlayServer nethandlerplayserver,
        CallbackInfo ci) {
        var syncTag = IExtendedPlayer.get(player)
            .xkdeco$getAppendTag();
        syncTag.setString("name", player.getCommandSenderName());
        GCNetworkManager.INSTANCE.sendPacketToPlayer(new PlayerConfigPacket(syncTag), player);
    }
}
