package com.xkball.xkdeco.mixins.mixin;

import com.xkball.xkdeco.api.client.block.IJsonModelBlock;
import com.xkball.xkdeco.client.model.mapper.BlockMetaModelMapper;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Block.class)
public class MixinBlock implements IJsonModelBlock {

    @Unique
    private BlockMetaModelMapper xkdeco$blockMetaModelMapper = null;

    @Inject(method = "canRenderInPass",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    public void onCheckPass(int pass, CallbackInfoReturnable<Boolean> cir){
        if(xkdeco$useJsonModel()){
            cir.setReturnValue(pass == xkdeco$getActuallyPass());
            cir.cancel();
        }
    }

    @Inject(method = "getRenderBlockPass",
        at = @At("HEAD"),
        cancellable = true)
    public void onGetPass(CallbackInfoReturnable<Integer> cir){
        if(xkdeco$useJsonModel()){
            cir.setReturnValue(xkdeco$getActuallyPass());
            cir.cancel();
        }
    }

    @Inject(method = "getRenderType",
            at = @At("HEAD"),
            cancellable = true)
    public void onGetRenderType(CallbackInfoReturnable<Integer> cir){
        if(xkdeco$useJsonModel()){
            cir.setReturnValue(xkdeco$getActuallyRenderType());
            cir.cancel();
        }
    }

    @Override
    public boolean xkdeco$useJsonModel() {
        return xkdeco$blockMetaModelMapper != null;
    }

    @Override
    public int xkdeco$getActuallyPass() {
        return xkdeco$blockMetaModelMapper.getPass();
    }

    @Override
    @Nullable
    public BlockMetaModelMapper xkdeco$getBlockJsonModelMapper() {
        return xkdeco$blockMetaModelMapper;
    }

    @Override
    public void xkdeco$setBlockJsonModelMapper(@Nullable BlockMetaModelMapper mapper) {
        xkdeco$blockMetaModelMapper = mapper;
    }
}
