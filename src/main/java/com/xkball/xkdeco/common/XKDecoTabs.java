package com.xkball.xkdeco.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class XKDecoTabs extends CreativeTabs {

    public static final XKDecoTabs XKDECO_BASIC = new XKDecoTabs("basic");

    private Item iconItem;

    public XKDecoTabs(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return iconItem;
    }

    public void setIconItem(Item iconItem) {
        this.iconItem = iconItem;
    }
}
