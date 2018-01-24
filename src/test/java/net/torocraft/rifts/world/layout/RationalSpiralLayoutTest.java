package net.torocraft.rifts.world.layout;

import org.junit.Test;

public class RationalSpiralLayoutTest {

  private static final RationalSpiralLayout layout = new RationalSpiralLayout();

  @Test
  public void indexToPoint() throws Exception {
    LayoutTestUtil.testIndexToPoint(layout, 0, 0, 0);
    LayoutTestUtil.testIndexToPoint(layout, 1, 1, 0);
    LayoutTestUtil.testIndexToPoint(layout, 2, 1, 1);
    LayoutTestUtil.testIndexToPoint(layout, 3, 0, 1);
    LayoutTestUtil.testIndexToPoint(layout, 4, -1, 1);
    LayoutTestUtil.testIndexToPoint(layout, 5, -1, 0);
  }

  @Test
  public void pointToIndex() throws Exception {

  }
}