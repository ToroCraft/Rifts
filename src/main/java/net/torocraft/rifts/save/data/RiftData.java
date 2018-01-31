package net.torocraft.rifts.save.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.torocraft.torotraits.nbt.NbtField;
import net.torocraft.torotraits.nbt.NbtSerializer;

public class RiftData {

  @NbtField
  public int riftId;

  @NbtField
  public RiftType type = RiftType.PLAINS;

  /**
   * rift progress out of 100
   */
  @NbtField
  public int progress;

  /**
   * the location of the rift portal in the overworld, a BlockPos packed into a long
   */
  @NbtField
  public long portalLocation = BlockPos.ORIGIN.toLong();

  /**
   * when false, this data can be removed from world save
   */
  public boolean active = true;

  public static RiftData random(int riftId) {
    RiftData data = new RiftData();
    data.type = RiftType.random();
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
