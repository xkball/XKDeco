package org.teacon.xkdeco.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IsotropicStairBlock extends StairBlock {
    public IsotropicStairBlock(RegistryObject<? extends Block> fullBlock, Properties properties) {
        super(() -> fullBlock.get().defaultBlockState(), properties);
    }
}
