package net.torocraft.rifts;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.torocraft.rifts.util.Timer;

@Mod(modid = Rifts.MODID, name = Rifts.MODNAME, version = Rifts.VERSION)
public class Rifts {

	public static final String MODID = "torocraft-rifts";
	public static final String VERSION = "1.12.2-1";
	public static final String MODNAME = "Rifts";

	@SidedProxy(clientSide = "net.torocraft.rifts.ClientProxy", serverSide = "net.torocraft.rifts.ServerProxy")
	public static CommonProxy proxy;

	@Instance(value = Rifts.MODID)
	public static Rifts instance;

	public static MinecraftServer SERVER;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {
		SERVER = e.getServer();
		MinecraftForge.EVENT_BUS.register(Timer.INSTANCE);
		e.registerServerCommand(new Commands());
	}
}
