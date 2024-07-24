package com.xkball.xkdeco.mixins.mixin;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.network.GCNetworkManager;
import com.xkball.xkdeco.network.packets.PlayerConfigPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerConfigurationManager.class)
public class MixinServerConfigManager {

    @Inject(method = "respawnPlayer",at = @At("RETURN"))
    public void afterRespawn(EntityPlayerMP player, int dimension, boolean conqueredEnd, CallbackInfoReturnable<EntityPlayerMP> cir){
        var newPlayer = cir.getReturnValue();
        var syncTag = ((IExtendedPlayer)player).xkdeco$getAppendTag();
        ((IExtendedPlayer)newPlayer).refreshTag(syncTag);
        GCNetworkManager.INSTANCE.sendPacketToPlayer(new PlayerConfigPacket(syncTag), player);

    }
}
