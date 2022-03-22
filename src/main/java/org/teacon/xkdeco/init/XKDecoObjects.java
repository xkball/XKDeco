package org.teacon.xkdeco.init;

import com.google.common.collect.Maps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.teacon.xkdeco.XKDeco;
import org.teacon.xkdeco.block.*;
import org.teacon.xkdeco.item.XKDecoCreativeModTab;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Comparator;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class XKDecoObjects {
    public static final CreativeModeTab TAB_BASIC = new XKDecoCreativeModTab(XKDeco.ID + "_basic");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, XKDeco.ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, XKDeco.ID);

    private static final BlockBehaviour.Properties BLOCK_DIRT = BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 3f);
    private static final BlockBehaviour.Properties BLOCK_GLASS = BlockBehaviour.Properties.of(Material.GLASS).noOcclusion().strength(1.5f, 3f);
    private static final BlockBehaviour.Properties BLOCK_WOOD = BlockBehaviour.Properties.of(Material.WOOD).strength(2f, 3f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_STONE = BlockBehaviour.Properties.of(Material.STONE).strength(1.8f, 9f).requiresCorrectToolForDrops();

    private static final Item.Properties ITEM_BASIC = new Item.Properties().tab(TAB_BASIC);

    private static final String SLAB_SUFFIX = "_slab";
    private static final String STAIRS_SUFFIX = "_stairs";

    private static void addBasic(String id, ShapeFunction shapeFunction,
                                 BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        var horizontalShapes = Maps.toMap(Direction.Plane.HORIZONTAL, shapeFunction::getShape);
        if (horizontalShapes.values().stream().anyMatch(s -> Block
                .isFaceFull(Shapes.join(Shapes.block(), s, BooleanOp.ONLY_FIRST), Direction.DOWN))) {
            var shapes = Maps.toMap(Arrays.stream(Direction.values()).toList(), shapeFunction::getShape);
            var block = BLOCKS.register(id, () -> new BasicFullDirectionBlock(properties, shapes));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else if (horizontalShapes.values().stream().allMatch(Block::isShapeFullBlock)) {
            var block = BLOCKS.register(id, () -> new BasicCubeBlock(properties, horizontalShapes));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else {
            var block = BLOCKS.register(id, () -> new BasicBlock(properties, horizontalShapes));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        }
    }

    private static void addIsotropic(String id, VoxelShape shape,
                                     BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        if (id.endsWith(SLAB_SUFFIX)) {
            var block = BLOCKS.register(id, () -> new IsotropicSlabBlock(properties));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else if (id.endsWith(STAIRS_SUFFIX)) {
            var prefix = id.substring(0, id.length() - STAIRS_SUFFIX.length());
            var fullBlockObject = BLOCKS.getEntries().stream()
                    .filter(r -> r.getId().getPath().startsWith(prefix))
                    .min(Comparator.comparing(r -> r.getId().getPath().length())).orElseThrow();
            var block = BLOCKS.register(id, () -> new IsotropicStairBlock(fullBlockObject, properties));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else if (Block.isShapeFullBlock(shape)) {
            var block = BLOCKS.register(id, () -> new IsotropicCubeBlock(properties));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else {
            var block = BLOCKS.register(id, () -> new IsotropicBlock(properties, shape));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        }
    }

    private static void addTree(String id,
                                BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        var block = BLOCKS.register(id, () -> new TreeLeavesBlock(properties));
        ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
    }

    @FunctionalInterface
    private interface ShapeFunction {
        VoxelShape getShape(Direction direction);

        static ShapeFunction fromSouth(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            return d -> switch (d) {
                case UP -> Block.box(minX, minZ, 16 - maxY, maxX, maxZ, 16 - minY);
                case DOWN -> Block.box(minX, 16 - maxZ, minY, maxX, 16 - minZ, maxY);
                case SOUTH -> Block.box(minX, minY, minZ, maxX, maxY, maxZ);
                case EAST -> Block.box(minZ, minY, 16 - maxX, maxZ, maxY, 16 - minX);
                case NORTH -> Block.box(16 - maxX, minY, 16 - maxZ, 16 - minX, maxY, 16 - minZ);
                case WEST -> Block.box(16 - maxZ, minY, minX, 16 - minZ, maxY, maxX);
            };
        }
    }


    static final BlockBehaviour.Properties BLOCK_LEAVES = BlockBehaviour.Properties.of(Material.LEAVES).noOcclusion();

    static {
        addBasic("big_book_stack", ShapeFunction.fromSouth(0, 0, 0, 16, 9, 16), BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("black_tiles", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("black_tile_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("black_tile_stairs", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("blue_tiles", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("blue_tile_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("blue_tile_stairs", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("bluewhite_porcelain", Block.box(2, 0, 2, 14, 16, 14), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("bluewhite_porcelain_small", Block.box(5, 0, 5, 11, 12, 11), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("bluewhite_porcelain_tall", Block.box(2, 0, 2, 14, 16, 14), BLOCK_STONE, ITEM_BASIC);

        addBasic("bonsai", ShapeFunction.fromSouth(0, 0, 3, 16, 6, 13), BLOCK_STONE, ITEM_BASIC);

        addBasic("bottle_stack", ShapeFunction.fromSouth(2, 0, 2, 14, 8, 14), BLOCK_GLASS, ITEM_BASIC);

        addIsotropic("celadon_porcelain", Block.box(2, 0, 2, 14, 16, 14), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("celadon_porcelain_small", Block.box(5, 0, 5, 11, 12, 11), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("celadon_porcelain_tall", Block.box(2, 0, 2, 14, 16, 14), BLOCK_STONE, ITEM_BASIC);

        addTree("cherry_blossom", BLOCK_LEAVES, ITEM_BASIC);
        addTree("cherry_blossom_leaves", BLOCK_LEAVES, ITEM_BASIC);

        addIsotropic("cobblestone_path", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cobblestone_path_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cobblestone_path_stairs", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("column_base", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("column_base_light", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addBasic("column_head", ShapeFunction.fromSouth(4, 4, 0, 12, 12, 10), BLOCK_STONE, ITEM_BASIC);

        addBasic("coral_bonsai", ShapeFunction.fromSouth(0, 0, 3, 16, 6, 13), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("crimson_nylium_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("cyan_bricks", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_brick_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_brick_stairs", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("cyan_tiles", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_tile_slab", Shapes.block(), BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_tile_stairs", Shapes.block(), BLOCK_STONE, ITEM_BASIC);

        addBasic("dark_fish_bowl", ShapeFunction.fromSouth(1, 0, 1, 15, 6, 15), BLOCK_STONE, ITEM_BASIC);

        addIsotropic("dirt_slab", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);

        addIsotropic("dirt_cobblestone", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);
        addIsotropic("dirt_cobblestone_slab", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);
        addIsotropic("dirt_cobblestone_stairs", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);

        addIsotropic("dirt_cobblestone_path", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);
        addIsotropic("dirt_cobblestone_path_slab", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);
        addIsotropic("dirt_cobblestone_path_stairs", Shapes.block(), BLOCK_DIRT, ITEM_BASIC);

        addBasic("fish_tank", ShapeFunction.fromSouth(0, 0, 0, 16, 16, 16), BLOCK_STONE, ITEM_BASIC);
    }
}
