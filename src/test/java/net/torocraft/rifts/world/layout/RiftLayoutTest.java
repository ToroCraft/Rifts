package net.torocraft.rifts.world.layout;

import org.junit.Assert;
import org.junit.Test;

public class RiftLayoutTest {

  @Test
  public void getRiftIdFromOrigin() throws Exception {
    Assert.assertEquals(0, RiftLayout.getRiftIdFromOrigin(0, 0));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(-1, 0));
    Assert.assertEquals(0, RiftLayout.getRiftIdFromOrigin(
        RiftLayout.RIFT_SIZE - 1,
        RiftLayout.RIFT_SIZE - 1
    ));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(
        RiftLayout.RIFT_SIZE,
        RiftLayout.RIFT_SIZE - 1
    ));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(
        RiftLayout.RIFT_SIZE * 2 - 1,
        0
    ));
    Assert.assertEquals(1, RiftLayout.getRiftIdFromOrigin(
        RiftLayout.RIFT_SIZE * 2,
        0
    ));
  }

  @Test
  public void getRiftOrigin() throws Exception {
    int rift = 15;
    int[] chunk = RiftLayout.getRiftOrigin(rift);
    chunk[0] += RiftLayout.RIFT_SIZE - 1;
    chunk[1] += RiftLayout.RIFT_SIZE - 1;
    Assert.assertEquals(rift, RiftLayout.getRiftIdFromOrigin(chunk[0], chunk[1]));
  }

  @Test
  public void getRiftOrigin_inMargin() throws Exception {
    int rift = 15;
    int[] chunk = RiftLayout.getRiftOrigin(rift);
    chunk[0] += RiftLayout.RIFT_SIZE;
    chunk[1] += RiftLayout.RIFT_SIZE;
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(chunk[0], chunk[1]));
  }

}