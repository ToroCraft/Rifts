package net.torocraft.rifts.savedata;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.torocraft.rifts.Rifts;

public class RiftsWorldSaveData extends WorldSavedData {

  public static final String NAME = Rifts.MODID + ":RiftsSaveData";

  //private static final String NBT_NEMESES = "nemeses";

  //protected List<NemesisEntry> nemeses = new ArrayList<>();

  public RiftsWorldSaveData() {
    super(NAME);
  }

  public RiftsWorldSaveData(String s) {
    super(s);
  }

  @Override
  public void readFromNBT(NBTTagCompound c) {
    // nemeses = readNemesesFromNBT(c);
  }

  public static List<String> readNemesesFromNBT(NBTTagCompound c) {
//    NBTTagList nbtNemeses = loadNbtList(c);
//    ArrayList<NemesisEntry> nemeses = new ArrayList<>();
//    for (int i = 0; i < nbtNemeses.tagCount(); i++) {
//      NemesisEntry nemesis = new NemesisEntry();
//      nemesis.readFromNBT(nbtNemeses.getCompoundTagAt(i));
//      nemeses.add(nemesis);
//    }
//    return nemeses;
    return null;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound c) {
//    writeNemesesToNBT(c, nemeses);
//    return c;
    return null;
  }

}