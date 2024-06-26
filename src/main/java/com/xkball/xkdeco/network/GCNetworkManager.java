package com.xkball.xkdeco.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.xkball.xkdeco.XKDeco;
import com.xkball.xkdeco.network.packets.PlayerConfigPacket;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

// borrow from https://harbinger.covertdragon.team/chapter-07/forge-extension/fml-event-channel.html
// @Mod.EventBusSubscriber
public enum GCNetworkManager {

    INSTANCE;

    // 获得一个信道实例。建议使用Modid来命名。
    // 当然也可以用别的，保证唯一即可。
    private final SimpleNetworkWrapper channel = new SimpleNetworkWrapper(XKDeco.MOD_ID);

    GCNetworkManager() {
        channel.registerMessage(PlayerConfigPacket.class, PlayerConfigPacket.class, 0, Side.SERVER);
        channel.registerMessage(PlayerConfigPacket.class, PlayerConfigPacket.class, 0, Side.CLIENT);
    }

    public static void init() {

    }

    // 向某个维度发包
    public <T extends IMessage> void sendPacketToDim(GCPacket<T> pkt, int dim) {
        channel.sendToDimension(pkt, dim);
    }

    // 向某个维度的某个点发包
    public <T extends IMessage> void sendPacketAroundPos(GCPacket<T> pkt, int dim, int x, int y, int z) {
        channel.sendToAllAround(pkt, new NetworkRegistry.TargetPoint(dim, x, y, z, 2.0D));
    }

    // 向某个玩家发包
    public <T extends IMessage> void sendPacketToPlayer(GCPacket<T> pkt, EntityPlayerMP player) {
        channel.sendTo(pkt, player);
    }

    // 向所有人发包
    public <T extends IMessage> void sendPacketToAll(GCPacket<T> pkt) {
        channel.sendToAll(pkt);
    }

    // 向服务器发包，这个给客户端用
    public <T extends IMessage> void sendPacketToServer(GCPacket<T> pkt) {
        channel.sendToServer(pkt);
    }

}
