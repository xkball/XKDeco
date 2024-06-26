package com.xkball.xkdeco.common.init;

import net.minecraft.block.Block;

import com.xkball.xkdeco.common.XKDecoTabs;
import com.xkball.xkdeco.common.block.BlockTest;
import com.xkball.xkdeco.common.item.ItemBlockXKDeco;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockInit {

    public static Block blockTest;

    public static void init() {
        blockTest = new BlockTest().setBlockName("xkdeco_test")
            .setCreativeTab(XKDecoTabs.XKDECO_BASIC);
        GameRegistry.registerBlock(blockTest, ItemBlockXKDeco.class, "xkdeco_test");
    }
}
