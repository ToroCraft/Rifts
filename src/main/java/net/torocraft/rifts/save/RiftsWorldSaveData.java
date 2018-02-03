package net.torocraft.rifts.save;

import java.util.Map.Entry;
import javax.annotation.Nullable;
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

  public void saveRift(RiftData data) {
    saveData.rifts.put(data.riftId + "", data);
    System.out.println("saved rift " + data.riftId);
    markDirty();
  }

  public void removeRift(int riftId) {
    saveData.rifts.remove(riftId + "");
    markDirty();
  }

  public RiftData createRift(BlockPos pos, int level) {
    RiftData data = RiftData.random(saveData.nextRift++);
    data.portalLocation = pos.toLong();
    data.level = level;
    saveRift(data);
    return data;
  }

  @Nullable
  public RiftData findByPortalPosition(BlockPos pos) {
    for (Entry<String, RiftData> entry : saveData.rifts.entrySet()) {
      if (distanceSq(pos, entry.getValue()) < 15) {
        return entry.getValue();
      }
    }
    return null;
  }

  private double distanceSq(BlockPos pos, RiftData riftData) {
    return pos.distanceSq(BlockPos.fromLong(riftData.portalLocation));
  }

  public RiftData loadRift(int riftId) {
    return saveData.rifts.get(riftId + "");
  }

  @Override
  public void readFromNBT(NBTTagCompound c) {
    saveData = new WorldData();
    System.out.println("READ: " + c);
    NbtSerializer.read(c, saveData);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound c) {
    NbtSerializer.write(c, saveData);
    System.out.println("WRITE: " + c);
    return c;
  }

}