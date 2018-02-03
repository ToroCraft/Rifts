package net.torocraft.rifts.items;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.torocraft.rifts.Rifts;

public class RecipeItemRiftKeystoneDowngrade extends IForgeRegistryEntry.Impl<IRecipe> implements
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
    ItemStack stack = getKeystone(inv);
    return !stack.isEmpty() && getKeystoneLevel(stack) > 1;
  }

  private ItemStack getKeystone(InventoryCrafting inv) {
    ItemStack stack = ItemStack.EMPTY;

    for (int index = 0; index < inv.getSizeInventory(); index++) {
      if (inv.getStackInSlot(index).isEmpty()) {
        continue;
      }

      if (!stack.isEmpty()) {
        return ItemStack.EMPTY;
      }

      stack = inv.getStackInSlot(index);

      if (!isKeystone(stack)) {
        return ItemStack.EMPTY;
      }
    }

    return stack;
  }

  private int getKeystoneLevel(ItemStack stack) {
    if (!isKeystone(stack)) {
      return 0;
    }
    if (!stack.hasTagCompound()) {
      return 1;
    }
    int level = stack.getTagCompound().getInteger(Rifts.NBT_RIFT_LEVEL);
    return level == 0 ? 1 : level;
  }

  private boolean isKeystone(ItemStack stack) {
    return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemRiftKeyStone;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    NBTTagCompound c = new NBTTagCompound();
    ItemStack input = getKeystone(inv);
    c.setInteger(Rifts.NBT_RIFT_LEVEL, getKeystoneLevel(input) - 1);
    ItemStack output = getRecipeOutput().copy();
    output.setTagCompound(c);
    output.setCount(2);
    return output;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height >= 1;
  }

}