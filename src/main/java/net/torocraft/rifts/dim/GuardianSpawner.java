package net.torocraft.rifts.dim;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.torocraft.rifts.entities.RiftGuardian;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.torotraits.api.SpawnApi;

public class GuardianSpawner {

  private static String pickMob(Random rand) {
    return "rifts:rifts_husk";
  }

  public static void spawnRiftGuardianAroundPlayer(EntityPlayer player) {
    World world = player.world;
    //, RiftData data

    int level = 1;

    String entityId = pickMob(world.rand);
    EntityCreature entity = SpawnApi.getEntityFromString(world, entityId);

    if (entity instanceof RiftGuardian) {
      // TODO fixme null
      ((RiftGuardian) entity).setRift(null);
    }

    SpawnApi.spawnEntityCreature(world, entity, player.getPosition(), 10);
    MobDecorator.decorate(entity, level);



    // entity.onInitialSpawn(world.getDifficultyForLocation(around), null);

  }

}
