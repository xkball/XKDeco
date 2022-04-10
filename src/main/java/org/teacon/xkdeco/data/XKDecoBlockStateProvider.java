package org.teacon.xkdeco.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.teacon.xkdeco.XKDeco;

import java.util.stream.Stream;

import static org.teacon.xkdeco.init.XKDecoObjects.*;

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
            var id = entry.getId().getPath();
            if (id.contains(SLAB_SUFFIX)) {
                var doubleSlabs = Stream.of("", "s", "_block").map(s -> id.replace(SLAB_SUFFIX, s)).toList();
                var doubleSlabId = BLOCKS.getEntries().stream().map(r -> r.getId().getPath())
                        .filter(doubleSlabs::contains).findFirst().orElse(doubleSlabs.get(doubleSlabs.size() - 1));
                this.slabBlock((SlabBlock) entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_top")),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + doubleSlabId)));
                this.simpleBlockItem(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (id.contains(STAIRS_SUFFIX)) {
                this.stairsBlock((StairBlock) entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_inner")),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_outer")));
                this.simpleBlockItem(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (id.contains(LOG_SUFFIX) || id.contains(WOOD_SUFFIX) || id.contains(PILLAR_SUFFIX)) {
                this.axisBlock((RotatedPillarBlock) entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_horizontal")));
                this.simpleBlockItem(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else if (id.contains(LUXURY_PREFIX) || id.contains(CHISELED_PREFIX)) {
                this.axisBlock((RotatedPillarBlock) entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id + "_horizontal")));
                this.simpleBlockItem(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            } else {
                this.simpleBlock(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
                this.simpleBlockItem(entry.get(),
                        new UncheckedModelFile(new ResourceLocation(XKDeco.ID, "block/" + id)));
            }
        }
    }
}
