package net.torocraft.rifts.world.layout;

import net.minecraft.util.math.BlockPos;
import org.junit.Assert;
import org.junit.Test;

public class RiftLayoutTest {

  @Test
  public void getRiftIdFromOrigin() throws Exception {
    Assert.assertEquals(0, RiftLayout.getRiftIdFromOrigin(new BlockPos(0, 0, 0)));
    Assert.assertEquals(0, RiftLayout.getRiftIdFromOrigin(new BlockPos(15, 0, 5)));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(new BlockPos(16, 0, 5)));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(new BlockPos(31, 0, 5)));
    Assert.assertEquals(1, RiftLayout.getRiftIdFromOrigin(new BlockPos(32, 0, 5)));
    Assert.assertEquals(1, RiftLayout.getRiftIdFromOrigin(new BlockPos(47, 0, 5)));
    Assert.assertEquals(-1, RiftLayout.getRiftIdFromOrigin(new BlockPos(48, 0, 5)));
    Assert.assertEquals(2, RiftLayout.getRiftIdFromOrigin(new BlockPos(64, 0, 5)));
  }

  @Test
  public void getRiftOrigin() throws Exception {
  }

}