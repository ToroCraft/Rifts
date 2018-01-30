package net.torocraft.rifts.save;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.data.RiftType;

public class RiftWorldSaveDataAccessor {

  public static RiftData loadRift(World world, int riftId) {
    RiftData data = get(world).loadRift(riftId);
    if (data == null) {
      data = RiftData.random();
      saveRift(world, riftId, data);
    }
    return data;
  }

  public static int createRift(World world, BlockPos pos) {
    return get(world).createRift(pos);
  }


  public static int findByPos(World world, BlockPos pos) {
    return get(world).findByPos(pos);
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
