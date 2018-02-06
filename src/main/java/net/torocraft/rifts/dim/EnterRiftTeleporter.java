package net.torocraft.rifts.dim;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.network.MessageSetRift;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;
import net.torocraft.torotraits.api.SpawnLocationScanner;

public class EnterRiftTeleporter extends Teleporter {

  private final WorldServer world;
  private final int riftId;
  private RiftData data;

  public EnterRiftTeleporter(WorldServer worldIn, int riftId) {
    super(worldIn);
    this.world = worldIn;
    this.riftId = riftId;
    data = RiftWorldSaveDataAccessor.loadRift(world, riftId);
  }

  @Override
  public void placeInPortal(Entity entityIn, float rotationYaw) {
    placeInExistingPortal(entityIn, rotationYaw);
  }

  @Override
  public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
    BlockPos pos = RiftUtil.getRiftCenter(riftId);

    int y = 90;
    BlockPos riftCenter = new BlockPos(pos.getX(), y, pos.getZ());

    SpawnLocationScanner scanner = new SpawnLocationScanner(world, entity, riftCenter);
    BlockPos spawnLocation = scanner.areaScan(20, world.getActualHeight(), 20);

    if (spawnLocation == null) {
      spawnLocation = pos;
    }

    positionEntity(entity, rotationYaw, spawnLocation);

    if (entity instanceof EntityPlayerMP) {
      Rifts.NETWORK.sendTo(new MessageSetRift(data), (EntityPlayerMP) entity);
    }

    return true;
  }

  private void positionEntity(Entity entity, float rotationYaw, BlockPos pos) {
    double x = (double) pos.getX() + 0.5D;
    double y = (double) pos.getY() + 0.5D;
    double z = (double) pos.getZ() + 0.5D;
    entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    if (entity instanceof EntityPlayerMP) {
      ((EntityPlayerMP) entity).connection
          .setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
    }
    entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
    entity.setPositionAndUpdate(x, y, z);
  }

  @Override
  public boolean makePortal(Entity e) {
    return true;
  }

}