package net.torocraft.rifts.world.layout;

import org.junit.Assert;
import org.junit.Test;

public class SimpleLinearLayoutTest {

  private static final SimpleLinearLayout layout = new SimpleLinearLayout();

  @Test
  public void indexToPoint() throws Exception {
    LayoutTestUtil.testIndexToPoint(layout, 0, 0, 0);
    LayoutTestUtil.testIndexToPoint(layout, 1, 1, 0);
    LayoutTestUtil.testIndexToPoint(layout, 2, 2, 0);
    LayoutTestUtil.testIndexToPoint(layout, 200, 200, 0);
  }

  @Test
  public void pointToIndex() throws Exception {
    LayoutTestUtil.testPointToIndex(layout, 0, 0, 0);
    LayoutTestUtil.testPointToIndex(layout, 1, 0, 1);
    LayoutTestUtil.testPointToIndex(layout, 1000, 0, 1000);
  }

  @Test
  public void pointToIndexOutOfBounds() throws Exception {
    Assert.assertEquals(layout.pointToIndex(0, 1), -1);
    Assert.assertEquals(layout.pointToIndex(10, -4), -1);
  }

}