package org.teacon.xkdeco.block;

public sealed interface XKDecoBlock permits
        BasicBlock, BasicCubeBlock, BasicFullDirectionBlock,
        IsotropicCubeBlock, IsotropicPillarBlock, IsotropicSlabBlock, IsotropicStairBlock,
        TreeLeavesBlock {
    // nothing here
}
