package org.teacon.xkdeco.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.teacon.xkdeco.XKDeco;
import org.teacon.xkdeco.init.XKDecoObjects;

import java.util.List;

public class XKDecoBlockStateProvider extends BlockStateProvider {
    private XKDecoBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, XKDeco.ID, existingFileHelper);
    }

    public static void register(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(new XKDecoBlockStateProvider(generator, existingFileHelper));
    }

    @Override
    protected void registerStatesAndModels() {
        for (var entry : XKDecoObjects.BLOCKS.getEntries()) {
            var id = entry.getId().getPath();
            if (id.contains(XKDecoObjects.LOG_SUFFIX)) {
                var horizontalId = id.replace(XKDecoObjects.LOG_SUFFIX, XKDecoObjects.LOG_SUFFIX + "_horizontal");
                this.axisBlock((RotatedPillarBlock) entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, horizontalId)));
                this.simpleBlockItem(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
            } else if (id.contains(XKDecoObjects.PILLAR_SUFFIX)) {
                var horizontalId = id.replace(XKDecoObjects.PILLAR_SUFFIX, XKDecoObjects.PILLAR_SUFFIX + "_horizontal");
                this.axisBlock((RotatedPillarBlock) entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, horizontalId)));
                this.simpleBlockItem(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
            } else if (id.contains(XKDecoObjects.SLAB_SUFFIX)) {
                var topSlabId = id.replace(XKDecoObjects.SLAB_SUFFIX, XKDecoObjects.SLAB_SUFFIX + "_top");
                var doubleSlabs = List.of(
                        id.replace(XKDecoObjects.SLAB_SUFFIX, ""),
                        id.replace(XKDecoObjects.SLAB_SUFFIX, "s"),
                        id.replace(XKDecoObjects.SLAB_SUFFIX, "_block"));
                var doubleSlabId = XKDecoObjects.BLOCKS.getEntries().stream().map(r -> r.getId().getPath())
                        .filter(doubleSlabs::contains).findFirst().orElse(doubleSlabs.get(doubleSlabs.size() - 1));
                this.slabBlock((SlabBlock) entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, topSlabId)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, doubleSlabId)));
                this.simpleBlockItem(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
            } else if (id.contains(XKDecoObjects.STAIRS_SUFFIX)) {
                var innerStairsId = id.replace(XKDecoObjects.STAIRS_SUFFIX, XKDecoObjects.STAIRS_SUFFIX + "_inner");
                var outerStairsId = id.replace(XKDecoObjects.STAIRS_SUFFIX, XKDecoObjects.STAIRS_SUFFIX + "_outer");
                this.stairsBlock((StairBlock) entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, innerStairsId)),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, outerStairsId)));
                this.simpleBlockItem(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
            } else {
                this.simpleBlock(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
                this.simpleBlockItem(entry.get(),
                        new ModelFile.UncheckedModelFile(new ResourceLocation(XKDeco.ID, id)));
            }
        }
    }
}
