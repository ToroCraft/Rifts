package net.torocraft.rifts.items;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.torocraft.rifts.Rifts;

public class RecipeItemRiftKeystoneUpgrade extends IForgeRegistryEntry.Impl<IRecipe> implements
    IRecipe {

  private final ItemStack output = new ItemStack(ItemRiftKeyStone.INSTANCE);

  @Override
  public ItemStack getRecipeOutput() {
    return output;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
    return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
  }

  @Override
  public boolean matches(InventoryCrafting inv, World world) {
    return getNextLevel(inv) > 0;
  }

  private int getNextLevel(InventoryCrafting inv) {
    int level = 0;
    boolean complete = false;

    for (int index = 0; index < inv.getSizeInventory(); index++) {
      ItemStack stack = inv.getStackInSlot(index);
      if (stack.isEmpty()) {
        continue;
      }

      if (complete) {
        return -1;
      }

      int thisLevel = getKeystoneLevel(stack);

      if (thisLevel == 0) {
        return -1;
      }

      if (level > 0 && !complete) {
        complete = level == thisLevel;
      }

      level = thisLevel;
    }

    return complete ? level + 1 : -1;
  }

  public static int getKeystoneLevel(ItemStack stack) {
    if (!isKeystone(stack)) {
      return 0;
    }
    if (!stack.hasTagCompound()) {
      return 1;
    }
    int level = stack.getTagCompound().getInteger(Rifts.NBT_RIFT_LEVEL);
    return level == 0 ? 1 : level;
  }

  public static boolean isKeystone(ItemStack stack) {
    return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemRiftKeyStone;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    NBTTagCompound c = new NBTTagCompound();
    c.setInteger(Rifts.NBT_RIFT_LEVEL, getNextLevel(inv));
    ItemStack output = getRecipeOutput().copy();
    output.setTagCompound(c);
    return output;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height >= 2;
  }

}