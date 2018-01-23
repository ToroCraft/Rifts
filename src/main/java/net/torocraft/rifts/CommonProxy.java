package net.torocraft.rifts;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
	}

	public void init(FMLInitializationEvent e) {
		DimensionManager.registerDimension(Rifts.RIFT_DIM_ID, Rifts.RIFT_DIM_TYPE);
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
