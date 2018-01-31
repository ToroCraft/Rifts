package net.torocraft.rifts.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.util.PortalUtil;

@EventBusSubscriber
public class ItemRiftKeyStone extends Item {

  public static ItemRiftKeyStone INSTANCE;
  public static final String NAME = "rift_keystone";

  public static ModelResourceLocation model =
      new ModelResourceLocation(Rifts.MODID + ":" + NAME, "inventory");

  public static ModelResourceLocation modelOn =
      new ModelResourceLocation(Rifts.MODID + ":" + NAME + "_on", "inventory");

  @SubscribeEvent
  public static void init(RegistryEvent.Register<Item> event) {
    INSTANCE = new ItemRiftKeyStone();
    INSTANCE.setRegistryName(new ResourceLocation(Rifts.MODID, NAME));
    event.getRegistry().register(INSTANCE);
  }

  @SideOnly(Side.CLIENT)
  public static void registerRenders() {
    ModelResourceLocation model = new ModelResourceLocation(Rifts.MODID + ":" + NAME, "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
  }

  public ItemRiftKeyStone() {
    setUnlocalizedName(NAME);
    this.setCreativeTab(CreativeTabs.MISC);
  }

  @Override
  public boolean isDamageable() {
    return false;
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
      EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (world.isRemote) {
      return EnumActionResult.PASS;
    }
    if (PortalUtil.openRiftPortal(player, pos.up(), facing)) {
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.FAIL;
  }

}
