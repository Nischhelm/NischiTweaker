package nischitweaker.features.antilure;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nischitweaker.config.ConfigHandler;

import javax.annotation.Nullable;
import java.util.List;

/** Melts the parasites around it when right clicked, then breaks without drops */
public class BlockAntiLure extends Block {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 15);

    public BlockAntiLure() {
        super(Material.ROCK);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TIER, 0));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (AntiLure.activate(world, pos, player, state.getValue(TIER))) world.setBlockToAir(pos);
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TIER);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TIER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TIER);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TIER);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab != this.getCreativeTab() && tab != CreativeTabs.SEARCH) return;
        for (int tier : AntiLure.getTiers()) items.add(new ItemStack(this, 1, tier));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tile.nischitweaker.anti_lure.tooltip.1", (int) ConfigHandler.srparasites.antiLure.radius));
        tooltip.add(TextFormatting.DARK_RED + I18n.format("tile.nischitweaker.anti_lure.tooltip.2"));
    }
}
