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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.torocraft.rifts.save.RiftWorldSaveDataAccessor;
import net.torocraft.rifts.save.data.RiftData;
import net.torocraft.rifts.save.data.RiftType;
import net.torocraft.rifts.world.RiftUtil;

public class ChunkGeneratorOverworld implements IChunkGenerator {

  protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
  private final Random rand;
  private NoiseGeneratorOctaves minLimitPerlinNoise;
  private NoiseGeneratorOctaves maxLimitPerlinNoise;
  private NoiseGeneratorOctaves mainPerlinNoise;
  private NoiseGeneratorPerlin surfaceNoise;
  public NoiseGeneratorOctaves scaleNoise;
  public NoiseGeneratorOctaves depthNoise;
  public NoiseGeneratorOctaves forestNoise;
  private final World world;
  private final WorldType terrainType;
  private final double[] heightMap;
  private ChunkGeneratorSettings settings;
  private double[] depthBuffer = new double[256];

  double[] mainNoiseRegion;
  double[] minLimitRegion;
  double[] maxLimitRegion;
  double[] depthRegion;

  //private final Biome biome = Biomes.PLAINS;

  public ChunkGeneratorOverworld(World worldIn) {
    long seed = worldIn.getSeed();
    boolean mapFeaturesEnabledIn = false;

    this.world = worldIn;
    this.terrainType = worldIn.getWorldInfo().getTerrainType();
    this.rand = new Random(seed);
    this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
    this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
    this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
    this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
    this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
    this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
    this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
    this.heightMap = new double[825];

    net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld ctx =
        new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld(
            minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise,
            depthNoise, forestNoise);
    ctx = net.minecraftforge.event.terraingen.TerrainGen
        .getModdedNoiseGenerators(worldIn, this.rand, ctx);
    this.minLimitPerlinNoise = ctx.getLPerlin1();
    this.maxLimitPerlinNoise = ctx.getLPerlin2();
    this.mainPerlinNoise = ctx.getPerlin();
    this.surfaceNoise = ctx.getHeight();
    this.scaleNoise = ctx.getScale();
    this.depthNoise = ctx.getDepth();
    this.forestNoise = ctx.getForest();

    this.settings = ChunkGeneratorSettings.Factory.jsonToFactory("").build();
    worldIn.setSeaLevel(this.settings.seaLevel);
  }

  private Biome getBiome(int xIn, int zIn) {
    int riftId = RiftUtil.getRiftIdForChunk(xIn, zIn);
    return RiftType.forRift(world.getSeed(), riftId).getBiome();
  }

  private void generateHeightmap(int xIn, int zIn) {
    Biome biome = getBiome(xIn, zIn);

    int yIn = 0;
    depthRegion = depthNoise.generateNoiseOctaves(
        depthRegion,
        xIn, zIn, 5, 5,
        (double) settings.depthNoiseScaleX,
        (double) settings.depthNoiseScaleZ,
        (double) settings.depthNoiseScaleExponent
    );

    float f = settings.coordinateScale;
    float f1 = settings.heightScale;

    mainNoiseRegion = mainPerlinNoise.generateNoiseOctaves(
        mainNoiseRegion, xIn, yIn, zIn, 5, 33, 5,
        (double) (f / settings.mainNoiseScaleX),
        (double) (f1 / settings.mainNoiseScaleY),
        (double) (f / settings.mainNoiseScaleZ)
    );

    minLimitRegion = minLimitPerlinNoise.generateNoiseOctaves(
        minLimitRegion, xIn, yIn, zIn, 5, 33, 5,
        (double) f, (double) f1, (double) f
    );

    maxLimitRegion = maxLimitPerlinNoise.generateNoiseOctaves(
        maxLimitRegion, xIn, yIn, zIn, 5, 33, 5,
        (double) f, (double) f1, (double) f
    );

    int i = 0;
    int j = 0;

    for (int k = 0; k < 5; ++k) {
      for (int l = 0; l < 5; ++l) {
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        int i1 = 2;

        for (int j1 = -2; j1 <= 2; ++j1) {
          for (int k1 = -2; k1 <= 2; ++k1) {
            Biome biome1 = biome;
            float f5 = settings.biomeDepthOffSet
                + biome1.getBaseHeight() * settings.biomeDepthWeight;
            float f6 = settings.biomeScaleOffset
                + biome1.getHeightVariation() * settings.biomeScaleWeight;

            if (terrainType == WorldType.AMPLIFIED && f5 > 0.0F) {
              f5 = 1.0F + f5 * 2.0F;
              f6 = 1.0F + f6 * 4.0F;
            }

            float f7 = 1;

            f2 += f6 * f7;
            f3 += f5 * f7;
            f4 += f7;
          }
        }

        f2 = f2 / f4;
        f3 = f3 / f4;
        f2 = f2 * 0.9F + 0.1F;
        f3 = (f3 * 4.0F - 1.0F) / 8.0F;
        double d7 = depthRegion[j] / 8000.0D;

        if (d7 < 0.0D) {
          d7 = -d7 * 0.3D;
        }

        d7 = d7 * 3.0D - 2.0D;

        if (d7 < 0.0D) {
          d7 = d7 / 2.0D;

          if (d7 < -1.0D) {
            d7 = -1.0D;
          }

          d7 = d7 / 1.4D;
          d7 = d7 / 2.0D;
        } else {
          if (d7 > 1.0D) {
            d7 = 1.0D;
          }

          d7 = d7 / 8.0D;
        }

        int baseSize = 10;

        ++j;
        double d8 = (double) f3;
        double d9 = (double) f2;
        d8 = d8 + d7 * 0.2D;
        d8 = d8 * (double) baseSize / 8.0D;
        double d0 = (double) baseSize + d8 * 4.0D;

        for (int l1 = 0; l1 < 33; ++l1) {
          double d1 = ((double) l1 - d0) * (double) settings.stretchY * 128.0D / 256.0D / d9;

          if (d1 < 0.0D) {
            d1 *= 4.0D;
          }

          double d2 = minLimitRegion[i] / (double) settings.lowerLimitScale;
          double d3 = maxLimitRegion[i] / (double) settings.upperLimitScale;
          double d4 = (mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
          double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

          if (l1 > 29) {
            double d6 = (double) ((float) (l1 - 29) / 3.0F);
            d5 = d5 * (1.0D - d6) + -10.0D * d6;
          }

          heightMap[i] = d5;
          ++i;
        }
      }
    }
  }

  public void setBlocksInChunk(int xIn, int zIn, ChunkPrimer primer) {
    int riftId = RiftUtil.getRiftIdForChunk(xIn, zIn);
    generateHeightmap(xIn * 4, zIn * 4);

    for (int i = 0; i < 4; ++i) {
      int j = i * 5;
      int k = (i + 1) * 5;

      for (int l = 0; l < 4; ++l) {
        int i1 = (j + l) * 33;
        int j1 = (j + l + 1) * 33;
        int k1 = (k + l) * 33;
        int l1 = (k + l + 1) * 33;

        for (int i2 = 0; i2 < 32; ++i2) {
          double d0 = 0.125D;
          double d1 = this.heightMap[i1 + i2];
          double d2 = this.heightMap[j1 + i2];
          double d3 = this.heightMap[k1 + i2];
          double d4 = this.heightMap[l1 + i2];
          double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
          double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
          double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
          double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;

          for (int j2 = 0; j2 < 8; ++j2) {
            double d9 = 0.25D;
            double d10 = d1;
            double d11 = d2;
            double d12 = (d3 - d1) * 0.25D;
            double d13 = (d4 - d2) * 0.25D;

            int y = i2 * 8 + j2;

            for (int k2 = 0; k2 < 4; ++k2) {

              int x = i * 4 + k2;

              double d14 = 0.25D;
              double nextStep = (d11 - d10) * 0.25D;
              double currentLevel = d10 - nextStep;

              for (int l2 = 0; l2 < 4; ++l2) {

                int z = l * 4 + l2;

                if ((currentLevel += nextStep) > 0.0D) {
                  if (isInRift(xIn, zIn, riftId, x, z)) {
                    primer.setBlockState(x, y, z, STONE);
                  }
                }
              }

              d10 += d12;
              d11 += d13;
            }

            d1 += d5;
            d2 += d6;
            d3 += d7;
            d4 += d8;
          }
        }
      }
    }
  }

  private static final int RIFT_END = (RiftUtil.RIFT_SIZE * 16) / 2;
  private static final int RIFT_BEGINNING_OF_END = RIFT_END - 30;

  private boolean isInRift(int xIn, int zIn, int riftId, int x, int z) {
    double distance = RiftUtil.distanceFromOrigin(riftId, x + (xIn * 16), z + (zIn * 16));
    if (distance < RIFT_BEGINNING_OF_END) {
      return true;
    }
    if (distance > RIFT_END) {
      return false;
    }
    double chance = (RIFT_END - distance) / (RIFT_END - RIFT_BEGINNING_OF_END);
    return chance > rand.nextDouble();
  }

  public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer) {
    Biome biome = getBiome(x, z);

    double d0 = 0.03125D;
    this.depthBuffer = this.surfaceNoise
        .getRegion(this.depthBuffer, (double) (x * 16), (double) (z * 16), 16, 16, 0.0625D, 0.0625D,
            1.0D);

    for (int i = 0; i < 16; ++i) {
      for (int j = 0; j < 16; ++j) {
        biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j,
            this.depthBuffer[j + i * 16]);
      }
    }
  }

  @Override
  public Chunk generateChunk(int x, int z) {
    this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
    ChunkPrimer chunkprimer = new ChunkPrimer();
    this.setBlocksInChunk(x, z, chunkprimer);
    this.replaceBiomeBlocks(x, z, chunkprimer);

    Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
    chunk.generateSkylightMap();
    return chunk;
  }

  @Override
  public void populate(int x, int z) {
    Biome biome = getBiome(x, z);

    BlockFalling.fallInstantly = true;
    int i = x * 16;
    int j = z * 16;
    BlockPos blockpos = new BlockPos(i, 0, j);
    this.rand.setSeed(this.world.getSeed());
    long k = this.rand.nextLong() / 2L * 2L + 1L;
    long l = this.rand.nextLong() / 2L * 2L + 1L;
    this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
    ChunkPos chunkpos = new ChunkPos(x, z);

    net.minecraftforge.event.ForgeEventFactory
        .onChunkPopulate(true, this, this.world, this.rand, x, z, false);
/*
    if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes
        && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
      if (net.minecraftforge.event.terraingen.TerrainGen
          .populate(this, this.world, this.rand, x, z, flag,
              net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
        int i1 = this.rand.nextInt(16) + 8;
        int j1 = this.rand.nextInt(256);
        int k1 = this.rand.nextInt(16) + 8;
        (new WorldGenLakes(Blocks.WATER))
            .generate(this.world, this.rand, blockpos.add(i1, j1, k1));
      }
    }

    if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0
        && this.settings.useLavaLakes) {
      if (net.minecraftforge.event.terraingen.TerrainGen
          .populate(this, this.world, this.rand, x, z, flag,
              net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA)) {
        int i2 = this.rand.nextInt(16) + 8;
        int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
        int k3 = this.rand.nextInt(16) + 8;

        if (l2 < this.world.getSeaLevel()
            || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
          (new WorldGenLakes(Blocks.LAVA))
              .generate(this.world, this.rand, blockpos.add(i2, l2, k3));
        }
      }
    }

    if (this.settings.useDungeons) {
      if (net.minecraftforge.event.terraingen.TerrainGen
          .populate(this, this.world, this.rand, x, z, flag,
              net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON)) {
        for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2) {
          int i3 = this.rand.nextInt(16) + 8;
          int l3 = this.rand.nextInt(256);
          int l1 = this.rand.nextInt(16) + 8;
          (new WorldGenDungeons())
              .generate(this.world, this.rand, blockpos.add(i3, l3, l1));
        }
      }
    }
*/
    biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

//    if (net.minecraftforge.event.terraingen.TerrainGen
//        .populate(this, this.world, this.rand, x, z, flag,
//            net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS)) {
//      WorldEntitySpawner
//          .performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
//    }
    //   blockpos = blockpos.add(8, 0, 8);

    /*
    if (net.minecraftforge.event.terraingen.TerrainGen
        .populate(this, this.world, this.rand, x, z, flag,
            net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE)) {
      for (int k2 = 0; k2 < 16; ++k2) {
        for (int j3 = 0; j3 < 16; ++j3) {
          BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
          BlockPos blockpos2 = blockpos1.down();

          if (this.world.canBlockFreezeWater(blockpos2)) {
            this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
          }

          if (this.world.canSnowAt(blockpos1, true)) {
            this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
          }
        }
      }
    }//Forge: End ICE
    */

    BlockFalling.fallInstantly = false;
  }

  @Override
  public boolean generateStructures(Chunk chunkIn, int x, int z) {
    return false;
  }

  @Override
  public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType,
      BlockPos pos) {

    return Collections.emptyList(); // biome.getSpawnableList(creatureType);
  }

  @Override
  public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
    return false;
  }

  @Override
  @Nullable
  public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
      boolean findUnexplored) {
    return null;
  }

  /**
   * Recreates data about structures intersecting given chunk (used for example by
   * getPossibleCreatures), without placing any blocks. When called for the first time before any
   * chunk is generated - also initializes the internal state needed by getPossibleCreatures.
   */
  @Override
  public void recreateStructures(Chunk chunkIn, int x, int z) {
  }
}