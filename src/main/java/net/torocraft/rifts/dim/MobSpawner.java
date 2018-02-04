package net.torocraft.rifts.dim;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.rifts.RiftsConfig;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.torotraits.api.SpawnApi;

public class MobSpawner {

  private static final int SPAWN_IN_RADIUS = 50;
  private static final int MAX_ENTITIES_IN_AREA = 100;

  public static void spawnRiftMobsAroundPlayer(EntityPlayer player, RiftData data) {
    if (!tooManyEntities(player)) {
      spawnRiftMob(player.world, player.getPosition(), data.level);
    }
  }

  private static boolean tooManyEntities(EntityPlayer player) {
    return mobCount(player.world, player.getPosition()) > MAX_ENTITIES_IN_AREA;
  }

  private static int mobCount(World world, BlockPos pos) {
    AxisAlignedBB box = new AxisAlignedBB(pos).grow(100, 100, 100);
    return world.getEntitiesWithinAABB(EntityMob.class, box).size();
  }

  private static String pickMob(Random rand) {
    return RiftsConfig.MOB_WHITELIST[rand.nextInt(RiftsConfig.MOB_WHITELIST.length)];
  }

  private static void spawnRiftMob(World world, BlockPos around, int level) {
    String entityId = pickMob(world.rand);
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);
    SpawnApi.spawnEntityCreature(world, entity, around, SPAWN_IN_RADIUS);
    MobDecorator.decorate(entity, level);
    entity.onInitialSpawn(world.getDifficultyForLocation(around), null);
    int packSize = entity.getRNG().nextInt(5);
    for (int i = 0; i < packSize; i++) {
      spawnMobPack(world, entity.getPosition(), entityId, level);
    }
  }

  private static void spawnMobPack(World world, BlockPos pos, String entityId, int level) {
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);
    if (entity == null) {
      return;
    }
    entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
    world.spawnEntity(entity);
    entity.playLivingSound();
    MobDecorator.decorate(entity, level);

    entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
  }
}
