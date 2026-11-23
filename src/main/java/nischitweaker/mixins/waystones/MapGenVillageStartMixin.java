package nischitweaker.mixins.waystones;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.waystones.worldgen.ComponentVillageWaystone;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import nischitweaker.config.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(MapGenVillage.Start.class)
public abstract class MapGenVillageStartMixin {

    @ModifyExpressionValue(
            method = "<init>(Lnet/minecraft/world/World;Ljava/util/Random;III)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/StructureVillagePieces;getStructureVillageWeightedPieceList(Ljava/util/Random;I)Ljava/util/List;")
    )
    private List<StructureVillagePieces.PieceWeight> nischitweaker_waystonesVillageDimensionBlacklist(List<StructureVillagePieces.PieceWeight> pieces, @Local(argsOnly = true) World world) {
        if(ConfigHandler.waystones.villageDimensionBlacklist.contains(world.provider.getDimension()))
            pieces.removeIf(piece -> piece.villagePieceClass == ComponentVillageWaystone.class);
        return pieces;
    }
}
