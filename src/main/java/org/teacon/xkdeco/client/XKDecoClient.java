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
            var id = entry.getId().getPath();
            if (id.contains(XKDecoObjects.GLASS_SUFFIX) || id.contains(XKDecoObjects.TRANSLUCENT_PREFIX)) {
                ItemBlockRenderTypes.setRenderLayer(entry.get(), RenderType.translucent());
            } else if (id.contains(XKDecoObjects.GLASS_PREFIX)) {
                ItemBlockRenderTypes.setRenderLayer(entry.get(), RenderType.cutout());
            } else if (entry.get() instanceof TreeLeavesBlock) {
                ItemBlockRenderTypes.setRenderLayer(entry.get(), RenderType.cutoutMipped());
            }
        }
    }
}
