package net.torocraft.rifts.save.data;

import net.torocraft.torotraits.nbt.NbtField;

public class RiftData {

  @NbtField
  public RiftType type;

  /**
   * rift progress out of 100
   */
  @NbtField
  public int progress;

  /**
   * the location of the rift portal in the overworld, a BlockPos packed into a long
   */
  @NbtField
  public long portalLocation;

  /**
   * when false, this data can be removed from world save
   */
  public boolean active = true;

}
