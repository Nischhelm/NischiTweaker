package nischitweaker.mixins.nuclearcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import nc.network.gui.EmptyTankPacket;
import nc.network.gui.OpenGuiPacket;
import nc.network.gui.OpenSideConfigGuiPacket;
import nc.network.gui.OpenTileGuiPacket;
import nc.network.gui.ResetItemSorptionsPacket;
import nc.network.gui.ResetTankSorptionsPacket;
import nc.network.gui.ToggleAlternateComparatorPacket;
import nc.network.gui.ToggleInputTanksSeparatedPacket;
import nc.network.gui.ToggleItemOutputSettingPacket;
import nc.network.gui.ToggleItemSorptionPacket;
import nc.network.gui.ToggleRedstoneControlPacket;
import nc.network.gui.ToggleTankOutputSettingPacket;
import nc.network.gui.ToggleTankSorptionPacket;
import nc.network.gui.ToggleVoidExcessFluidOutputPacket;
import nc.network.gui.ToggleVoidUnusableFluidInputPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.features.NuclearCraftPacketRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EmptyTankPacket.Handler.class,
        OpenGuiPacket.Handler.class,
        OpenSideConfigGuiPacket.Handler.class,
        OpenTileGuiPacket.Handler.class,
        ResetItemSorptionsPacket.Handler.class,
        ResetTankSorptionsPacket.Handler.class,
        ToggleAlternateComparatorPacket.Handler.class,
        ToggleInputTanksSeparatedPacket.Handler.class,
        ToggleItemOutputSettingPacket.Handler.class,
        ToggleItemSorptionPacket.Handler.class,
        ToggleRedstoneControlPacket.Handler.class,
        ToggleTankOutputSettingPacket.Handler.class,
        ToggleTankSorptionPacket.Handler.class,
        ToggleVoidExcessFluidOutputPacket.Handler.class,
        ToggleVoidUnusableFluidInputPacket.Handler.class
})
public abstract class GuiPacketHandlersMixin {

    @WrapOperation(
            method = "processMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;getTileEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;", remap = true),
            remap = false
    )
    private TileEntity nischitweaker_onlyNearbyTiles(WorldServer world, BlockPos pos, Operation<TileEntity> original, @Local(argsOnly = true) MessageContext ctx) {
        if (!NuclearCraftPacketRange.isInRange(ctx.getServerHandler().player, pos)) return null;
        return original.call(world, pos);
    }
}
