package net.torocraft.rifts;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.util.Timer;
import net.torocraft.rifts.world.RiftsWorldProvider;

@Mod(modid = Rifts.MODID, name = Rifts.MODNAME, version = Rifts.VERSION)
public class Rifts {

  public static final String MODID = "rifts";
  public static final String VERSION = "1.12.2-1";
  public static final String MODNAME = "Rifts";
  public static final int RIFT_DIM_ID = 13;
  public static final String NBT_RIFT_ID = "torocraft_rift_id";

  public static final DimensionType RIFT_DIM_TYPE = DimensionType
      .register("torocraft_rifts", "_rifts", RIFT_DIM_ID, RiftsWorldProvider.class, true);

  @SidedProxy(clientSide = "net.torocraft.rifts.ClientProxy")
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
