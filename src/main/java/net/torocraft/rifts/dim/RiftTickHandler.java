package net.torocraft.rifts.dim;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;

@EventBusSubscriber
public class RiftTickHandler {

  @SubscribeEvent
  public static void onPlayerTick(PlayerTickEvent event) {
    if (DimensionUtil.isRiftTick(event)) {
      RiftData data = loadPlayerRift(event.player);
      if (data == null || !data.active) {
        return;
      }
      data.time++;
      handleGuardianSpawn(event, data);
      if (!data.guardianSpawned) {
        MobSpawner.spawnRiftMobsAroundPlayer(event.player, data);
      }
      RiftWorldSaveDataAccessor.saveRift(event.player.world, data);
      DimensionUtil.syncPlayers(event.player, data);
    }
  }

  private static void handleGuardianSpawn(PlayerTickEvent event, RiftData data) {
    if (data.guardianSpawned || data.progress() < 1) {
      return;
    }
    GuardianSpawner.spawnRiftGuardianAroundPlayer(event.player);
    data.guardianSpawned = true;
  }

  private static RiftData loadPlayerRift(EntityPlayer player) {
    return RiftWorldSaveDataAccessor.loadRift(
        player.world,
        RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ)
    );
  }

}
