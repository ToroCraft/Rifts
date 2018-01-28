package net.torocraft.rifts.save;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.data.RiftType;

public class RiftWorldSaveDataAccessor {

  private static RiftData createRandom(int riftId) {
    RiftData data = new RiftData();
    data.type = RiftType.values()[new Random().nextInt(RiftType.values().length)];
    data.portalLocation = 0;
    data.progress = 0;
    return data;
  }

  public static RiftData loadRift(World world, int riftId) {
    RiftData data = get(world).loadRift(riftId);
    if (data == null) {
      data = createRandom(riftId);
      saveRift(world, riftId, data);
    }
    return data;
  }

  public static void saveRift(World world, int riftId, RiftData data) {
    get(world).saveRift(riftId, data);
  }

  private static RiftsWorldSaveData get(World world) {
    MapStorage storage = world.getPerWorldStorage();
    RiftsWorldSaveData instance = (RiftsWorldSaveData) storage
        .getOrLoadData(RiftsWorldSaveData.class, RiftsWorldSaveData.NAME);
    if (instance == null) {
      instance = new RiftsWorldSaveData();
      storage.setData(RiftsWorldSaveData.NAME, instance);
    }
    return instance;
  }

}
