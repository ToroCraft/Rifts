package net.torocraft.rifts.entities.zombieVillager;

import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.entities.RiftGuardian;
import net.torocraft.rifts.save.data.RiftData;

public class EntityZombieVillagerRiftGuardian extends EntityZombieVillager implements RiftGuardian {

  public static String NAME = Rifts.MODID + "_zombie_villager_guardian";

  private static final String NBT_SCALE = Rifts.MODID + "_scale";
  private static final DataParameter<Float> SCALE = EntityDataManager.createKey(
      EntityZombieVillagerRiftGuardian.class, DataSerializers.FLOAT);

  private float scale = 1;

  public static void init(int entityId) {
    EntityRegistry.registerModEntity(
        new ResourceLocation(Rifts.MODID, NAME),
        EntityZombieVillagerRiftGuardian.class, NAME, entityId,
        Rifts.INSTANCE, 60, 2,
        true, 0xFFFFFF, 0x000000);
  }

  public EntityZombieVillagerRiftGuardian(World worldIn) {
    super(worldIn);
  }

  protected void entityInit() {
    super.entityInit();
    dataManager.register(SCALE, 1f);
  }

  @Override
  public float getEyeHeight() {
    return scale * super.getEyeHeight();
  }

  @Override
  public float getScale() {
    float scale = dataManager.get(SCALE);
    if (world.isRemote && scale != this.scale) {
      setScale(scale);
    }
    return scale;
  }

  private void setScale(float scale) {
    if (scale < 1) {
      scale = 1;
    }
    dataManager.set(SCALE, scale);
    getEntityData().setFloat(NBT_SCALE, scale);
    multiplySize(scale);
    this.scale = scale;
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (!world.isRemote) {
      setScale(getEntityData().getFloat(NBT_SCALE));
    }
  }

  @Override
  public void setRift(RiftData data) {
    setScale(RiftGuardian.getScaleForLevel(data.level));
  }
}
