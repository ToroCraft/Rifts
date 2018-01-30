package net.torocraft.rifts.dim;

import java.lang.reflect.Field;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.util.Timer;

public class DimensionUtil {

  public static final int OVERWORLD_DIM_ID = 0;

  public static void travelToRift(EntityPlayerMP player, int riftId) {
    Teleporter t = new EnterRiftTeleporter(getWorld(player, Rifts.RIFT_DIM_ID), riftId);
    Timer.INSTANCE.addScheduledTask(() -> changePlayerDimension(player, Rifts.RIFT_DIM_ID, t));
  }

  public static void travelToOverworld(EntityPlayerMP player, int riftId) {
    Teleporter t = new LeaveRiftTeleporter(getWorld(player, OVERWORLD_DIM_ID), riftId);
    Timer.INSTANCE.addScheduledTask(() -> changePlayerDimension(player, OVERWORLD_DIM_ID, t));
  }

  private static WorldServer getWorld(EntityPlayerMP player, int dimId) {
    return player.mcServer.getWorld(dimId);
  }

  private static void changePlayerDimension(EntityPlayerMP player, int dimId, Teleporter teleporter) {
    if (!ForgeHooks.onTravelToDimension(player, dimId)) {
      return;
    }
    setInvulnerableDimensionChange(player, true);
    player.timeUntilPortal = 10;
    player.mcServer.getPlayerList().transferPlayerToDimension(player, dimId, teleporter);
    resetStatusFields(player);
    setInvulnerableDimensionChange(player, false);
  }

  private static void resetStatusFields(EntityPlayerMP player) {
    try {
      Field lastExperience = getReflectionField("field_71144_ck", "lastExperience");
      Field lastHealth = getReflectionField("field_71149_ch", "lastHealth");
      Field lastFoodLevel = getReflectionField("field_71146_ci", "lastFoodLevel");
      lastExperience.setInt(player, -1);
      lastHealth.setFloat(player, -1.0F);
      lastFoodLevel.setInt(player, -1);
    } catch (Exception e) {
      throw new RuntimeException("Unable to set reset status fields via reflection", e);
    }
  }

  private static void setInvulnerableDimensionChange(EntityPlayerMP thePlayer, boolean enable) {
    try {
      Field invulnerableDimensionChange = getReflectionField("field_184851_cj",
          "invulnerableDimensionChange");
      invulnerableDimensionChange.setBoolean(thePlayer, enable);
    } catch (Exception e) {
      throw new RuntimeException("Unable to set invulnerableDimensionChange via reflection", e);
    }
  }

  private static Field getReflectionField(String... names) {
    Field f;
    for (String name : names) {
      f = getFieldFromPlayer(name);
      if (f != null) {
        f.setAccessible(true);
        return f;
      }
    }
    throw new RuntimeException(
        join(names, ", ") + " field not found in " + EntityPlayerMP.class.getName());
  }

  private static Field getFieldFromPlayer(String name) {
    try {
      return EntityPlayerMP.class.getDeclaredField(name);
    } catch (Exception e) {
      return null;
    }
  }

  private static String join(String[] aArr, String sSep) {
    StringBuilder sbStr = new StringBuilder();
    for (int i = 0, il = aArr.length; i < il; i++) {
      if (i > 0) {
        sbStr.append(sSep);
      }
      sbStr.append(aArr[i]);
    }
    return sbStr.toString();
  }

}
