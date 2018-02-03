package net.torocraft.rifts;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.torocraft.rifts.blocks.BlockRiftPortal;
import net.torocraft.rifts.items.ItemCrackedRiftKeyStone;
import net.torocraft.rifts.items.ItemRiftKeyStone;
import net.torocraft.rifts.items.ItemRiftReturnStone;

public class ClientProxy extends CommonProxy {

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
    BlockRiftPortal.registerRenders();
    ItemRiftKeyStone.registerRenders();
    ItemCrackedRiftKeyStone.registerRenders();
    ItemRiftReturnStone.registerRenders();
  }

  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);
  }

}