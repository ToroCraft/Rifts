package net.torocraft.rifts.world.chunks;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.ForgeEventFactory;

public class BiomeChunkProvider implements IChunkGenerator {

  private static final IBlockState base = Blocks.STONE.getDefaultState();

  private final World world;
  private final Random random;
  private final Biome biome = Biome.getBiomeForId(1);

  public BiomeChunkProvider(World worldIn) {
    world = worldIn;
    random = new Random(0);
  }

  @Override
  public Chunk generateChunk(int chunkX, int chunkZ) {
    ChunkPrimer chunkprimer = new ChunkPrimer();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 1; y < 50; y++) {
          chunkprimer.setBlockState(x, y, z, base);

        }
      }
    }

    biome.generateBiomeTerrain(world, world.rand, chunkprimer, chunkX, chunkZ, 1);

    return createChunk(chunkX, chunkZ, chunkprimer);
  }


  private Chunk createChunk(int chunkX, int chunkZ, ChunkPrimer chunkprimer) {
    Chunk chunk = new Chunk(world, chunkprimer, chunkX, chunkZ);
    chunk.generateSkylightMap();
    return chunk;
  }

  @Override
  public void populate(int chunkX, int chunkZ) {
    BlockFalling.fallInstantly = true;
    ForgeEventFactory.onChunkPopulate(true, this, world, random, chunkX, chunkZ, false);
    ForgeEventFactory.onChunkPopulate(false, this, world, random, chunkX, chunkZ, false);
    BlockFalling.fallInstantly = false;
  }

  @Override
  public boolean generateStructures(Chunk chunkIn, int x, int z) {
    return false;
  }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
      boolean findUnexplored) {
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