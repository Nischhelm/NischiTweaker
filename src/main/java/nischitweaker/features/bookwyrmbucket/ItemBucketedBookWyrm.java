package nischitweaker.features.bookwyrmbucket;

import lykrast.defiledlands.common.init.ModItems;
import lykrast.defiledlands.common.init.ModSounds;
import lykrast.defiledlands.common.util.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nischitweaker.config.ConfigHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemBucketedBookWyrm extends Item {
    // The bucket the wyrm was caught with, returned on release
    public static final String BUCKET_KEY = "Bucket";
    // Full entity nbt of the caught wyrm. Stats and digestion progress are read from and written to it directly
    public static final String WYRM_KEY = "Wyrm";
    public static final String COOLDOWN_END_KEY = "CooldownEnd";
    public static final String COOLDOWN_LENGTH_KEY = "CooldownLength";

    public ItemBucketedBookWyrm() {
        this.setMaxStackSize(1);
        // No lambda, apply is client only and gets stripped on servers
        this.addPropertyOverride(new ResourceLocation("golden"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return isGolden(stack) ? 1 : 0;
            }
        });
    }

    // ---------- Data access ----------

    public static NBTTagCompound getWyrmData(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? new NBTTagCompound() : tag.getCompoundTag(WYRM_KEY);
    }

    public static boolean isGolden(ItemStack stack) {
        return getWyrmData(stack).getBoolean("Golden");
    }

    public static int getMaxLevel(ItemStack stack) {
        return Math.max(1, getWyrmData(stack).getInteger("MaxLvl"));
    }

    public static int getDigestTime(ItemStack stack) {
        return Math.max(1, getWyrmData(stack).getInteger("Digest"));
    }

    public static int getDigested(ItemStack stack) {
        return getWyrmData(stack).getInteger("Digested");
    }

    public static boolean isOnCooldown(ItemStack stack, long worldTime) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return false;
        return tag.getLong(COOLDOWN_END_KEY) > worldTime;
    }

    /** Same value a Book Wyrm entity assigns to a fed book */
    public static int getBookValue(ItemStack book) {
        if (book.getItem() != Items.ENCHANTED_BOOK) return 0;
        int value = 0;
        for (Map.Entry<Enchantment, Integer> e : EnchantmentHelper.getEnchantments(book).entrySet())
            value += e.getKey().getMinEnchantability(e.getValue());
        return (int) (value * Config.conversionRate);
    }

    // ---------- Digestion ----------

    /** Copy of the wyrm with the fed value added to its digested levels */
    public static ItemStack feed(ItemStack wyrmStack, int fedValue) {
        ItemStack wyrm = wyrmStack.copy();
        NBTTagCompound tag = wyrm.getTagCompound();
        if (tag == null) wyrm.setTagCompound(tag = new NBTTagCompound());
        NBTTagCompound data = tag.getCompoundTag(WYRM_KEY);
        tag.setTag(WYRM_KEY, data);
        data.setInteger("Digested", data.getInteger("Digested") + fedValue);
        return wyrm;
    }

    /** Burps once (like the entity does every maxLevel digested levels) if off cooldown, books go to the player */
    private static void tryBurp(ItemStack stack, World world, EntityPlayer player) {
        long now = world.getTotalWorldTime();
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || isOnCooldown(stack, now)) return;
        NBTTagCompound data = tag.getCompoundTag(WYRM_KEY);
        int maxLevel = getMaxLevel(stack);
        int digested = data.getInteger("Digested");
        if (digested < maxLevel) return;

        data.setInteger("Digested", digested - maxLevel);
        for (ItemStack book : burp(world.rand, maxLevel, data.getBoolean("Golden")))
            if (!player.inventory.addItemStackToInventory(book)) player.dropItem(book, false);

        int length = (int) (maxLevel * getDigestTime(stack) * ConfigHandler.defiledlands.bookwyrmBucket.cooldownMultiplier);
        tag.setLong(COOLDOWN_END_KEY, now + length);
        tag.setInteger(COOLDOWN_LENGTH_KEY, length);

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    /** Copy of EntityBookWyrm book generation */
    private static List<ItemStack> burp(Random rand, int maxLevel, boolean golden) {
        List<ItemStack> books = new ArrayList<>();
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, new ItemStack(Items.BOOK), maxLevel, golden);
        list.sort(Comparator.comparingInt(e -> -e.enchantment.getMinEnchantability(e.enchantmentLevel)));
        int remaining = (int) (maxLevel / Config.conversionRate);
        for (EnchantmentData e : list) {
            int value = (int) (e.enchantment.getMinEnchantability(e.enchantmentLevel) * Config.conversionRate);
            if (remaining >= value) {
                remaining -= value;
                ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantedBook.addEnchantment(book, e);
                books.add(book);
                if (remaining <= 0) break;
            }
        }
        return books;
    }

    @SideOnly(Side.CLIENT)
    public static boolean showStats() {
        switch (ConfigHandler.defiledlands.bookwyrmBucket.statsTooltip) {
            case ALWAYS: return true;
            case NEVER: return false;
            default:
                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player == null) return false;
                for (ItemStack stack : player.inventory.mainInventory) if (stack.getItem() == ModItems.bookWyrmAnalyzer) return true;
                for (ItemStack stack : player.inventory.offHandInventory) if (stack.getItem() == ModItems.bookWyrmAnalyzer) return true;
                return false;
        }
    }

    // Eating sound, same as feeding the entity
    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return;
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (world.isRemote || !(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        // Config chance is per minute, digesting wyrms (on cooldown or with a pending burp) stay put
        boolean digesting = isOnCooldown(stack, world.getTotalWorldTime()) || getDigested(stack) >= getMaxLevel(stack);
        if (!digesting && world.rand.nextFloat() < ConfigHandler.defiledlands.bookwyrmBucket.escapeChance / 1200F && escape(stack, world, player)) return;
        tryBurp(stack, world, player);
    }

    // ---------- Release ----------

    @Nullable
    private static Entity release(ItemStack stack, World world, double x, double y, double z) {
        NBTTagCompound data = getWyrmData(stack).copy();
        data.removeTag("UUIDMost");
        data.removeTag("UUIDLeast");
        // Entity burps every tick while Digested >= MaxLvl, so hand the backlog back as pending levels to keep its pacing
        int backlog = data.getInteger("Digested");
        if (backlog > 0) {
            data.setInteger("Digesting", data.getInteger("Digesting") + backlog);
            data.setInteger("Digested", 0);
            data.setInteger("DigestTimer", Math.max(1, data.getInteger("Digest")));
        }
        Entity wyrm = EntityList.createEntityFromNBT(data, world);
        if (wyrm == null) return null;
        wyrm.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
        world.spawnEntity(wyrm);
        wyrm.playSound(ModSounds.bookWyrmIdle, 1.0F, 1.0F);
        return wyrm;
    }

    private static ItemStack getBucket(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(BUCKET_KEY) ? new ItemStack(tag.getCompoundTag(BUCKET_KEY)) : ItemStack.EMPTY;
    }

    /** Wyrm hops out of the bucket next to the player, the bucket stays in the inventory slot */
    private static boolean escape(ItemStack stack, World world, EntityPlayer player) {
        for (NonNullList<ItemStack> list : Arrays.asList(player.inventory.mainInventory, player.inventory.offHandInventory)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != stack) continue;
                if (release(stack, world, player.posX, player.posY, player.posZ) == null) return false;
                list.set(i, getBucket(stack));
                player.sendStatusMessage(new TextComponentTranslation("item.nischitweaker.bucketed_bookwyrm.escaped"), true);
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) return EnumActionResult.PASS;
        if (world.isRemote) return EnumActionResult.SUCCESS;

        ItemStack stack = player.getHeldItem(hand);
        BlockPos spawnPos = pos.offset(facing);
        if (release(stack, world, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5) == null) return EnumActionResult.FAIL;
        player.setHeldItem(hand, getBucket(stack));
        return EnumActionResult.SUCCESS;
    }

    // ---------- Display ----------

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + (isGolden(stack) ? "_golden" : "");
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        String name = super.getItemStackDisplayName(stack);
        String wyrmName = getWyrmData(stack).getString("CustomName");
        return wyrmName.isEmpty() ? name : name + " - " + TextFormatting.ITALIC + wyrmName + TextFormatting.RESET;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (showStats()) {
            String base = "ui.defiledlands.book_wyrm_analyze.";
            tooltip.add(TextFormatting.GRAY + I18n.format(base + "max_level", getMaxLevel(stack)));
            tooltip.add(TextFormatting.GRAY + I18n.format(base + "digest_time", getDigestTime(stack)));
            tooltip.add(TextFormatting.GRAY + I18n.format(base + "digested", getDigested(stack)));
            if (isGolden(stack)) tooltip.add(TextFormatting.GOLD + I18n.format(base + "golden"));
        }
        long remaining = getCooldownRemaining(stack);
        if (remaining > 0) tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("item.nischitweaker.bucketed_bookwyrm.cooldown", (remaining + 19) / 20));
        tooltip.add(TextFormatting.WHITE + I18n.format("item.nischitweaker.bucketed_bookwyrm.tooltip"));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getCooldownRemaining(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        int length = tag == null ? 0 : tag.getInteger(COOLDOWN_LENGTH_KEY);
        return length <= 0 ? 0 : (double) getCooldownRemaining(stack) / length;
    }

    private static long getCooldownRemaining(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey(COOLDOWN_END_KEY)) return 0;
        if (FMLCommonHandler.instance().getSide() != Side.CLIENT) return 0;
        World world = Minecraft.getMinecraft().world;
        return world == null ? 0 : Math.max(0, tag.getLong(COOLDOWN_END_KEY) - world.getTotalWorldTime());
    }
}
