package com.xkball.xkdeco.api.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.xkball.xkdeco.network.GCNetworkManager;
import com.xkball.xkdeco.network.packets.PlayerConfigPacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IExtendedPlayer {

    NBTTagCompound xkdeco$getAppendTag();

    default void setLeftHandSide(boolean left) {
        xkdeco$getAppendTag().setBoolean("left_hand_side", left);
    }

    default boolean isLeftHandSide() {
        return xkdeco$getAppendTag().getBoolean("left_hand_side");
    }

    default void refreshTag(NBTTagCompound tag) {
        if (tag == null) return;
        if (tag.hasKey("left_hand_side")) {
            setLeftHandSide(tag.getBoolean("left_hand_side"));
        }
    }

    @SideOnly(Side.CLIENT)
    default void syncToServer() {
        GCNetworkManager.INSTANCE.sendPacketToServer(new PlayerConfigPacket(xkdeco$getAppendTag()));
    }

    static IExtendedPlayer get(EntityPlayer player) {
        return (IExtendedPlayer) player;
    }

}
