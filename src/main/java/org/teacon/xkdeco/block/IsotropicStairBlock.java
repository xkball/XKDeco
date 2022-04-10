package org.teacon.xkdeco.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IsotropicStairBlock extends StairBlock {
    public IsotropicStairBlock(Properties properties) {
        super(Blocks.AIR::defaultBlockState, properties);
    }
}
