package net.torocraft.rifts.dim;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;

@EventBusSubscriber
public class ProgressHandler {

  private static final int SPAWN_IN_RADIUS = 50;
  private static final int MAX_ENTITIES_IN_AREA = 100;

  @SubscribeEvent
  public static void onXpPickup(LivingExperienceDropEvent event) {
    EntityPlayer player = event.getAttackingPlayer();
    if (player == null || player.dimension != Rifts.RIFT_DIM_ID) {
      return;
    }

    event.setCanceled(true);
    int riftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
    RiftData data = RiftWorldSaveDataAccessor.loadRift(player.world, riftId);
    if (data == null) {
      return;
    }

    data.progress += event.getDroppedExperience();
    RiftWorldSaveDataAccessor.saveRift(player.world, data);
    DimensionUtil.syncPlayers(event.getAttackingPlayer(), data);
  }


}
