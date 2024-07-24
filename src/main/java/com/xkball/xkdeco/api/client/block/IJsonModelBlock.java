package com.xkball.xkdeco.api.client.block;

import com.xkball.xkdeco.client.model.mapper.BlockMetaModelMapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public interface IJsonModelBlock {

    boolean xkdeco$useJsonModel();

    int xkdeco$getActuallyPass();

    @Nullable
    BlockMetaModelMapper xkdeco$getBlockJsonModelMapper();

    void xkdeco$setBlockJsonModelMapper(@Nullable BlockMetaModelMapper mapper);

    default int xkdeco$getActuallyRenderType(){
        return 114514;
    }

    static IJsonModelBlock cast(Block block) {
        return (IJsonModelBlock) block;
    }
}
