package net.torocraft.rifts.dim;

import java.lang.reflect.Field;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.network.MessageRiftUpdate;
import net.torocraft.rifts.network.MessageSetRift;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.util.Timer;
import net.torocraft.rifts.world.RiftUtil;

public class DimensionUtil {

  public static final int OVERWORLD_DIM_ID = 0;

  public static void travelToRift(EntityPlayerMP player, int riftId) {
    Teleporter t = new EnterRiftTeleporter(getWorld(player, Rifts.RIFT_DIM_ID), riftId);
    Timer.INSTANCE.addScheduledTask(() -> {
      changePlayerDimension(player, Rifts.RIFT_DIM_ID, t);
      playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    });
  }

  public static void travelToOverworld(EntityPlayerMP player, int riftId) {
    Teleporter t = new LeaveRiftTeleporter(getWorld(player, OVERWORLD_DIM_ID), riftId);
    Timer.INSTANCE.addScheduledTask(() -> {
      changePlayerDimension(player, OVERWORLD_DIM_ID, t);
      clearRiftDataOnClient(player);
      playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    });
  }

  private static void clearRiftDataOnClient(EntityPlayerMP player) {
    Rifts.NETWORK.sendTo(new MessageSetRift(null), player);
  }

  private static WorldServer getWorld(EntityPlayerMP player, int dimId) {
    return player.mcServer.getWorld(dimId);
  }

  private static void changePlayerDimension(EntityPlayerMP player, int dimId,
      Teleporter teleporter) {
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

  public static boolean isRiftTick(PlayerTickEvent e) {
    return !e.player.world.isRemote &&
        Phase.END.equals(e.phase) &&
        onlyInRift(e) &&
        onlyEveryFiveSeconds(e);
  }

  public static boolean onlyInRift(PlayerTickEvent e) {
    return e.player.dimension == Rifts.RIFT_DIM_ID;
  }

  public static boolean onlyEveryFiveSeconds(PlayerTickEvent event) {
    return event.player.world.getTotalWorldTime() % 100 == 0;
  }

  public static void syncPlayers(EntityPlayer player, RiftData data) {
    TargetPoint point = new TargetPoint(
        Rifts.RIFT_DIM_ID,
        player.posX, player.posY, player.posZ,
        RiftUtil.RIFT_DISTANCE
    );
    Rifts.NETWORK.sendToAllAround(new MessageRiftUpdate(data), point);
  }

  private static void playSound(EntityPlayer player, SoundEvent sound) {
    player.world.playSound(null,
        player.posX, player.posY, player.posZ,
        sound,
        SoundCategory.PLAYERS,
        1.0F, 1.0F);
  }
}
