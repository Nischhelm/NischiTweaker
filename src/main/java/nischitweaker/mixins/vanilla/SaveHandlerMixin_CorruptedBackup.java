package nischitweaker.mixins.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import nischitweaker.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(SaveHandler.class)
public abstract class SaveHandlerMixin_CorruptedBackup {

    // If level.dat and level.dat_old both fail to load, a fresh level.dat gets created and the first save rotates the corrupted ones away -> copy them aside first
    @WrapOperation(
            method = "loadWorldInfo",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/SaveFormatOld;loadAndFix(Ljava/io/File;Lnet/minecraft/util/datafix/DataFixer;Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;", remap = false)
    )
    private WorldInfo nischitweaker_backupCorruptedLevelDat(File file, DataFixer fixer, SaveHandler save, Operation<WorldInfo> original) {
        WorldInfo info = original.call(file, fixer, save);
        if (info == null && file.exists()) {
            // Named after the file's last modification so repeated load attempts of the same corrupted file don't create duplicates
            String stamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(file.lastModified()));
            File backup = new File(file.getParentFile(), file.getName() + "_corrupted_" + stamp);
            if (!backup.exists()) {
                try {
                    Files.copy(file.toPath(), backup.toPath());
                    Tags.LOGGER.warn("Failed to read {}, kept a copy as {}", file, backup.getName());
                } catch (IOException e) {
                    Tags.LOGGER.error("Failed to back up corrupted {}", file, e);
                }
            }
        }
        return info;
    }
}
