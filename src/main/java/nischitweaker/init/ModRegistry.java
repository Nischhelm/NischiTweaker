package nischitweaker.init;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nischitweaker.features.GreaterXPTome;
import nischitweaker.features.bookwyrmbucket.BucketedBookWyrm;

@Mod.EventBusSubscriber
public class ModRegistry {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (BucketedBookWyrm.isEnabled()) BucketedBookWyrm.registerItems(event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (BucketedBookWyrm.isEnabled()) BucketedBookWyrm.registerRecipes(event);
        if (GreaterXPTome.isEnabled()) GreaterXPTome.registerRecipes(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        if (BucketedBookWyrm.isEnabled()) BucketedBookWyrm.registerModels();
    }
}
