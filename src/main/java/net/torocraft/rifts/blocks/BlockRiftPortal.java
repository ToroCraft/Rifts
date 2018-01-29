package net.torocraft.rifts.blocks;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.rifts.Rifts;

@EventBusSubscriber
@SuppressWarnings("deprecation")
public class BlockRiftPortal extends BlockBreakable {

  public static BlockRiftPortal INSTANCE;

  public static Item ITEM_INSTANCE;

  public static final String NAME = "rift_portal";

  public static ResourceLocation REGISTRY_NAME = new ResourceLocation(Rifts.MODID, NAME);

  public static final PropertyEnum<Axis> AXIS =
      PropertyEnum.create("axis", EnumFacing.Axis.class, Axis.X, Axis.Z);

  private static final AxisAlignedBB X_AABB =
      new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);

  private static final AxisAlignedBB Z_AABB =
      new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);

  private static final AxisAlignedBB Y_AABB =
      new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

  @SubscribeEvent
  public static void init(RegistryEvent.Register<Block> event) {
    INSTANCE = (BlockRiftPortal) new BlockRiftPortal().setUnlocalizedName(NAME);
    INSTANCE.setRegistryName(REGISTRY_NAME);
    event.getRegistry().register(INSTANCE);
  }

  @SubscribeEvent
  public static void initItem(RegistryEvent.Register<Item> event) {
    ITEM_INSTANCE = new ItemBlock(INSTANCE);
    ITEM_INSTANCE.setRegistryName(REGISTRY_NAME);
    event.getRegistry().register(ITEM_INSTANCE);
  }

  public static void registerRenders() {
    ModelResourceLocation model = new ModelResourceLocation(Rifts.MODID + ":" + NAME, "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_INSTANCE, 0, model);
  }

  public BlockRiftPortal() {
    super(Material.PORTAL, false);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
  }

  protected void onPlayerEnterPortal(EntityPlayerMP player, BlockPos pos) {
    // TODO good stuff here
    System.out.println("onPlayerEnterPortal " + pos);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch (state.getValue(AXIS)) {
      case X:
        return X_AABB;
      case Y:
      default:
        return Y_AABB;
      case Z:
        return Z_AABB;
    }
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn,
      BlockPos pos) {
    return NULL_AABB;
  }

  public static int getMetaForAxis(EnumFacing.Axis axis) {
    return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess,
      BlockPos pos, EnumFacing side) {
    pos = pos.offset(side);
    EnumFacing.Axis axis = null;

    if (blockState.getBlock() == this) {
      axis = blockState.getValue(AXIS);

      if (axis == EnumFacing.Axis.Z && side != EnumFacing.EAST
          && side != EnumFacing.WEST) {
        return false;
      }

      if (axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH
          && side != EnumFacing.NORTH) {
        return false;
      }
    }

    boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this
        && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
    boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this
        && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
    boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this
        && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
    boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this
        && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
    boolean flag4 = flag || flag1 || axis == EnumFacing.Axis.X;
    boolean flag5 = flag2 || flag3 || axis == EnumFacing.Axis.Z;

    return flag4 && side == EnumFacing.WEST || flag4 && side == EnumFacing.EAST
        || flag5 && side == EnumFacing.NORTH || flag5 && side == EnumFacing.SOUTH;
  }

  @Override
  public int quantityDropped(Random random) {
    return 0;
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state,
      Entity entity) {

    if (world.isRemote || entity.getRidingEntity() != null || entity.isBeingRidden()) {
      return;
    }

    if (entity.timeUntilPortal > 0) {
      entity.timeUntilPortal = 10;
    } else if (entity instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP) entity;
      onPlayerEnterPortal(player, pos);
      player.timeUntilPortal = 10;
    }
  }

  @Override
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return ItemStack.EMPTY;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? Axis.Z : Axis.X);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    for (int i = 0; i < 4; ++i) {
      double d0 = (double) ((float) pos.getX() + rand.nextFloat());
      double d1 = (double) ((float) pos.getY() + rand.nextFloat());
      double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
      double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
      double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
      double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
      int j = rand.nextInt(2) * 2 - 1;

      if (worldIn.getBlockState(pos.west()).getBlock() != this
          && worldIn.getBlockState(pos.east()).getBlock() != this) {
        d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
        d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
      } else {
        d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
        d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
      }

      worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, 0);
    }
  }

  /**
   * Convert the BlockState into the correct metadata value
   */
  @Override
  public int getMetaFromState(IBlockState state) {
    return getMetaForAxis(state.getValue(AXIS));
  }

  /**
   * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable,
   * returns the passed blockstate.
   */

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case COUNTERCLOCKWISE_90:
      case CLOCKWISE_90:
        switch (state.getValue(AXIS)) {
          case X:
            return state.withProperty(AXIS, EnumFacing.Axis.Z);
          case Z:
            return state.withProperty(AXIS, EnumFacing.Axis.X);
          default:
            return state;
        }
      default:
        return state;
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, AXIS);
  }

}