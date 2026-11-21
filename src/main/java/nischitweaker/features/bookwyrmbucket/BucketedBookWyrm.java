package nischitweaker.features.bookwyrmbucket;

import lykrast.defiledlands.common.entity.passive.EntityBookWyrm;
import lykrast.defiledlands.common.init.ModSounds;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

public class BucketedBookWyrm {
    public static Item ITEM;

    public static boolean isEnabled() {
        return Loader.isModLoaded("defiledlands") && ConfigHandler.defiledlands.bookwyrmBucket.bucketedBookWyrm;
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        ITEM = new ItemBucketedBookWyrm()
                .setRegistryName(new ResourceLocation(Tags.MODID, "bucketed_bookwyrm"))
                .setTranslationKey(Tags.MODID + ".bucketed_bookwyrm");
        event.getRegistry().register(ITEM);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeFeedBookWyrm().setRegistryName(new ResourceLocation(Tags.MODID, "bucketed_bookwyrm_feeding")));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        ModelLoader.setCustomModelResourceLocation(ITEM, 0, new ModelResourceLocation(ITEM.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof EntityBookWyrm)) return;
        EntityBookWyrm wyrm = (EntityBookWyrm) event.getTarget();
        if (wyrm.isDead || wyrm.isChild()) return;
        ItemStack bucket = event.getItemStack();
        if (!isEmptyBucket(bucket)) return;

        event.setCanceled(true);
        event.setCancellationResult(EnumActionResult.SUCCESS);
        if (event.getWorld().isRemote) return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack bucketed = capture(wyrm, bucket);
        if (bucketed.isEmpty()) return;
        wyrm.playSound(ModSounds.bookWyrmHurt, 1.0F, 1.0F);
        wyrm.setDead();

        if (!player.capabilities.isCreativeMode) bucket.shrink(1);
        if (bucket.isEmpty()) player.setHeldItem(event.getHand(), bucketed);
        else ItemHandlerHelper.giveItemToPlayer(player, bucketed);
    }

    private static ItemStack capture(EntityBookWyrm wyrm, ItemStack bucket) {
        wyrm.clearLeashed(true, true);
        wyrm.dismountRidingEntity();
        wyrm.removePassengers();

        NBTTagCompound data = new NBTTagCompound();
        if (!wyrm.writeToNBTAtomically(data)) return ItemStack.EMPTY;
        // Bucketed wyrms digest instantly, pending levels count as digested
        data.setInteger("Digested", data.getInteger("Digested") + data.getInteger("Digesting"));
        data.setInteger("Digesting", 0);
        data.setInteger("DigestTimer", 0);

        ItemStack bucketCopy = bucket.copy();
        bucketCopy.setCount(1);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(ItemBucketedBookWyrm.WYRM_KEY, data);
        tag.setTag(ItemBucketedBookWyrm.BUCKET_KEY, bucketCopy.writeToNBT(new NBTTagCompound()));

        ItemStack stack = new ItemStack(ITEM);
        stack.setTagCompound(tag);
        return stack;
    }

    private static boolean isEmptyBucket(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.hasTagCompound() && !stack.getTagCompound().isEmpty()) return false;
        return ConfigHandler.defiledlands.bookwyrmBucket.isBucketItem(stack);
    }
}
