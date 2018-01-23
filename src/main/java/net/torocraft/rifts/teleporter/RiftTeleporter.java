package net.torocraft.rifts.teleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class RiftTeleporter extends Teleporter {

	private final WorldServer world;

	public RiftTeleporter(WorldServer worldIn) {
		super(worldIn);
		this.world = worldIn;
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
		placeInExistingPortal(entityIn, rotationYaw);
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
		positionEntity(entity, rotationYaw, new BlockPos(0, 50, 0));
		return true;
	}

	private void positionEntity(Entity entity, float rotationYaw, BlockPos pos) {
		double x = (double) (pos).getX() + 0.5D;
		double y = (double) (pos).getY() + 0.5D;
		double z = (double) (pos).getZ() + 0.5D;
		entity.motionX = entity.motionY = entity.motionZ = 0.0D;
		if (entity instanceof EntityPlayerMP) {
			((EntityPlayerMP) entity).connection.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
		}
		entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
		entity.setPositionAndUpdate(x, y, z);
	}

	@Override
	public boolean makePortal(Entity e) {
		return true;
	}

}