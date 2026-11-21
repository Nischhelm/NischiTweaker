package nischitweaker.features.infestedbed;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/** Places both halves of the bed, like ItemBed */
public class ItemInfestedBed extends ItemBlock {

    public ItemInfestedBed(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        if (facing != EnumFacing.UP) return EnumActionResult.FAIL;

        boolean replaceable = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
        if (!replaceable) pos = pos.up();

        EnumFacing bedFacing = EnumFacing.byHorizontalIndex(MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3);
        BlockPos headPos = pos.offset(bedFacing);
        ItemStack stack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos, facing, stack) || !player.canPlayerEdit(headPos, facing, stack)) return EnumActionResult.FAIL;
        boolean footFree = replaceable || world.isAirBlock(pos);
        boolean headFree = world.getBlockState(headPos).getBlock().isReplaceable(world, headPos) || world.isAirBlock(headPos);
        if (!footFree || !headFree || !world.getBlockState(pos.down()).isTopSolid() || !world.getBlockState(headPos.down()).isTopSolid()) return EnumActionResult.FAIL;

        IBlockState foot = this.block.getDefaultState().withProperty(BlockBed.OCCUPIED, false).withProperty(BlockBed.FACING, bedFacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
        world.setBlockState(pos, foot, 10);
        world.setBlockState(headPos, foot.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
        SoundType sound = this.block.getSoundType(foot, world, pos, player);
        world.playSound(null, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        world.notifyNeighborsRespectDebug(pos, this.block, false);
        world.notifyNeighborsRespectDebug(headPos, this.block, false);

        if (!player.capabilities.isCreativeMode) stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
