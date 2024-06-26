package com.xkball.xkdeco.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import com.xkball.xkdeco.api.player.IExtendedPlayer;
import com.xkball.xkdeco.network.GCNetworkManager;
import com.xkball.xkdeco.network.GCPacket;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PlayerConfigPacket implements GCPacket<PlayerConfigPacket> {

    NBTTagCompound tag;

    public PlayerConfigPacket() {
        this.tag = new NBTTagCompound();
    }

    public PlayerConfigPacket(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public IMessage onMessage(PlayerConfigPacket message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            var player = ctx.getServerHandler().playerEntity;
            IExtendedPlayer.get(player)
                .refreshTag(message.tag);
            message.tag.setString("name", player.getCommandSenderName());
            GCNetworkManager.INSTANCE.sendPacketToAll(new PlayerConfigPacket(message.tag));
        } else if (ctx.side.isClient()) {
            handleClient(message, ctx);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void handleClient(PlayerConfigPacket message, MessageContext ctx) {
        if (!message.tag.hasKey("name")) return;
        var name = message.tag.getString("name");
        if (Minecraft.getMinecraft().thePlayer.getCommandSenderName()
            .equals(name)) {
            IExtendedPlayer.get(Minecraft.getMinecraft().thePlayer)
                .refreshTag(message.tag);
        } else Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
            .filter(
                e -> e instanceof EntityOtherPlayerMP && e.getCommandSenderName()
                    .equals(name))
            .map(e -> (EntityOtherPlayerMP) e)
            .findFirst()
            .ifPresent(
                p -> IExtendedPlayer.get(p)
                    .refreshTag(message.tag));
    }
}
