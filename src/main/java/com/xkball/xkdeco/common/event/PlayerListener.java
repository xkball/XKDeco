package com.xkball.xkdeco.common.event;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.network.GCNetworkManager;
import com.xkball.xkdeco.network.packets.PlayerConfigPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

//真服了这倒霉运气 每次以为可以用事件解决最后还是得mixin
public class PlayerListener {

    // @Mod.EventHandler
    // public void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
    // if (event.player instanceof EntityPlayerMP emp) {
    // GCNetworkManager.INSTANCE.sendPacketToPlayer(
    // new PlayerConfigPacket(
    // IExtendedPlayer.get(emp)
    // .xkdeco$getAppendTag()),
    // emp);
    // }
    // }

//   @SubscribeEvent
//   public void onPlayerJoinWorld(EntityJoinWorldEvent event){
//       var entity = event.entity;
//       if(!(entity instanceof EntityPlayerMP player)) return;
//       var syncTag = IExtendedPlayer.get(player).xkdeco$getAppendTag();
//       syncTag.setString("name", player.getCommandSenderName());
//       GCNetworkManager.INSTANCE.sendPacketToPlayer(new PlayerConfigPacket(syncTag), player);
//
//   }

}
