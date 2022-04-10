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

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class XKDecoObjects {
    public static final CreativeModeTab TAB_BASIC = new XKDecoCreativeModTab(XKDeco.ID + "_basic");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, XKDeco.ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, XKDeco.ID);

    private static final BlockBehaviour.Properties BLOCK_MUD = BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 3f);
    private static final BlockBehaviour.Properties BLOCK_SANDSTONE = BlockBehaviour.Properties.of(Material.STONE).strength(1.5f, 6f);
    private static final BlockBehaviour.Properties BLOCK_GLASS = BlockBehaviour.Properties.of(Material.GLASS).noOcclusion().isValidSpawn((s, g, p, e) -> false).isRedstoneConductor((s, g, p) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false).strength(1.5f, 3f);
    private static final BlockBehaviour.Properties BLOCK_IRON = BlockBehaviour.Properties.of(Material.METAL).strength(2f, 12f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_WOOD = BlockBehaviour.Properties.of(Material.WOOD).strength(2f, 3f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_BRICK = BlockBehaviour.Properties.of(Material.STONE).strength(1.8f, 6f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_STONE = BlockBehaviour.Properties.of(Material.STONE).strength(1.8f, 9f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_HARD_STONE = BlockBehaviour.Properties.of(Material.STONE).strength(2f, 10f).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BLOCK_LEAVES = BlockBehaviour.Properties.of(Material.LEAVES).noOcclusion();

    private static final Item.Properties ITEM_BASIC = new Item.Properties().tab(TAB_BASIC);

    public static final String GLASS_PREFIX = "glass_";

    public static final String LOG_SUFFIX = "_log";
    public static final String SLAB_SUFFIX = "_slab";
    public static final String STAIRS_SUFFIX = "_stairs";
    public static final String PILLAR_SUFFIX = "_pillar";

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

    private static void addIsotropic(String id,
                                     BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        var isGlass = id.contains(GLASS_PREFIX);
        if (id.contains(LOG_SUFFIX) || id.contains(PILLAR_SUFFIX)) {
            var block = BLOCKS.register(id, () -> new IsotropicPillarBlock(properties, isGlass));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else if (id.contains(SLAB_SUFFIX)) {
            var block = BLOCKS.register(id, () -> new IsotropicSlabBlock(properties, isGlass));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else if (id.contains(STAIRS_SUFFIX)) {
            var block = BLOCKS.register(id, () -> new IsotropicStairBlock(properties, isGlass));
            ITEMS.register(id, () -> new BlockItem(block.get(), itemProperties));
        } else {
            var block = BLOCKS.register(id, () -> new IsotropicCubeBlock(properties, isGlass));
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

    static {
        addIsotropic("black_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("black_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("black_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("cyan_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("cyan_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("yellow_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("yellow_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("yellow_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("blue_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("blue_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("blue_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("green_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("green_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("green_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("red_tiles", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("red_tile_slab", BLOCK_STONE, ITEM_BASIC);
        addIsotropic("red_tile_stairs", BLOCK_STONE, ITEM_BASIC);

        addIsotropic("steel_tiles", BLOCK_IRON, ITEM_BASIC);
        addIsotropic("steel_tile_slab", BLOCK_IRON, ITEM_BASIC);
        addIsotropic("steel_tile_stairs", BLOCK_IRON, ITEM_BASIC);

        addIsotropic("copper_tiles", BLOCK_IRON, ITEM_BASIC);
        addIsotropic("copper_tile_slab", BLOCK_IRON, ITEM_BASIC);
        addIsotropic("copper_tile_stairs", BLOCK_IRON, ITEM_BASIC);

        addIsotropic("glass_tiles", BLOCK_GLASS, ITEM_BASIC);
        addIsotropic("glass_tile_slab", BLOCK_GLASS, ITEM_BASIC);
        addIsotropic("glass_tile_stairs", BLOCK_GLASS, ITEM_BASIC);

        addIsotropic("mud_wall_block", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("mud_wall_slab", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("mud_wall_stairs", BLOCK_MUD, ITEM_BASIC);

        addIsotropic("framed_mud_wall_block", BLOCK_MUD, ITEM_BASIC);

        addIsotropic("lined_mud_wall_block", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("lined_mud_wall_slab", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("lined_mud_wall_stairs", BLOCK_MUD, ITEM_BASIC);

        addIsotropic("crossed_mud_wall_slab", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("crossed_mud_wall_stairs", BLOCK_MUD, ITEM_BASIC);

        addIsotropic("dirty_mud_wall_block", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("dirty_mud_wall_slab", BLOCK_MUD, ITEM_BASIC);
        addIsotropic("dirty_mud_wall_stairs", BLOCK_MUD, ITEM_BASIC);

        addIsotropic("cyan_bricks", BLOCK_BRICK, ITEM_BASIC);
        addIsotropic("cyan_brick_slab", BLOCK_BRICK, ITEM_BASIC);
        addIsotropic("cyan_brick_stairs", BLOCK_BRICK, ITEM_BASIC);

        addIsotropic("black_bricks", BLOCK_BRICK, ITEM_BASIC);
        addIsotropic("black_brick_slab", BLOCK_BRICK, ITEM_BASIC);
        addIsotropic("black_brick_stairs", BLOCK_BRICK, ITEM_BASIC);

        addIsotropic("varnished_wood", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("varnished_log", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("varnished_log_slab", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("ebony_wood", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("ebony_log", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("ebony_log_slab", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("mahogany_wood", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("mahogany_log", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("mahogany_log_slab", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("varnished_planks", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("varnished_slab", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("varnished_stairs", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("ebony_planks", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("ebony_slab", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("ebony_stairs", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("mahogany_planks", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("mahogany_slab", BLOCK_WOOD, ITEM_BASIC);
        addIsotropic("mahogany_stairs", BLOCK_WOOD, ITEM_BASIC);

        addIsotropic("sandstone_pillar", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("polished_sandstone", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("polished_sandstone_slab", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("sandstone_bricks", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("sandstone_brick_slab", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("sandstone_brick_stairs", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("sandstone_small_bricks", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("sandstone_small_brick_slab", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("sandstone_small_brick_stairs", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("red_sandstone_pillar", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("polished_red_sandstone", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("polished_red_sandstone_slab", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("red_sandstone_bricks", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("red_sandstone_brick_slab", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("red_sandstone_brick_stairs", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("red_sandstone_small_bricks", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("red_sandstone_small_brick_slab", BLOCK_SANDSTONE, ITEM_BASIC);
        addIsotropic("red_sandstone_small_brick_stairs", BLOCK_SANDSTONE, ITEM_BASIC);

        addIsotropic("stone_brick_pillar", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("stone_brick_pavement", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("stone_brick_pavement_slab", BLOCK_HARD_STONE, ITEM_BASIC);

        addIsotropic("deepslate_pillar", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("deepslate_pavement", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("deepslate_pavement_slab", BLOCK_HARD_STONE, ITEM_BASIC);

        addIsotropic("mossy_deepslate_bricks", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("mossy_deepslate_brick_slab", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("mossy_deepslate_brick_stairs", BLOCK_HARD_STONE, ITEM_BASIC);

        addIsotropic("blackstone_pillar", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("blackstone_pavement", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("blackstone_pavement_slab", BLOCK_HARD_STONE, ITEM_BASIC);

        addIsotropic("glided_blackstone_bricks", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("glided_blackstone_brick_slab", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("glided_blackstone_brick_stairs", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("glided_blackstone_brick_pillar", BLOCK_HARD_STONE, ITEM_BASIC);

        addIsotropic("chiseled_glided_blackstone", BLOCK_HARD_STONE, ITEM_BASIC);
        addIsotropic("luxury_glided_blackstone", BLOCK_HARD_STONE, ITEM_BASIC);
    }
}
