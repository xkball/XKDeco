package com.xkball.xkdeco.common.event;

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

    public void test(int i, int j) {
        i += 10;
        j += 10;
    }
}
