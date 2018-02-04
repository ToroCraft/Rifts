package net.torocraft.rifts.entities.stray;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderStray;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderStrayRiftGuardian extends RenderStray {

  public static void init() {
    RenderingRegistry.registerEntityRenderingHandler(EntityStrayRiftGuardian.class,
        RenderStrayRiftGuardian::new);
  }

  public RenderStrayRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(AbstractSkeleton entity, float partialTickTime) {
    float scale = ((EntityStrayRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
