package net.torocraft.rifts.dim;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
  public static void onGuardianDeath(LivingDeathEvent event) {
    if (isRiftGuardian(event.getEntity())) {
      onRiftGuardianDeath((EntityCreature) event.getEntity());
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

    dropKeystones(entity, keyStoneDropLevel);

    data.active = false;
    data.guardianSpawned = true;
  }

  private static void dropKeystones(EntityCreature guardian, int level) {
    double x = guardian.posX;
    double y = guardian.posY;
    double z = guardian.posZ;
    int roll = guardian.getRNG().nextInt(3) + 2;
    ItemStack stack = new ItemStack(ItemRiftKeyStone.INSTANCE, roll);
    NBTTagCompound c = new NBTTagCompound();
    c.setInteger(Rifts.NBT_RIFT_LEVEL, level);
    stack.setTagCompound(c);
    EntityItem itemEntity = new EntityItem(guardian.world, x, y, z, stack);
    guardian.world.spawnEntity(itemEntity);
  }

}
