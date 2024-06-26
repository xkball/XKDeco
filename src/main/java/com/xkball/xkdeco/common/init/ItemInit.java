package com.xkball.xkdeco.common.init;

import net.minecraft.item.ItemBlock;

import com.xkball.xkdeco.common.XKDecoTabs;
import com.xkball.xkdeco.common.item.ItemBlockXKDeco;

public class ItemInit {

    public static ItemBlockXKDeco testItemBlock;

    public static void init() {
        testItemBlock = (ItemBlockXKDeco) ItemBlock.getItemFromBlock(BlockInit.blockTest);
        XKDecoTabs.XKDECO_BASIC.setIconItem(testItemBlock);
    }
}
