package net.torocraft.rifts.world.chunks;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.torocraft.rifts.world.RiftUtil;

public class RiftsChunkProvider implements IChunkGenerator {

  private final World world;

  private final EmptyChunkProvider empty;
  private final TeletoryChunkProvider teletory;
  private final FlatChunkProvider flat;
  private final BiomeChunkProvider biome;
  private final ChunkGeneratorHell hell;
  private final MarginChunkProvider margin;
  private final BiomedRiftChunkProvider overworld;

  public RiftsChunkProvider(World world) {
    this.world = world;
    empty = new EmptyChunkProvider(world);
    teletory = new TeletoryChunkProvider(world, world.getSeed());
    flat = new FlatChunkProvider(world);
    biome = new BiomeChunkProvider(world);
    hell = new ChunkGeneratorHell(world, false, world.getSeed());
    margin = new MarginChunkProvider(world);
    overworld = new BiomedRiftChunkProvider(world);
  }

  private IChunkGenerator getGenerator(int chunkX, int chunkZ) {
    int riftId = RiftUtil.getRiftIdForChunk(chunkX, chunkZ);

    if (riftId < 0) {
      return empty;
    }
//    if (riftId % 3 == 0) {
//      return flat;
//    }
//    if (riftId % 2 == 0) {
//      return teletory;
//    }
//    if (riftId % 1 == 0) {
//      return hell;
//    }

    return overworld;
  }

  @Override
  public Chunk generateChunk(int chunkX, int chunkZ) {
    return getGenerator(chunkX, chunkZ).generateChunk(chunkX, chunkZ);
  }

  @Override
  public void populate(int chunkX, int chunkZ) {
    getGenerator(chunkX, chunkZ).populate(chunkX, chunkZ);
  }

  @Override
  public boolean generateStructures(Chunk chunkIn, int chunkX, int chunkZ) {
    return getGenerator(chunkX, chunkZ).generateStructures(chunkIn, chunkX, chunkZ);
  }

  private static int[] toChunk(BlockPos pos) {
    return new int[]{
        MathHelper.floor(pos.getX() / 16),
        MathHelper.floor(pos.getZ() / 16)
    };
  }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType,
      BlockPos pos) {
    int[] chunk = toChunk(pos);
    return getGenerator(chunk[0], chunk[1]).getPossibleCreatures(creatureType, pos);
  }

  @Nullable
  @Override
  public BlockPos getNearestStructurePos(World world, String structureName, BlockPos pos,
      boolean findUnexplored) {
    int[] chunk = toChunk(pos);
    IChunkGenerator gen = getGenerator(chunk[0], chunk[1]);
    return gen.getNearestStructurePos(world, structureName, pos, findUnexplored);
  }

  @Override
  public void recreateStructures(Chunk chunkIn, int chunkX, int chunkZ) {
    getGenerator(chunkX, chunkZ).recreateStructures(chunkIn, chunkX, chunkZ);
  }

  @Override
  public boolean isInsideStructure(World world, String structureName, BlockPos pos) {
    int[] chunk = toChunk(pos);
    IChunkGenerator gen = getGenerator(chunk[0], chunk[1]);
    return gen.isInsideStructure(world, structureName, pos);
  }

}