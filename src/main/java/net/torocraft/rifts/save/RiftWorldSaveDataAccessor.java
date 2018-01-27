package net.torocraft.rifts.save;

import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.rifts.save.data.RiftData;

public class RiftWorldSaveDataAccessor {

  public static int createRift(World world, RiftData data) {
    return get(world).createRift(data);
  }

  public static RiftData getRift(World world, int riftId) {
    return get(world).getRift(riftId);
  }

  public static boolean udpateRift(World world, int riftId, RiftData data) {
    return get(world).updateRift(riftId, data);
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
