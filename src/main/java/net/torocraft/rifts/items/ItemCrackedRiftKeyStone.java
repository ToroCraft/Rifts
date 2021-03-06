package net.torocraft.rifts.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.util.PortalUtil;
import net.torocraft.rifts.util.Timer;

@EventBusSubscriber
public class ItemCrackedRiftKeyStone extends Item {

  public static ItemCrackedRiftKeyStone INSTANCE;
  public static final String NAME = "cracked_rift_keystone";

  @SubscribeEvent
  public static void init(RegistryEvent.Register<Item> event) {
    INSTANCE = new ItemCrackedRiftKeyStone();
    INSTANCE.setRegistryName(new ResourceLocation(Rifts.MODID, NAME));
    event.getRegistry().register(INSTANCE);
  }

  @SideOnly(Side.CLIENT)
  public static void registerRenders() {
    ModelResourceLocation model = new ModelResourceLocation(Rifts.MODID + ":" + NAME, "inventory");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(INSTANCE, 0, model);
  }

  public ItemCrackedRiftKeyStone() {
    setUnlocalizedName(NAME);
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

    ItemStack keystone = player.getHeldItem(hand);
    RiftData data = getRiftDataFromKeystone(keystone);

    if (data == null) {
      return EnumActionResult.PASS;
    }

    if (PortalUtil.reopenRiftPortal(player, pos.up(), facing, data)) {
      Timer.INSTANCE.addScheduledTask(() -> player.getHeldItem(hand).shrink(1));
      return EnumActionResult.SUCCESS;
    }

    return EnumActionResult.FAIL;
  }

  @Nullable
  private RiftData getRiftDataFromKeystone(ItemStack keystone) {
    if (isCrackedKeystone(keystone)) {
      return readRiftDataFromNbt(keystone);
    }
    return null;
  }

  private boolean isCrackedKeystone(ItemStack keystone) {
    return !keystone.isEmpty() && keystone.getItem() == ItemCrackedRiftKeyStone.INSTANCE;
  }

  private RiftData readRiftDataFromNbt(ItemStack keystone) {
    try {
      return RiftData.fromNBT(keystone.getTagCompound().getCompoundTag(Rifts.NBT_RIFT_DATA));
    } catch (NullPointerException e) {
      e.printStackTrace();
      return null;
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip,
      ITooltipFlag flagIn) {
    super.addInformation(stack, world, tooltip, flagIn);
    if (!stack.hasTagCompound()) {
      return;
    }
    int riftId = stack.getTagCompound().getInteger(Rifts.NBT_RIFT_ID);
    tooltip.add(I18n.format("item." + NAME + ".tooltip", riftId));
  }

}
