package net.torocraft.rifts.save;

import java.util.Map.Entry;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.data.WorldData;
import net.torocraft.torotraits.nbt.NbtSerializer;

public class RiftsWorldSaveData extends WorldSavedData {

  public static final String NAME = Rifts.MODID + ":RiftsSaveData";

  private static final String NBT_RIFTS = "torocraft_rifts";

  private WorldData saveData = new WorldData();

  public RiftsWorldSaveData() {
    super(NAME);
  }

  public RiftsWorldSaveData(String s) {
    super(s);
  }

  public void saveRift(int riftId, RiftData data) {
    saveData.rifts.put(riftId + "", data);
    markDirty();
  }

  public int createRift(BlockPos pos) {
    int riftId = saveData.nextRift++;
    System.out.println("creating Rift " + riftId);
    RiftData data = RiftData.random();
    data.portalLocation = pos.toLong();
    saveRift(riftId, data);
    return riftId;
  }

  public int findByPos(BlockPos pos) {
    for (Entry<String, RiftData> entry : saveData.rifts.entrySet()) {
      if (distanceSq(pos, entry.getValue()) < 10) {
        System.out.println("found rift: " + entry.getKey());
        return i(entry.getKey());
      }
    }
    System.out.println("rift not found, now what?");
    return 0;
  }

  private double distanceSq(BlockPos pos, RiftData riftData) {
    return pos.distanceSq(BlockPos.fromLong(riftData.portalLocation));
  }

  private int i(String key) {
    try {
      return Integer.parseInt(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public RiftData loadRift(int riftId) {
    return saveData.rifts.get(riftId + "");
  }

  @Override
  public void readFromNBT(NBTTagCompound c) {
    saveData = new WorldData();
    NbtSerializer.read(c, saveData);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound c) {
    NbtSerializer.write(c, saveData);
    return c;
  }

}