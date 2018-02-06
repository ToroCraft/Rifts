package net.torocraft.rifts.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.dim.DimensionUtil;
import net.torocraft.rifts.util.Timer;
import net.torocraft.rifts.world.RiftUtil;

@EventBusSubscriber
public class ItemRiftReturnStone extends Item {

  public static ItemRiftReturnStone INSTANCE;
  public static final String NAME = "rift_return_stone";

  @SubscribeEvent
  public static void init(Register<Item> event) {
    INSTANCE = new ItemRiftReturnStone();
    INSTANCE.setRegistryName(new ResourceLocation(Rifts.MODID, NAME));
    event.getRegistry().register(INSTANCE);
  }

  @SideOnly(Side.CLIENT)
  public static void registerRenders() {
    ModelResourceLocation model = new ModelResourceLocation(Rifts.MODID + ":" + NAME, "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
  }

  public ItemRiftReturnStone() {
    setUnlocalizedName(NAME);
    this.setCreativeTab(CreativeTabs.MISC);
  }

  @Override
  public boolean isDamageable() {
    return false;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
      EnumHand handIn) {
    EnumActionResult result = handleClick(world, player, handIn);
    return new ActionResult<>(result, player.getHeldItem(handIn));
  }

  private EnumActionResult handleClick(World world, EntityPlayer player, EnumHand hand) {
    if (world.isRemote) {
      return EnumActionResult.PASS;
    }
    if (player.dimension == Rifts.RIFT_DIM_ID) {
      int riftId = RiftUtil.getRiftIdForChunk(player.chunkCoordX, player.chunkCoordZ);
      Timer.INSTANCE.addScheduledTask(() -> player.getHeldItem(hand).shrink(1));
      DimensionUtil.travelToOverworld((EntityPlayerMP) player, riftId);
      return EnumActionResult.SUCCESS;
    }
    playSound(player, SoundEvents.ENTITY_CREEPER_HURT);
    return EnumActionResult.FAIL;
  }

  private static void playSound(EntityPlayer player, SoundEvent sound) {
    player.world.playSound(null,
        player.posX, player.posY, player.posZ,
        sound,
        SoundCategory.PLAYERS,
        1.0F, 1.0F);
  }

}
