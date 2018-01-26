package net.torocraft.rifts.world.layout;

public interface RiftLayout {

  /**
   * given the rift ID (index) return the canonical coordinates of the rift as an integer array of
   * the form [x, y]
   */
  int[] indexToPoint(int index);

  /**
   * given the canonical rift coordinates, return the rift ID (index). function will return -1 if
   * there is no index for the given canonical coordinates
   */
  int pointToIndex(int x, int z);
}
