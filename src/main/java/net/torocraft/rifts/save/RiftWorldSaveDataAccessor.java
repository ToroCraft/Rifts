package net.torocraft.rifts.save;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.torocraft.rifts.save.data.RiftData;

public class RiftWorldSaveDataAccessor {

  public static RiftData loadRift(World world, int riftId) {
    return get(world).loadRift(riftId);
  }

  public static RiftData createRift(World world, BlockPos pos, int level) {
    return get(world).createRift(pos, level);
  }

  public static RiftData findByPortalPosition(World world, BlockPos pos) {
    return get(world).findByPortalPosition(pos);
  }

  public static void saveRift(World world, RiftData data) {
    get(world).saveRift(data);
  }

  public static void removeRift(World world, int riftId) {
    get(world).removeRift(riftId);
  }

  private static RiftsWorldSaveData get(World world) {
    MapStorage storage = world.getMapStorage();

    RiftsWorldSaveData instance = (RiftsWorldSaveData) storage.getOrLoadData(
        RiftsWorldSaveData.class,
        RiftsWorldSaveData.NAME
    );

    if (instance == null) {
      instance = new RiftsWorldSaveData();
      storage.setData(RiftsWorldSaveData.NAME, instance);
    }

    return instance;
  }
}
