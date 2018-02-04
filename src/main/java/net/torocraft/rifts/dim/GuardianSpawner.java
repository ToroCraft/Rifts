package net.torocraft.rifts.dim;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.rifts.RiftsConfig;
import net.torocraft.rifts.entities.RiftGuardian;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.util.Timer;
import net.torocraft.rifts.world.RiftUtil;
import net.torocraft.torotraits.api.SpawnApi;
import net.torocraft.torotraits.api.SpawnLocationScanner;

public class GuardianSpawner {

  private static String pickMob(Random rand) {
    return RiftsConfig.GUARDIAN_WHITELIST[rand.nextInt(RiftsConfig.GUARDIAN_WHITELIST.length)];
  }

  public static void spawnRiftGuardianAroundPlayer(EntityPlayer player) {
    World world = player.world;
    int riftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
    RiftData data = RiftWorldSaveDataAccessor.loadRift(world, riftId);

    if (data == null) {
      data = new RiftData();
    }
    String entityId = pickMob(world.rand);
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);
    if (entity instanceof RiftGuardian) {
      ((RiftGuardian) entity).setRift(data);
    }
    BlockPos pos = findPos(player, world, entity);
    MobDecorator.decorate(entity, data.level);
    specialDefects(world, pos, () -> SpawnApi.spawn(world, entity, pos));
  }

  private static BlockPos findPos(EntityPlayer player, World world, EntityCreature entity) {
    SpawnLocationScanner scanner = new SpawnLocationScanner(world, entity, player.getPosition());

    BlockPos pos = scanner.areaScan(10, 10, 10);
    if (pos == null) {
      pos = player.getPosition();
    }
    return pos;
  }

  private static void delay(int time, Runnable task) {
    Timer.INSTANCE.addScheduledTask(task, time);
  }

  private static void specialDefects(World world, BlockPos pos, Runnable onComplete) {
    double x = pos.getX() + 0.5;
    double y = pos.getY();
    double z = pos.getZ() + 0.5;
    bolt(world, x, y, z);
    delay(800, () -> {
      bolt(world, x, y, z);
      delay(400, () -> {
        bolt(world, x, y, z);
        delay(200, () -> {
          bolt(world, x, y, z);
          delay(100, () -> {
            bolt(world, x, y, z);
            delay(50, () -> {
              boom(world, x, y, z);
              delay(20, onComplete);
            });
          });
        });
      });
    });
  }

  private static void boom(World world, double x, double y, double z) {
    world.createExplosion(null, x, y + 4, z, 5.0F, true);
  }

  private static void bolt(World world, double x, double y, double z) {
    world.addWeatherEffect(new EntityLightningBolt(world, x, y, z, true));
  }

}
