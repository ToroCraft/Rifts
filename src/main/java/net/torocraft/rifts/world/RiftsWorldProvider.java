package net.torocraft.rifts.world;

import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.rifts.Rifts;
import net.torocraft.rifts.world.chunks.RiftsChunkProvider;

public class RiftsWorldProvider extends WorldProvider {

  private static final Vec3d COLOR = new Vec3d(0.1, 0D, 0.2D);

  public RiftsWorldProvider() {
    this.biomeProvider = new BiomeProviderSingle(Biomes.VOID);
    this.hasSkyLight = false;
  }

  public DimensionType getDimensionType() {
    return Rifts.RIFT_DIM_TYPE;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Vec3d getFogColor(float par1, float par2) {
    return COLOR;
  }

  @Override
  public IChunkGenerator createChunkGenerator() {
    return new RiftsChunkProvider(world);
  }

  @Override
  public boolean isSurfaceWorld() {
    return false;
  }

  @Override
  public boolean canCoordinateBeSpawn(int par1, int par2) {
    return false;
  }

  @Override
  public boolean canRespawnHere() {
    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean doesXZShowFog(int par1, int par2) {
    return true;
  }

  @Override
  public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
    return COLOR;
  }

  @Override
  public boolean isSkyColored() {
    return true;
  }

  @Override
  public boolean hasSkyLight() {
    return true;
  }

  @Override
  public IRenderHandler getWeatherRenderer() {
    return null;
  }

  @Override
  public boolean isDaytime() {
    return false;
  }

  @Override
  public float getSunBrightness(float par1) {
    return 1;
  }

  @Override
  public float getStarBrightness(float par1) {
    return 1;
  }

  @Override
  public void updateWeather() {

  }

  @Override
  public void onWorldUpdateEntities() {

  }

}