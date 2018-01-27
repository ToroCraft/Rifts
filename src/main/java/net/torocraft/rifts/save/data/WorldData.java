package net.torocraft.rifts.save.data;

import java.util.List;
import net.torocraft.torotraits.nbt.NbtField;

public class WorldData {

  @NbtField(genericType = RiftData.class)
  public List<RiftData> rifts;

  // TODO leaderboard
}
