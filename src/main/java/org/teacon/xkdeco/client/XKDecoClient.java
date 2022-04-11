package org.teacon.xkdeco.client;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.teacon.xkdeco.block.TreeLeavesBlock;
import org.teacon.xkdeco.init.XKDecoObjects;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class XKDecoClient {
    public static void setCutoutBlocks(FMLClientSetupEvent event) {
        for (var entry : XKDecoObjects.BLOCKS.getEntries()) {
            if (entry.getId().getPath().contains(XKDecoObjects.GLASS_PREFIX)) {
                ItemBlockRenderTypes.setRenderLayer(entry.get(), RenderType.cutout());
            }
            if (entry.get() instanceof TreeLeavesBlock) {
                ItemBlockRenderTypes.setRenderLayer(entry.get(), RenderType.cutoutMipped());
            }
        }
    }
}
