package nischitweaker.mixins.charm;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.crafting.block.BlockCrate;
import svenhjol.charm.crafting.tile.TileCrate;

/**
 * Charm's BlockCrate#breakBlock blindly casts the tile entity to its own TileCrate.
 * Worldgen (e.g. OTG BO4s) can leave a foreign tile (like Varied Commodities' TileCrate) under a Charm crate,
 * which then crashes with a ClassCastException once the block gets replaced.
 */
@Mixin(BlockCrate.class)
public abstract class BlockCrateMixin extends Block {
    public BlockCrateMixin(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void nischitweaker_skipForeignTile(World world, BlockPos pos, IBlockState state, CallbackInfo ci) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && !(tile instanceof TileCrate)) {
            ci.cancel();
            super.breakBlock(world, pos, state);
        }
    }
}
