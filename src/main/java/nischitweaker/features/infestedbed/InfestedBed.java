package nischitweaker.features.infestedbed;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.Loader;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

public class InfestedBed {
    public static Block BLOCK;
    public static Item ITEM;

    public static boolean isEnabled() {
        return Loader.isModLoaded("lostcities") && ConfigHandler.lostcities.infestedPortalBed;
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BLOCK = new BlockInfestedBed()
                .setRegistryName(new ResourceLocation(Tags.MODID, "infested_bed"))
                .setTranslationKey(Tags.MODID + ".infested_bed");
        event.getRegistry().register(BLOCK);
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        ITEM = new ItemInfestedBed(BLOCK)
                .setRegistryName(BLOCK.getRegistryName())
                .setTranslationKey(Tags.MODID + ".infested_bed");
        event.getRegistry().register(ITEM);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        ModelLoader.setCustomStateMapper(BLOCK, new StateMap.Builder().ignore(BlockBed.OCCUPIED).build());
        ModelLoader.setCustomModelResourceLocation(ITEM, 0, new ModelResourceLocation(ITEM.getRegistryName(), "inventory"));
    }
}
