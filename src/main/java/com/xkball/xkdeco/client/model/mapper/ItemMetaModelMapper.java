package com.xkball.xkdeco.client.model.mapper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

@SideOnly(Side.CLIENT)
public interface ItemMetaModelMapper {
    @Nullable
    ResourceLocation mapModel(ItemStack itemStack);

    Collection<ResourceLocation> possibleResourceLocations();
}
