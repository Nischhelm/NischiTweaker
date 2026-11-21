package nischitweaker.features.infestedbed;

import mcjty.lostcities.ForgeEventHandlers;
import mcjty.lostcities.config.LostCityConfiguration;
import mcjty.lostcities.varia.CustomTeleporter;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nischitweaker.util.LostCitiesHelper;
import nischitweaker.mixins.lostcities.ForgeEventHandlersInvoker;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Bed without tile entity that teleports to/from the Lost Cities when right clicked.
 * Uses the Lost Cities portal checks (special bed block and skulls) but skips sleeping, so it also works in the End and ignores the portal phase lock.
 */
public class BlockInfestedBed extends BlockBed {
    private static ForgeEventHandlersInvoker lostCitiesHandler;

    public BlockInfestedBed() {
        super();
        this.hasTileEntity = false;
        this.setSoundType(SoundType.WOOD);
        this.setHardness(0.2F);
        this.disableStats();
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    private static ForgeEventHandlersInvoker getLostCitiesHandler() {
        if (lostCitiesHandler == null) lostCitiesHandler = (ForgeEventHandlersInvoker) new ForgeEventHandlers();
        return lostCitiesHandler;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (state.getValue(PART) != EnumPartType.HEAD) {
            pos = pos.offset(state.getValue(FACING));
            if (world.getBlockState(pos).getBlock() != this) return true;
        }
        if (LostCityConfiguration.DIMENSION_ID == -1) return true;

        ForgeEventHandlersInvoker handler = getLostCitiesHandler();
        if (!handler.nischitweaker_isValidSpawnBed(world, pos)) {
            player.sendStatusMessage(new TextComponentTranslation("nischitweaker.msg.infestedbed.incomplete").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
            return true;
        }

        int destDim = LostCitiesHelper.isLostCities(world.provider.getDimension()) ? 0 : LostCityConfiguration.DIMENSION_ID;
        WorldServer destWorld = world.getMinecraftServer().getWorld(destDim);
        BlockPos dest = handler.nischitweaker_findLocation(pos, destWorld);
        double x = dest.getX() + 0.5, y = dest.getY(), z = dest.getZ() + 0.5;
        // Not CustomTeleporter.teleportToDimension: when leaving the End it spawns the player a second time,
        // which Forge already did for non-vanilla teleporters, leaving the player entity marked dead in the new world
        player.changeDimension(destDim, new CustomTeleporter(destWorld, x, y, z));
        player.setPositionAndUpdate(x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 2; i++)
            world.spawnParticle(EnumParticleTypes.PORTAL,
                    pos.getX() + rand.nextDouble(), pos.getY() + 0.6D, pos.getZ() + rand.nextDouble(),
                    (rand.nextDouble() - 0.5D) * 0.5D, rand.nextDouble() * 0.5D, (rand.nextDouble() - 0.5D) * 0.5D);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {}

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(InfestedBed.ITEM);
    }
}
