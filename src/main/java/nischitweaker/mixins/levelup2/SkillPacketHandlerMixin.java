package nischitweaker.mixins.levelup2;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.netty.buffer.ByteBuf;
import levelup2.api.IPlayerSkill;
import levelup2.config.LevelUpConfig;
import levelup2.network.SkillPacketHandler;
import levelup2.player.IPlayerClass;
import levelup2.skills.SkillRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import nischitweaker.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(SkillPacketHandler.class)
public abstract class SkillPacketHandlerMixin {

    @WrapMethod(method = "handlePacket", remap = false)
    private void nischitweaker_validateSkillChange(ByteBuf buf, EntityPlayer player, Operation<Void> original) {
        if (player.world.isRemote) {
            original.call(buf, player);
            return;
        }

        byte button = buf.readByte();
        buf.readInt(); //client calculated level cost, ignored
        if (button != -1) return; //original only removed levels in this case

        Map<String, Integer> requested = new HashMap<>();
        int skillCount = SkillRegistry.getSkillRegistry().size();
        for (int i = 0; i < skillCount; i++)
            requested.put(ByteBufUtils.readUTF8String(buf), buf.readInt());

        IPlayerClass properties = SkillRegistry.getPlayer(player);
        long cost = 0;
        for (Map.Entry<String, Integer> entry : requested.entrySet()) {
            IPlayerSkill skill = SkillRegistry.getSkillFromName(entry.getKey());
            if (skill == null) {
                nischitweaker$reject(player, "unknown skill " + entry.getKey());
                return;
            }
            int current = properties.getSkillLevel(entry.getKey());
            int target = entry.getValue();
            if (target == current) continue;
            if (target < current || target > skill.getMaxLevel() || !skill.isEnabled()) {
                nischitweaker$reject(player, "invalid level " + target + " for " + entry.getKey());
                return;
            }
            for (int level = current; level < target; level++) {
                int levelCost = skill.getLevelCost(level);
                if (levelCost < 0) {
                    nischitweaker$reject(player, "locked skill " + entry.getKey());
                    return;
                }
                cost += levelCost;
            }
            if (skill.getPrerequisites() != null) {
                for (String prerequisite : skill.getPrerequisites()) {
                    IPlayerSkill check = SkillRegistry.getSkillFromName(prerequisite);
                    if (check != null && check.isEnabled() && requested.getOrDefault(prerequisite, properties.getSkillLevel(prerequisite)) == 0) {
                        nischitweaker$reject(player, "missing prerequisite " + prerequisite + " for " + entry.getKey());
                        return;
                    }
                }
            }
        }

        if (cost > player.experienceLevel) {
            nischitweaker$reject(player, "not enough levels");
            return;
        }

        for (Map.Entry<String, Integer> entry : requested.entrySet())
            properties.setSkillLevel(entry.getKey(), entry.getValue());
        SkillRegistry.loadPlayer(player);
        if (cost > 0) player.addExperienceLevel((int) -cost);
    }

    @WrapMethod(method = "handleClassChange", remap = false)
    private void nischitweaker_validateClassChange(byte newClass, boolean reclass, EntityPlayerMP player, Operation<Void> original) {
        IPlayerClass properties = SkillRegistry.getPlayer(player);
        if (newClass < 0 || newClass > 2 || properties.getSpecialization() == newClass) return;

        boolean hasClass = properties.hasClass();
        if (hasClass) {
            if (!nischitweaker$isHoldingRespecBook(player) || player.experienceLevel < LevelUpConfig.reclassCost) {
                nischitweaker$reject(player, "class change without respec book or levels");
                return;
            }
        } else if (player.experienceLevel < 5) {
            nischitweaker$reject(player, "class selection without enough levels");
            return;
        }

        original.call(newClass, hasClass, player);
    }

    @Unique
    private static boolean nischitweaker$isHoldingRespecBook(EntityPlayer player) {
        for (ItemStack stack : player.getHeldEquipment())
            if (stack.getItem() == SkillRegistry.respecBook) return true;
        return false;
    }

    @Unique
    private static void nischitweaker$reject(EntityPlayer player, String reason) {
        Tags.LOGGER.warn("Rejected Level Up! skill packet from {}: {}", player.getName(), reason);
        SkillRegistry.loadPlayer(player);
    }
}
