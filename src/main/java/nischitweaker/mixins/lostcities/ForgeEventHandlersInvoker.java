package nischitweaker.mixins.lostcities;

import mcjty.lostcities.ForgeEventHandlers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ForgeEventHandlers.class)
public interface ForgeEventHandlersInvoker {

    @Invoker(value = "isValidSpawnBed", remap = false)
    boolean nischitweaker_isValidSpawnBed(World world, BlockPos pos);

    @Invoker(value = "findLocation", remap = false)
    BlockPos nischitweaker_findLocation(BlockPos bedLocation, WorldServer destWorld);
}
