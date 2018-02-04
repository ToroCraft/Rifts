package net.torocraft.rifts.entities.husk;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHuskRiftGuardian extends RenderHusk {

  public static void init() {
    RenderingRegistry
        .registerEntityRenderingHandler(EntityHuskRiftGuardian.class, RenderHuskRiftGuardian::new);
  }

  public RenderHuskRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(EntityZombie entity, float partialTickTime) {
    float scale = ((EntityHuskRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
