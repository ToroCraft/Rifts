package net.torocraft.rifts.dim;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.items.ItemRiftKeyStone;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.world.RiftUtil;

@EventBusSubscriber
public class DeathHandler {

  @SubscribeEvent
  public static void onDeath(LivingDeathEvent event) {
    if (event.getEntity().world.isRemote) {
      return;
    }
    if (isRiftGuardian(event.getEntity())) {
      onRiftGuardianDeath((EntityCreature) event.getEntity());
    }
    if (isWitch(event)) {
      chanceToDropKeyStone((EntityCreature) event.getEntity());
    }
  }

  private static boolean isWitch(LivingDeathEvent event) {
    return event.getEntity() instanceof EntityWitch
        && event.getEntity().dimension == 0;
  }

  private static void chanceToDropKeyStone(EntityCreature entity) {
    int roll = entity.getRNG().nextInt(100);
    if (roll > 80) {
      dropKeystones(entity, 1, 1);
    }
  }

  private static boolean isRiftGuardian(Entity entity) {
    if (!(entity instanceof EntityCreature)) {
      return false;
    }
    for (String tag : entity.getTags()) {
      if (Rifts.TAG_GUARDIAN.equals(tag)) {
        return true;
      }
    }
    return false;
  }

  private static void onRiftGuardianDeath(EntityCreature entity) {
    int riftId = RiftUtil.getRiftIdForChunk(entity.chunkCoordX, entity.chunkCoordZ);
    RiftData data = RiftWorldSaveDataAccessor.loadRift(entity.world, riftId);
    if (data == null) {
      return;
    }

    int keyStoneDropLevel = Math.min(1, data.level - 2);
    if (data.time() > 0) {
      keyStoneDropLevel = data.level + 1;
    }

    int dropAmount = entity.getRNG().nextInt(3) + 2;
    dropKeystones(entity, keyStoneDropLevel, dropAmount);

    data.active = false;
    data.guardianSpawned = true;
  }

  private static void dropKeystones(EntityCreature fromEntity, int level, int amount) {
    double x = fromEntity.posX;
    double y = fromEntity.posY;
    double z = fromEntity.posZ;
    ItemStack stack = new ItemStack(ItemRiftKeyStone.INSTANCE, amount);
    NBTTagCompound c = new NBTTagCompound();
    c.setInteger(Rifts.NBT_RIFT_LEVEL, level);
    stack.setTagCompound(c);
    EntityItem itemEntity = new EntityItem(fromEntity.world, x, y, z, stack);
    fromEntity.world.spawnEntity(itemEntity);
  }

}
