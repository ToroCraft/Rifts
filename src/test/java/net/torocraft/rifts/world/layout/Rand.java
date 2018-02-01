package net.torocraft.rifts.world.layout;

import java.util.Random;
import org.junit.Test;

public class Rand {
  @Test
  public void test () {
    long seed = 564184316;
    Random rand = new Random(12);
    for (int i = 0; i < 10; i++) {
      rand.setSeed(seed++);
      //rand.nextInt();
      System.out.println(rand.nextInt(1000));
    }
  }
}
