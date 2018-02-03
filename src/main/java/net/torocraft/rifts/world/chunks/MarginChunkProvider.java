package net.torocraft.rifts.world.chunks;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

public class MarginChunkProvider implements IChunkGenerator {

  private final World world;

  public MarginChunkProvider(World worldIn) {
    world = worldIn;
  }

  @Override
  public Chunk generateChunk(int chunkX, int chunkZ) {
    ChunkPrimer chunkprimer = new ChunkPrimer();
    int xOffset = chunkX * 16;
    int zOffset = chunkZ * 16;
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 0; y < world.getActualHeight(); y++) {
          chunkprimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
        }
      }
    }
    return createChunk(chunkX, chunkZ, chunkprimer);
  }

  private Chunk createChunk(int chunkX, int chunkZ, ChunkPrimer chunkprimer) {
    Chunk chunk = new Chunk(world, chunkprimer, chunkX, chunkZ);
    chunk.generateSkylightMap();
    return chunk;
  }

  @Override
  public void populate(int chunkX, int chunkZ) {
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