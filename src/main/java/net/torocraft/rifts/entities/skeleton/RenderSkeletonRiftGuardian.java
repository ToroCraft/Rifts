package net.torocraft.rifts.entities.skeleton;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkeletonRiftGuardian extends RenderSkeleton {

  public static void init() {
    RenderingRegistry
        .registerEntityRenderingHandler(EntitySkeletonRiftGuardian.class,
            RenderSkeletonRiftGuardian::new);
  }

  public RenderSkeletonRiftGuardian(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }

  @Override
  protected void preRenderCallback(AbstractSkeleton entity, float partialTickTime) {
    float scale = ((EntitySkeletonRiftGuardian) entity).getScale();
    GlStateManager.scale(scale, scale, scale);
  }
}
