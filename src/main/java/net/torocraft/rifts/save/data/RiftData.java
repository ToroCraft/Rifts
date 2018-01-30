package net.torocraft.rifts.save.data;

import net.minecraft.util.math.BlockPos;
import net.torocraft.torotraits.nbt.NbtField;

public class RiftData {

  @NbtField
  public RiftType type = RiftType.PLAINS;

  /**
   * rift progress out of 100
   */
  @NbtField
  public int progress = 0;

  /**
   * the location of the rift portal in the overworld, a BlockPos packed into a long
   */
  @NbtField
  public long portalLocation = BlockPos.ORIGIN.toLong();

  public transient boolean keystoneDropped = false;

  /**
   * when false, this data can be removed from world save
   */
  public boolean active = true;

  public static RiftData random() {
    RiftData data = new RiftData();
    data.type = RiftType.random();
    return data;
  }
}
