package net.torocraft.rifts;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RiftsConfig {

  private static final String CATEGORY = "Rifts Settings";
  private static Configuration config;
  private static final String[] DEFAULT_MOB_LIST = {
      "minecraft:zombie",
      "minecraft:zombie_pigman",
      "minecraft:zombie_villager",
      "minecraft:stray",
      "minecraft:husk",
      "minecraft:skeleton"
  };

  public static String[] MOB_WHITELIST = DEFAULT_MOB_LIST;

  public static void init(File configFile) {
    if (config == null) {
      config = new Configuration(configFile);
      loadConfiguration();
    }
  }

  private static void loadConfiguration() {
    try {

      MOB_WHITELIST =
          config.getStringList("MOB_WHITELIST", CATEGORY, DEFAULT_MOB_LIST,
              "Mobs that will be used to create nemeses. (Must extend EntityCreature)");

      config.save();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SubscribeEvent
  public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equalsIgnoreCase(Rifts.MODID)) {
      loadConfiguration();
    }
  }
}
