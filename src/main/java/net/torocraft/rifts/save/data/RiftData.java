package net.torocraft.rifts.save.data;

import net.torocraft.torotraits.nbt.NbtField;

public class RiftData {

  @NbtField
  public RiftType type;

  /**
   * out of 100
   */
  @NbtField
  public int progress;

}
