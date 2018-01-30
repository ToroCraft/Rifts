package net.torocraft.rifts.save.data;

import java.util.HashMap;
import java.util.Map;
import net.torocraft.torotraits.nbt.NbtField;

public class WorldData {

  @NbtField
  public int nextRift = 0;

  @NbtField(genericType = RiftData.class)
  public Map<String, RiftData> rifts = new HashMap<>();

  // TODO leaderboard
}
