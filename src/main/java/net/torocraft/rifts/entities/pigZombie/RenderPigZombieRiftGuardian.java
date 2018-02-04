package net.torocraft.rifts.entities.pigZombie;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigZombieRiftGuardian extends RenderPigZombie {

  public static void init() {
    RenderingRegistry
        .registerEntityRenderingHandler(EntityPigZombieRiftGuardian.class,
            RenderPigZombieRiftGuardian::new);
  }

  public RenderPigZombieRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(EntityPigZombie entity, float partialTickTime) {
    float scale = ((EntityPigZombieRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
