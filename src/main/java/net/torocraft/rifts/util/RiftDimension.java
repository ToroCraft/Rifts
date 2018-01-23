package net.torocraft.rifts.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.torocraft.rifts.world.TeletoryWorldProvider;

public class RiftDimension {

	public static final int DIM_ID = 13;
	public static final DimensionType TYPE = DimensionType.register("torocraft_rifts", "_rifts", DIM_ID, TeletoryWorldProvider.class, true);

	public static void changeEntityDimension(final Entity entity) {
		System.out.println("changeEntityDimension");
		Timer.INSTANCE.addScheduledTask(new ChangeDimensionTask(entity));
	}

	public static void init() {
		DimensionManager.registerDimension(DIM_ID, TYPE);
	}

}
