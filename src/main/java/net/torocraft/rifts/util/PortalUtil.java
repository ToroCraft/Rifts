package net.torocraft.rifts.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.rifts.blocks.BlockRiftPortal;
import net.torocraft.rifts.dim.DimensionUtil;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;

public class PortalUtil {

  public static void enterRiftPortal(EntityPlayer player, BlockPos pos) {
    if (!(player instanceof EntityPlayerMP)) {
      return;
    }
    int riftId = RiftWorldSaveDataAccessor.findByPos(player.world, pos);
    DimensionUtil.travelToRift((EntityPlayerMP)player, riftId);
  }

  public static boolean openRiftPortal(EntityPlayer player, BlockPos pos, EnumFacing blockSide) {
    if (placeRiftPortalBlocks(player, pos, blockSide)) {
      RiftWorldSaveDataAccessor.createRift(player.world, pos);
      playSound(player, SoundEvents.ENTITY_LIGHTNING_IMPACT);
      return true;
    } else {
      playSound(player, SoundEvents.ENTITY_CREEPER_HURT);
      return false;
    }
  }

  private static boolean placeRiftPortalBlocks(EntityPlayer player, BlockPos pos, EnumFacing blockSide) {
    World world = player.world;

    if (player.dimension != 0) {
      return false;
    }

    if (!blockSide.equals(EnumFacing.UP)) {
      return false;
    }

    if (!isAir(world, pos)) {
      pos = pos.up();
    }

    boolean xAxis = playerIsFacingXAxis(player);

    List<BlockPos> positions = new ArrayList<>(9);
    positions.add(pos = xAxis ? pos.east() : pos.north());
    positions.add(pos = pos.up());
    positions.add(pos = pos.up());

    positions.add(pos = xAxis ? pos.west() : pos.south());
    positions.add(pos = pos.down());
    positions.add(pos = pos.down());

    positions.add(pos = xAxis ? pos.west() : pos.south());
    positions.add(pos = pos.up());
    positions.add(pos.up());

    for (BlockPos position : positions) {
      if (!isAir(world, position)) {
        return false;
      }
    }

    for (BlockPos position : positions) {
      world.setBlockState(position, getPortalBlock(xAxis ? Axis.X : Axis.Z));
    }

    return true;
  }

  private static boolean playerIsFacingXAxis(EntityPlayer player) {
    EnumFacing facing = player.getHorizontalFacing();
    return facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH);
  }

  private static IBlockState getPortalBlock(Axis axis) {
    return BlockRiftPortal.INSTANCE.getDefaultState().withProperty(BlockRiftPortal.AXIS, axis);
  }

  private static boolean isAir(World world, BlockPos pos) {
    return world.getBlockState(pos).getBlock() == Blocks.AIR;
  }

  private static void playSound(EntityPlayer player, SoundEvent sound) {
    player.world.playSound(null,
        player.posX, player.posY, player.posZ,
        sound,
        SoundCategory.PLAYERS,
        1.0F, 1.0F);
  }

}
