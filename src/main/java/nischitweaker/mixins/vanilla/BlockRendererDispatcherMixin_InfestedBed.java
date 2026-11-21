package nischitweaker.mixins.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import nischitweaker.features.infestedbed.BlockScaleDamageModel;
import nischitweaker.features.infestedbed.InfestedBed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockRendererDispatcher.class)
public abstract class BlockRendererDispatcherMixin_InfestedBed {
    @WrapOperation(
            method = "renderBlockDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelShapes;getModelForState(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/client/renderer/block/model/IBakedModel;")
    )
    private IBakedModel nischitweaker_infestedBedDamageModel(BlockModelShapes shapes, IBlockState state, Operation<IBakedModel> original) {
        IBakedModel model = original.call(shapes, state);
        return state.getBlock() == InfestedBed.BLOCK ? new BlockScaleDamageModel(model) : model;
    }
}
