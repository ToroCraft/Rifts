package net.torocraft.rifts.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

class ChangeDimensionTask implements Runnable {

	private final Entity entity;

	public ChangeDimensionTask(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void run() {
		System.out.println("change dim running");
		if (entity == null) {
			return;
		}


		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			int nextDimension = Util.getNextDimension(player);
			Util.changePlayerDimension(player, nextDimension);
		} else {
			// TODO: support teleporting non-players
			// wip_fakeTeletportNonPlayer(entity);
		}
	}

}
