package com.xkball.xkdeco.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.xkball.xkdeco.api.player.IExtendedPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiListener {

    @SubscribeEvent
    public void onInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiOptions) {
            // noinspection unchecked
            event.buttonList.add(
                new GuiButton(
                    114514,
                    event.gui.width / 2 - 155,
                    event.gui.height / 6 + 48 - 6,
                    150,
                    20,
                    I18n.format("xkdeco.gui.left_hand_main")));
        }
    }

    @SubscribeEvent
    public void onButtonClick(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (event.gui instanceof GuiOptions) {
            if (event.button.id == 114514) {
                var player = IExtendedPlayer.get(Minecraft.getMinecraft().thePlayer);
                player.setLeftHandSide(!player.isLeftHandSide());
                player.syncToServer();
            }
        }
    }
}
