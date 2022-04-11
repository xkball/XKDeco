package org.teacon.xkdeco.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.teacon.xkdeco.XKDeco;

import java.util.stream.Stream;

import static org.teacon.xkdeco.init.XKDecoObjects.BLOCKS;
import static org.teacon.xkdeco.init.XKDecoObjects.SLAB_SUFFIX;

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
        for (var entry : BLOCKS.getEntries()) {
            var block = entry.get();
            var id = entry.getId().getPath();
            if (block instanceof SlabBlock slabBlock) {
                var doubleSlabs = Stream.of("", "s", "_block").map(s -> id.replace(SLAB_SUFFIX, s)).toList();
                var doubleSlabId = BLOCKS.getEntries().stream().map(r -> r.getId().getPath())
                        .filter(doubleSlabs::contains).findFirst().orElse(doubleSlabs.get(doubleSlabs.size() - 1));
                this.slabBlock(slabBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_top")),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + doubleSlabId)));
                this.simpleBlockItem(slabBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (block instanceof StairBlock stairBlock) {
                this.stairsBlock(stairBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_inner")),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_outer")));
                this.simpleBlockItem(stairBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (block instanceof RotatedPillarBlock rotatedPillarBlock) {
                this.axisBlock(rotatedPillarBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_horizontal")));
                this.simpleBlockItem(rotatedPillarBlock,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                this.horizontalBlock(block,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
                this.simpleBlockItem(block,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else {
                this.simpleBlock(block,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
                this.simpleBlockItem(block,
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            }
        }
    }
}
