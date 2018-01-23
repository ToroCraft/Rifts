package net.torocraft.rifts;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.rifts.util.RiftDimension;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
	}

	public void init(FMLInitializationEvent e) {
		RiftDimension riftDimension = new RiftDimension();
		MinecraftForge.EVENT_BUS.register(riftDimension);
		RiftDimension.init();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
