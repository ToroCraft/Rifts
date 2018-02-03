package net.torocraft.rifts.items;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.util.PortalUtil;
import net.torocraft.rifts.util.Timer;

@EventBusSubscriber
public class ItemRiftKeyStone extends Item {

  public static ItemRiftKeyStone INSTANCE;
  public static final String NAME = "rift_keystone";

  @SubscribeEvent
  public static void init(RegistryEvent.Register<Item> event) {
    INSTANCE = new ItemRiftKeyStone();
    INSTANCE.setRegistryName(new ResourceLocation(Rifts.MODID, NAME));
    event.getRegistry().register(INSTANCE);
  }

  @SubscribeEvent
  public static void initRecipe(RegistryEvent.Register<IRecipe> event) {
    loadRecipe(event, new RecipeItemRiftKeystoneUpgrade(), "_upgrade_recipe");
    loadRecipe(event, new RecipeItemRiftKeystoneDowngrade(), "_downgrade_recipe");
  }

  private static void loadRecipe(Register<IRecipe> event, IRecipe recipe, String suffix) {
    recipe.setRegistryName(new ResourceLocation(Rifts.MODID, NAME + suffix));
    event.getRegistry().register(recipe);
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
      Timer.INSTANCE.addScheduledTask(() -> player.getHeldItem(hand).shrink(1));
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.FAIL;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip,
      ITooltipFlag flagIn) {
    super.addInformation(stack, world, tooltip, flagIn);
    if (!stack.hasTagCompound()) {
      return;
    }
    int level = stack.getTagCompound().getInteger(Rifts.NBT_RIFT_LEVEL);
    tooltip.add(I18n.format("item." + NAME + ".tooltip", level));
  }

}
