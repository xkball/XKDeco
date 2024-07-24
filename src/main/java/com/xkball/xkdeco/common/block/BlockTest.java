package com.xkball.xkdeco.common.block;

import com.xkball.xkdeco.client.JsonModelManager;
import com.xkball.xkdeco.client.render.model.JsonModelBlockRender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

import java.util.Objects;

public class BlockTest extends Block {

    public BlockTest() {
        super(Material.ground);
    }

    @Override
    public int getRenderType() {
        return 114514;
    }

    @Override
    protected String getTextureName() {
        return "";
    }

    @Override
    public float getAmbientOcclusionLightValue() {
        return 1.0f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return JsonModelManager.INSTANCE.getIcon(Objects.requireNonNull(JsonModelManager.INSTANCE.getRawModel(JsonModelBlockRender.Globe)).getParticle());
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

}
