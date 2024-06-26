package com.xkball.xkdeco.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;

public interface GCPacket<T extends IMessage> extends IMessage, IMessageHandler<T, IMessage> {
}
