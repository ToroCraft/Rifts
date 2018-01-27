package net.torocraft.rifts.dim;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.torocraft.rifts.world.RiftUtil;
import net.torocraft.torotraits.api.SpawnApi;

public class RiftTeleporter extends Teleporter {

  private final WorldServer world;
  private final int riftId;

  public RiftTeleporter(WorldServer worldIn, int riftId) {
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
    BlockPos pos = RiftUtil.getRiftCenter(riftId);

    int y = 90;
    BlockPos riftCenter = new BlockPos(pos.getX(), y, pos.getZ());

    entity.motionX = entity.motionY = entity.motionZ = 0.0D;

    if (entity instanceof EntityCreature) {
      SpawnApi.findAndSetSuitableSpawnLocation((EntityCreature) entity, riftCenter, 10);
    } else {
      positionEntity(entity, rotationYaw, riftCenter);
    }

    if (entity instanceof EntityPlayerMP) {
      positionPlayer((EntityPlayerMP) entity, rotationYaw);
    }

    return true;
  }

  private void positionEntity(Entity entity, float rotationYaw, BlockPos pos) {
    double x = (double) (pos).getX() + 0.5D;
    double y = (double) (pos).getY() + 0.5D;
    double z = (double) (pos).getZ() + 0.5D;
    entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
    entity.setPositionAndUpdate(x, y, z);
  }

  private void positionPlayer(EntityPlayerMP entity, float rotationYaw) {
    double x = entity.posX;
    double y = entity.posY;
    double z = entity.posZ;
    entity.setLocationAndAngles(x, y, z, rotationYaw, entity.rotationPitch);
    entity.setPositionAndUpdate(x, y, z);
  }

  @Override
  public boolean makePortal(Entity e) {
    return true;
  }

}