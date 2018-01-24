package net.torocraft.rifts.world.layout;

import org.junit.Assert;

public class LayoutTestUtil {

  public static void testIndexToPoint(RiftLayout layout, int index, int x, int z) {
    Assert.assertArrayEquals(layout.indexToPoint(index), new int[]{x, z});
  }

  public static void testPointToIndex(RiftLayout layout, int x, int z, int index) {
    Assert.assertEquals(layout.pointToIndex(x, z), index);
  }
}
