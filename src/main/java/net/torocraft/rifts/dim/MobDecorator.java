package net.torocraft.rifts.dim;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MobDecorator {

  private static final Item[] WEAPONS = {
      Items.STONE_SHOVEL,
      Items.STONE_AXE,
      Items.STONE_HOE,
      Items.STONE_PICKAXE,
      Items.STONE_SWORD,
      Items.GOLDEN_SHOVEL,
      Items.GOLDEN_AXE,
      Items.GOLDEN_HOE,
      Items.GOLDEN_PICKAXE,
      Items.GOLDEN_SWORD,
      Items.IRON_SHOVEL,
      Items.IRON_AXE,
      Items.IRON_HOE,
      Items.IRON_PICKAXE,
      Items.IRON_SWORD,
      Items.DIAMOND_SHOVEL,
      Items.DIAMOND_AXE,
      Items.DIAMOND_HOE,
      Items.DIAMOND_PICKAXE,
      Items.DIAMOND_SWORD
  };

  private static final Item[] HELMETS = {
      Items.LEATHER_HELMET,
      Items.CHAINMAIL_HELMET,
      Items.GOLDEN_HELMET,
      Items.IRON_HELMET,
      Items.DIAMOND_HELMET
  };

  private static final Item[] CHEST_PLATES = {
      Items.LEATHER_CHESTPLATE,
      Items.CHAINMAIL_CHESTPLATE,
      Items.GOLDEN_CHESTPLATE,
      Items.IRON_CHESTPLATE,
      Items.DIAMOND_CHESTPLATE

  };

  private static final Item[] LEGGINGS = {
      Items.LEATHER_LEGGINGS,
      Items.CHAINMAIL_LEGGINGS,
      Items.GOLDEN_LEGGINGS,
      Items.IRON_LEGGINGS,
      Items.DIAMOND_LEGGINGS
  };

  private static final Item[] BOOTS = {
      Items.LEATHER_BOOTS,
      Items.CHAINMAIL_BOOTS,
      Items.GOLDEN_BOOTS,
      Items.IRON_BOOTS,
      Items.DIAMOND_BOOTS
  };

  public static void decorate(EntityLiving entity, int level) {
    setArmorAndWeapons(entity, level);
    setAttributes(entity, level);
  }

  private static void setArmorAndWeapons(EntityLiving entity, int level) {

    int maxRoll = (int) Math.round((level / 10d) - 1);
    if (maxRoll < 1) {
      return;
    }

    int armorRoll = entity.getRNG().nextInt(HELMETS.length);
    int weaponRoll = entity.getRNG().nextInt(WEAPONS.length);

    armorRoll = Math.min(maxRoll, armorRoll);
    weaponRoll = Math.min(maxRoll * 5, weaponRoll);

    ItemStack helmet = new ItemStack(HELMETS[armorRoll]);
    ItemStack chestPlate = new ItemStack(CHEST_PLATES[armorRoll]);
    ItemStack leggings = new ItemStack(LEGGINGS[armorRoll]);
    ItemStack boots = new ItemStack(BOOTS[armorRoll]);
    ItemStack weapon = new ItemStack(WEAPONS[weaponRoll]);

    entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);
    entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestPlate);
    entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, leggings);
    entity.setItemStackToSlot(EntityEquipmentSlot.FEET, boots);

    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weapon);
  }

  private static void setAttributes(EntityLiving entity, int level) {

    double healthFactor = 0.6 + (level / 30d);
    double attackFactor = 0.6 + (level / 40d);

    for (IAttributeInstance attribute : entity.getAttributeMap().getAllAttributes()) {
      if (attribute.getAttribute() == SharedMonsterAttributes.ATTACK_DAMAGE) {
        attribute.setBaseValue(attribute.getAttributeValue() * attackFactor);
      }

      if (attribute.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
        attribute.setBaseValue(attribute.getAttributeValue() * healthFactor);
        entity.setHealth(entity.getMaxHealth());
      }
    }
  }

}
