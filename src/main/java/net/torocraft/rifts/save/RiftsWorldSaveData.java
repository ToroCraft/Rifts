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