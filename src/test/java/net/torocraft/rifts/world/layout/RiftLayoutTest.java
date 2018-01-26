package net.torocraft.rifts.world.layout;

import net.torocraft.rifts.world.RiftUtil;
import org.junit.Assert;
import org.junit.Test;

public class RiftLayoutTest {

  @Test
  public void getRiftIdFromOrigin() throws Exception {
    Assert.assertEquals(0, RiftUtil.getRiftIdForChunk(0, 0));
    Assert.assertEquals(-1, RiftUtil.getRiftIdForChunk(-1, 0));

    Assert.assertEquals(-1, RiftUtil.getRiftIdForChunk(-2, 0));

    Assert.assertEquals(0, RiftUtil.getRiftIdForChunk(
        RiftUtil.RIFT_SIZE - 1,
        RiftUtil.RIFT_SIZE - 1
    ));
    Assert.assertEquals(-1, RiftUtil.getRiftIdForChunk(
        RiftUtil.RIFT_SIZE,
        RiftUtil.RIFT_SIZE - 1
    ));
    Assert.assertEquals(-1, RiftUtil.getRiftIdForChunk(
        RiftUtil.RIFT_SIZE * 2 - 1,
        0
    ));
    Assert.assertEquals(1, RiftUtil.getRiftIdForChunk(
        RiftUtil.RIFT_SIZE * 2,
        0
    ));
  }

  @Test
  public void getRiftOrigin() throws Exception {
    int rift = 15;
    int[] chunk = RiftUtil.getRiftOrigin(rift);
    chunk[0] += RiftUtil.RIFT_SIZE - 1;
    chunk[1] += RiftUtil.RIFT_SIZE - 1;
    Assert.assertEquals(rift, RiftUtil.getRiftIdForChunk(chunk[0], chunk[1]));
  }

  @Test
  public void getRiftOrigin_inMargin() throws Exception {
    int rift = 15;
    int[] chunk = RiftUtil.getRiftOrigin(rift);
    chunk[0] += RiftUtil.RIFT_SIZE;
    chunk[1] += RiftUtil.RIFT_SIZE;
    Assert.assertEquals(-1, RiftUtil.getRiftIdForChunk(chunk[0], chunk[1]));
  }

}