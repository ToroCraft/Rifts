package net.torocraft.rifts.world;

import net.minecraft.util.math.BlockPos;
import net.torocraft.rifts.world.layout.RiftLayout;
import net.torocraft.rifts.world.layout.SimpleLinearLayout;

public class RiftUtil {

  public static final RiftLayout INSTANCE = new SimpleLinearLayout();
  /**
   * the length and width of the rifts measured in chunks
   */
  public static final int RIFT_SIZE = 32;
  /**
   * the distance from the origin of a rift to its edge measured in blocks
   */
  public static final int RIFT_RADIUS = (RIFT_SIZE * 16) / 2;
  /**
   * the distance from the start of one rift to the next one measured in blocks (including margin
   * between them)
   */
  public static final int RIFT_DISTANCE = RIFT_SIZE * 16 * 2;

  /**
   * returns -1 if in a margin
   */
  public static int getRiftIdForChunk(int chunkX, int chunkZ) {
    int canonicalX = chunkX / (RIFT_SIZE * 2);
    int canonicalZ = chunkZ / (RIFT_SIZE * 2);

    int cX, cZ;

    if (chunkX < 0) {
      canonicalX--;
    }

    if (chunkZ < 0) {
      canonicalZ--;
    }

    cX = chunkX - (canonicalX * RIFT_SIZE * 2);
    cZ = chunkZ - (canonicalZ * RIFT_SIZE * 2);

    if (cX >= RIFT_SIZE || cZ >= RIFT_SIZE) {
      return -1;
    }
    return INSTANCE.pointToIndex(canonicalX, canonicalZ);
  }

  public static int[] getRiftOrigin(int riftId) {
    int[] canonical = INSTANCE.indexToPoint(riftId);
    int chunkX = 2 * RIFT_SIZE * canonical[0];
    int chunkZ = 2 * RIFT_SIZE * canonical[1];
    return new int[]{chunkX, chunkZ};
  }

  public static BlockPos getRiftCenter(int riftId) {
    int[] chunk = getRiftOrigin(riftId);
    int x = (chunk[0] * 16) + RIFT_RADIUS;
    int z = (chunk[1] * 16) + RIFT_RADIUS;
    return new BlockPos(x, 256, z);
  }
}
