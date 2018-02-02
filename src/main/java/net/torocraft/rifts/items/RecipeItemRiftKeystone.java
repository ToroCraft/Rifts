package net.torocraft.rifts.items;

import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeItemRiftKeystone extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

  public static final String NBT_WHITE_BLOCK_KEY = "white_checker_block";
  public static final String NBT_BLACK_BLOCK_KEY = "black_checker_block";
  private static final int INDEX_WHITE_BLOCK = 3;
  private static final int INDEX_BLACK_BLOCK = 5;

  private final ItemStack output = new ItemStack(ItemRiftKeyStone.INSTANCE);

  public ItemStack getRecipeOutput() {
    return output;
  }

  public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
    return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
  }

  public boolean matches(InventoryCrafting inv, World worldIn) {

    int size = inv.getHeight() * inv.getWidth();

    if (size < 9) {
      return false;
    }

    for (int index = 0; index < 9; index++) {
      if (check(index, inv.getStackInSlot(index))) {
        return true;
      }
    }

    return false;
  }

  private boolean check(int index, ItemStack stack) {
    return isKeystone(stack);

  }

  private boolean isEntityEssenceItem(ItemStack stack) {
    return stack.getItem() == Items.BONE || stack.getItem() == Items.ROTTEN_FLESH || stack.getItem() == Items.ENDER_PEARL;
  }


  private boolean isKeystone(ItemStack stack) {
    if (stack == null || stack.isEmpty()) {
      return false;
    }
    return stack.getItem() instanceof ItemRiftKeyStone;
  }

  private boolean isBlock(ItemStack stack, Block block) {
    return getBlock(stack) == block;
  }

  private Block getBlock(ItemStack stack) {
    if (stack == null || stack.isEmpty()) {
      return null;
    }

    if (!(stack.getItem() instanceof ItemBlock)) {
      return null;
    }

    return ((ItemBlock) stack.getItem()).getBlock();
  }


  public ItemStack getCraftingResult(InventoryCrafting inv) {
    NonNullList<ItemStack> list = NonNullList.withSize(2, ItemStack.EMPTY);
    list.set(0, inv.getStackInSlot(INDEX_WHITE_BLOCK));
    list.set(1, inv.getStackInSlot(INDEX_BLACK_BLOCK));

    NBTTagCompound c = new NBTTagCompound();
    ItemStackHelper.saveAllItems(c, list);

    ItemStack output = getRecipeOutput().copy();
    output.setTagCompound(c);
    output.setStackDisplayName("Test Crafted Keystone");
    return output;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height >= 9;
  }

}