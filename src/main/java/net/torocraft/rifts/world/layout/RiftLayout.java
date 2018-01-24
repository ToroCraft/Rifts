package net.torocraft.rifts.world.layout;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public interface RiftLayout {

  RiftLayout INSTANCE = new SimpleLinearLayout();

  /**
   * the length and width of the rifts measured in chunks
   */
  int RIFT_SIZE = 1;

  /**
   * the distance from the start of one rift to the next one measured in blocks (including margin
   * between them)
   */
  int RIFT_DISTANCE = RIFT_SIZE * 16 * 2;

  /**
   * the distance from the origin of a rift to its edge measured in blocks
   */
  int RIFT_RADIUS = (RIFT_SIZE * 16) / 2;

  /**
   * returns -1 if in a margin
   */
  static int getRiftIdFromOrigin(BlockPos origin) {
    int canonicalX = MathHelper.floor(origin.getX() / (RIFT_DISTANCE / 2));
    int canonicalZ = MathHelper.floor(origin.getZ() / (RIFT_DISTANCE / 2));
    if (canonicalX % 2 == 1 || canonicalZ % 2 == 1) {
      return -1;
    }
    return INSTANCE.pointToIndex(canonicalX / 2, canonicalZ / 2);
  }

  static BlockPos getRiftOrigin(int riftId) {
    int[] canonical = INSTANCE.indexToPoint(riftId);
    int x = RIFT_DISTANCE * canonical[0];
    int z = RIFT_DISTANCE * canonical[1];
    return new BlockPos(x, 0, z);
  }


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
