package nischitweaker.mixins.suikecherry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import suike.suikecherry.entity.boat.ModEntityChestBoat;

@Mixin(ModEntityChestBoat.ChestInventory.class)
public abstract class ChestBoatInventoryMixin {

    @WrapOperation(
            method = "validateAndUpdateContents",
            at = @At(value = "INVOKE", target = "Lsuike/suikecherry/entity/boat/ModEntityChestBoat$ChestInventory;readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"),
            remap = false
    )
    private void nischitweaker_keepServerContents(ModEntityChestBoat.ChestInventory inventory, NBTTagCompound compound, Operation<Void> original) {
        //no op
    }
}
