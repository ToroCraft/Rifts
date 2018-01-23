package net.torocraft.rifts.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

public class RiftsChunkProvider implements IChunkGenerator {

	private final World world;
	private final Random random;

	private final int xSize = 16;
	private final int zSize = 16;

	private final int xScale = 5;
	private final int yScale = 6;
	private final int zScale = 15;

	private final int yOffset = 0;

	private final IBlockState base;

	public RiftsChunkProvider(World worldIn, long seed) {
		world = worldIn;
		random = new Random(seed);
		base = Blocks.END_STONE.getDefaultState();
	}

	@Override
	public Chunk generateChunk(int chunkX, int chunkZ) {
		ChunkPrimer chunkprimer = new ChunkPrimer();
		int xOffset = chunkX * 16;
		int zOffset = chunkZ * 16;
		drawChunk(chunkX, chunkZ, chunkprimer);
		return createChunk(chunkX, chunkZ, chunkprimer);
	}

	private void drawChunk(int chunkX, int chunkZ, ChunkPrimer chunkprimer) {
		if (chunkX % 2 == 0 || chunkZ % 2 == 0) {
			return;
		}
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 10; y < 11; y++) {
					chunkprimer.setBlockState(x, y, z, base);
				}
			}
		}
	}

	private Chunk createChunk(int chunkX, int chunkZ, ChunkPrimer chunkprimer) {
		Chunk chunk = new Chunk(world, chunkprimer, chunkX, chunkZ);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int chunkX, int chunkZ) {
		net.minecraft.block.BlockFalling.fallInstantly = true;
		setChunkSeed(chunkX, chunkZ);
		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.random, chunkX, chunkZ, false);
		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.random, chunkX, chunkZ, false);
		net.minecraft.block.BlockFalling.fallInstantly = false;
	}

	protected void setChunkSeed(int chunkX, int chunkZ) {
		random.setSeed(this.world.getSeed());
		long k = random.nextLong() / 2L * 2L + 1L;
		long l = random.nextLong() / 2L * 2L + 1L;
		random.setSeed((long) chunkX * k + (long) chunkZ * l ^ this.world.getSeed());
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return new ArrayList<>(0);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
	}

}