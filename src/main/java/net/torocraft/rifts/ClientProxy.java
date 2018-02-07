package net.torocraft.rifts;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.rifts.blocks.BlockRiftPortal;
import net.torocraft.rifts.entities.Entities;
import net.torocraft.rifts.gui.GuiRiftStatus;
import net.torocraft.rifts.items.ItemCrackedRiftKeyStone;
import net.torocraft.rifts.items.ItemRiftKeyStone;
import net.torocraft.rifts.items.ItemRiftReturnStone;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;

public class ClientProxy extends CommonProxy {

  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
    Entities.registerRenders();
  }

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
    MinecraftForge.EVENT_BUS.register(new GuiRiftStatus());
  }

  @Override
  public void updateRiftOnClient(RiftData data) {
    if (data == null) {
      GuiRiftStatus.data = null;
      return;
    }
    EntityPlayer player = Minecraft.getMinecraft().player;
    int playerRiftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
    if (playerRiftId != data.riftId) {
      return;
    }
    GuiRiftStatus.data = data;
  }

}