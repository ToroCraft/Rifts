package net.torocraft.rifts;

import java.io.File;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.rifts.entities.Entities;
import net.torocraft.rifts.network.MessageRiftUpdate;
import net.torocraft.rifts.network.MessageSetRift;
import net.torocraft.rifts.save.data.RiftData;

public abstract class CommonProxy {

  public void preInit(FMLPreInitializationEvent e) {
    initConfig(e.getSuggestedConfigurationFile());
    Entities.init();
  }

  public void init(FMLInitializationEvent e) {
    DimensionManager.registerDimension(Rifts.RIFT_DIM_ID, Rifts.RIFT_DIM_TYPE);
    int messageId = 0;
    MessageRiftUpdate.init(messageId++);
    MessageSetRift.init(messageId);
  }

  public void postInit(FMLPostInitializationEvent e) {

  }

  private void initConfig(File configFile) {
    RiftsConfig.init(configFile);
    MinecraftForge.EVENT_BUS.register(new RiftsConfig());
  }

  public void updateRiftOnClient(RiftData data) {

  }
}
