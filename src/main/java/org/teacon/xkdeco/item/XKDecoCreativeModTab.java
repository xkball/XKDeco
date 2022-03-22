package org.teacon.xkdeco.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.teacon.xkdeco.XKDeco;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class XKDecoCreativeModTab extends CreativeModeTab {
    private static final RegistryObject<Item> FISH_TANK_ITEM;

    static {
        FISH_TANK_ITEM = RegistryObject.of(new ResourceLocation(XKDeco.ID, "fish_tank"), ForgeRegistries.ITEMS);
    }

    public XKDecoCreativeModTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return FISH_TANK_ITEM.get().getDefaultInstance();
    }
}
