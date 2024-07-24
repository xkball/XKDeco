package com.xkball.xkdeco.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public class ClientUtils {

    public static void useDefaultColor(){
        var color = 16777215;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        Tessellator.instance.setColorOpaque_F(red, green, blue);
    }
}
