package net.torocraft.rifts.entities.zombie;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieRiftGuardian extends RenderZombie {

  public static void init() {
    RenderingRegistry.registerEntityRenderingHandler(EntityZombieRiftGuardian.class,
        RenderZombieRiftGuardian::new);
  }

  public RenderZombieRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(EntityZombie entity, float partialTickTime) {
    float scale = ((EntityZombieRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
