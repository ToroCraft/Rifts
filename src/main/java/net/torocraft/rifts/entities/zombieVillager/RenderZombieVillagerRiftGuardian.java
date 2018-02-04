package net.torocraft.rifts.entities.zombieVillager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombieVillager;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieVillagerRiftGuardian extends RenderZombieVillager {

  public static void init() {
    RenderingRegistry.registerEntityRenderingHandler(
        EntityZombieVillagerRiftGuardian.class, RenderZombieVillagerRiftGuardian::new);
  }

  public RenderZombieVillagerRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(EntityZombieVillager entity, float partialTickTime) {
    float scale = ((EntityZombieVillagerRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
