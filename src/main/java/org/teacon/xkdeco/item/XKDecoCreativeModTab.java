package org.teacon.xkdeco.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.teacon.xkdeco.XKDeco;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class XKDecoCreativeModTab extends CreativeModeTab {
    private final String itemId;

    private final Lazy<ItemStack> itemStackLazy;

    public XKDecoCreativeModTab(String label, String itemId) {
        super(label);
        this.itemId = itemId;
        this.itemStackLazy = Lazy.of(this::getItemStack);
    }

    @Override
    public ItemStack makeIcon() {
        return this.itemStackLazy.get();
    }

    private ItemStack getItemStack() {
        var resourceLocation = new ResourceLocation(XKDeco.ID, this.itemId);
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(resourceLocation)).getDefaultInstance();
    }
}
