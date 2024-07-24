package com.xkball.xkdeco.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import javax.annotation.Nullable;

public class VanillaUtils {

    @Nullable
    public static Item getItem(String id){
        return (Item) Item.itemRegistry.getObject(id);
    }

    @Nullable
    public static Block getBlock(String id){
        return (Block) Block.blockRegistry.getObject(id);
    }
}
