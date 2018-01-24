package net.torocraft.rifts.world.layout;

/**
 * Layout the rifts in the most compact form, a spiral starting from the original and rotating
 * outward.
 *
 * ....14 13 12 4  3  2 11 5  0  1 10 6  7  8  9
 *
 * indexToPoint logic found here and ported to java: https://math.stackexchange.com/questions/163080/on-a-two-dimensional-grid-is-there-a-formula-i-can-use-to-spiral-coordinates-in
 *
 * Other links: https://math.stackexchange.com/questions/2388738/cartesian-coordinates-of-the-points-on-the-plane-in-the-same-order-as-the-ulam-s
 * http://mathworld.wolfram.com/RationalSpiral.html
 */

public class RationalSpiralLayout implements RiftLayout {

  @Override
  public int[] indexToPoint(int index) {
    index++;

    if (index < 0) {
      throw new IllegalArgumentException("spiral index must be zero or greater, [" + index + "]");
    }

    int k = (int) Math.ceil((Math.sqrt(index) - 1) / 2);
    int t = (2 * k + 1);
    int m = (int) Math.pow(t, 2);

    t = t - 1;

    if (index >= m - t) {
      return new int[]{k - (m - index), -k};
    } else {
      m = m - t;
    }

    if (index >= m - t) {
      return new int[]{-k, -k + (m - index)};
    } else {
      m = m - t;
    }

    if (index >= m - t) {
      return new int[]{-k + (m - index), k};
    } else {
      return new int[]{k, k - (m - index - t)};
    }
  }

  @Override
  public int pointToIndex(int x, int y) {
    throw new UnsupportedOperationException("If I only had a brain.");
  }
}
