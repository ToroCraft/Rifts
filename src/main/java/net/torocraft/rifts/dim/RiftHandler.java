package net.torocraft.rifts.dim;


import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.RiftsConfig;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;
import net.torocraft.torotraits.api.SpawnApi;

@EventBusSubscriber
public class RiftHandler {

  private static final int SPAWN_IN_RADIUS = 50;
  private static final int MAX_ENTITIES_IN_AREA = 100;

  @SubscribeEvent
  public static void init(PlayerTickEvent event) {
    if (!isRiftTick(event)) {
      return;
    }
    spawnRiftMobs(event);
  }


  private static boolean isRiftTick(PlayerTickEvent e) {
    return !e.player.world.isRemote && e.player.dimension == Rifts.RIFT_DIM_ID;
  }


  private static void spawnRiftMobs(PlayerTickEvent event) {
    if (event.player.world.getTotalWorldTime() % 200 != 0) {
      return;
    }

    RiftData data = loadRift(event);
    System.out.println("****event rift[" + data.riftId + "] level[" + data.level + "] **");
    System.out.println("for player " + event.player.getName());

    if (!tooManyEntities(event)) {
      spawnRiftMob(event.player.world, event.player.getPosition());
    }
  }

  private static boolean tooManyEntities(PlayerTickEvent event) {
    return count(event.player.world, event.player.getPosition()) > MAX_ENTITIES_IN_AREA;
  }

  private static int count(World world, BlockPos pos) {
    AxisAlignedBB box = new AxisAlignedBB(pos).grow(100, 100, 100);
    return world.getEntitiesWithinAABB(EntityMob.class, box).size();
  }

  private static void spawnMobPack(World world, BlockPos pos, String entityId) {
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);
    if (entity == null) {
      return;
    }
    entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
    world.spawnEntity(entity);
    entity.playLivingSound();
  }

  private static String pickMob(Random rand) {
    return RiftsConfig.MOB_WHITELIST[rand.nextInt(RiftsConfig.MOB_WHITELIST.length)];
  }

  private static void spawnRiftMob(World world, BlockPos around) {
    String entityId = pickMob(world.rand);
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);
    SpawnApi.spawnEntityCreature(world, entity, around, SPAWN_IN_RADIUS);
    int packSize = entity.getRNG().nextInt(5);
    for (int i = 0; i < packSize; i++) {
      spawnMobPack(world, entity.getPosition(), entityId);
    }
  }

  private static RiftData loadRift(PlayerTickEvent event) {
    return RiftWorldSaveDataAccessor.loadRift(
        event.player.world,
        RiftUtil.getRiftIdForChunk(event.player.chunkCoordX, event.player.chunkCoordZ)
    );
  }

}
