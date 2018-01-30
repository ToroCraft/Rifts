package net.torocraft.rifts.dim;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;
import net.torocraft.torotraits.api.SpawnLocationScanner;

public class LeaveRiftTeleporter extends Teleporter {

  private final WorldServer world;
  private final int riftId;

  public LeaveRiftTeleporter(WorldServer worldIn, int riftId) {
    super(worldIn);
    this.world = worldIn;
    this.riftId = riftId;
  }

  @Override
  public void placeInPortal(Entity entityIn, float rotationYaw) {
    placeInExistingPortal(entityIn, rotationYaw);
  }

  @Override
  public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
    RiftData riftData = RiftWorldSaveDataAccessor.loadRift(world, riftId);
    BlockPos pos = BlockPos.fromLong(riftData.portalLocation).up();
    positionEntity(entity, rotationYaw, pos);
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