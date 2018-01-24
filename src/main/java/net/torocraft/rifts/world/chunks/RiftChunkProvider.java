package net.torocraft.rifts.world.chunks;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class RiftChunkProvider implements IChunkGenerator {

  private final World worldObj;

  public RiftChunkProvider(World worldIn) {
    this.worldObj = worldIn;
  }

  @Override
  public Chunk generateChunk(int x, int z) {
    return null;
  }

  @Override
  public void populate(int x, int z) {

  }

  @Override
  public boolean generateStructures(Chunk chunkIn, int x, int z) {
    return false;
  }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType,
      BlockPos pos) {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
      boolean findUnexplored) {
    return null;
  }

  @Nullable
  public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
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