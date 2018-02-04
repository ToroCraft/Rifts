package net.torocraft.rifts.save.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.torocraft.torotraits.nbt.NbtField;
import net.torocraft.torotraits.nbt.NbtSerializer;

public class RiftData {

  public static final int COMPLETE_TIME = 120;
  public static final int COMPLETE_PROGRESS = 300;

  @NbtField
  public int riftId;

  /**
   * rift progress out of 350
   */
  @NbtField
  public int progress;

  /**
   * rift time in rift ticks (1/5sec)
   *
   * 10 minutes = 120 rift ticks
   */
  @NbtField
  public int time;

  /**
   * the location of the rift portal in the overworld, a BlockPos packed into a long
   */
  @NbtField
  public long portalLocation = BlockPos.ORIGIN.toLong();

  @NbtField
  public int level = 1;

  /**
   * when false, this data can be removed from world save
   */
  public boolean active = true;

  public float progress() {
    return progress / (float) COMPLETE_PROGRESS;
  }

  public float time() {
    return 1 - (time / (float) COMPLETE_TIME);
  }

  public static RiftData random(int riftId) {
    RiftData data = new RiftData();
    data.riftId = riftId;
    return data;
  }

  public static RiftData fromNBT(NBTTagCompound c) {
    RiftData saveData = new RiftData();
    NbtSerializer.read(c, saveData);
    return saveData;
  }

  public static NBTTagCompound toNBT(RiftData data) {
    NBTTagCompound c = new NBTTagCompound();
    NbtSerializer.write(c, data);
    return c;
  }

}
