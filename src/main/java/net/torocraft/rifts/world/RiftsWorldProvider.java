package net.torocraft.rifts.world;

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

public class RiftsWorldProvider extends WorldProvider {

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
		return new Vec3d(0D, 0D, 0D);
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new RiftsChunkProvider(world, world.getSeed());
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
	public boolean isSkyColored() {
		return false;
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
		return true;
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