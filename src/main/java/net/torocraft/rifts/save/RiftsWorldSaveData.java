package net.torocraft.rifts.save;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.data.WorldData;
import net.torocraft.torotraits.nbt.NbtSerializer;

public class RiftsWorldSaveData extends WorldSavedData {

  public static final String NAME = Rifts.MODID + ":RiftsSaveData";

  private static final String NBT_RIFTS = "torocraft_rifts";

  private WorldData saveData;

  public RiftsWorldSaveData() {
    super(NAME);
  }

  public RiftsWorldSaveData(String s) {
    super(s);
  }

  public synchronized int createRift(RiftData data) {
    saveData.rifts.add(data);
    return saveData.rifts.size() - 1;
  }

  public boolean updateRift(int riftId, RiftData data) {
    try {
      saveData.rifts.set(riftId, data);
      return true;
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  public RiftData getRift(int riftId) {
    try {
      return saveData.rifts.get(riftId);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
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