package net.torocraft.rifts.world.layout;

/**
 * Layout the rifts in a simplistic by simply starting at the origin and progressing down X axis.
 */

public class SimpleLinearLayout implements RiftLayout {

  @Override
  public int[] indexToPoint(int index) {
    return new int []{index, 0};
  }

  @Override
  public int pointToIndex(int x, int z) {
    return z == 0 ? x : -1;
  }
}
