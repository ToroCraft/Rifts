package net.torocraft.rifts.entities;

import net.torocraft.rifts.save.data.RiftData;

public interface RiftGuardian {
  void setRift(RiftData data);

  float getScale();

  static float getScaleForLevel(int level) {
    return 2f;
  }
}
