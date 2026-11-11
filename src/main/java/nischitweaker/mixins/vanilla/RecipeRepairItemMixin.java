package nischitweaker.mixins.vanilla;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeRepairItem;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeRepairItem.class)
public abstract class RecipeRepairItemMixin {

    // Vanilla returns each input's container item, so repairable items with a container item (like NTP saws) stay in the grid -> dupe
    @WrapMethod(method = "getRemainingItems")
    private NonNullList<ItemStack> nischitweaker_noContainerItemsOnRepair(InventoryCrafting inv, Operation<NonNullList<ItemStack>> cir) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }
}
