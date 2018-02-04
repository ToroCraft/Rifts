package net.torocraft.rifts.entities;

import net.torocraft.rifts.entities.husk.EntityHuskRiftGuardian;
import net.torocraft.rifts.entities.husk.RenderHuskRiftGuardian;
import net.torocraft.rifts.entities.pigZombie.EntityPigZombieRiftGuardian;
import net.torocraft.rifts.entities.pigZombie.RenderPigZombieRiftGuardian;
import net.torocraft.rifts.entities.skeleton.EntitySkeletonRiftGuardian;
import net.torocraft.rifts.entities.skeleton.RenderSkeletonRiftGuardian;
import net.torocraft.rifts.entities.stray.EntityStrayRiftGuardian;
import net.torocraft.rifts.entities.stray.RenderStrayRiftGuardian;
import net.torocraft.rifts.entities.zombie.EntityZombieRiftGuardian;
import net.torocraft.rifts.entities.zombie.RenderZombieRiftGuardian;
import net.torocraft.rifts.entities.zombieVillager.EntityZombieVillagerRiftGuardian;
import net.torocraft.rifts.entities.zombieVillager.RenderZombieVillagerRiftGuardian;

public class Entities {

  public static void init() {
    int entityId = 0;
    EntityZombieRiftGuardian.init(entityId++);
    EntityPigZombieRiftGuardian.init(entityId++);
    EntityZombieVillagerRiftGuardian.init(entityId++);
    EntityHuskRiftGuardian.init(entityId++);
    EntitySkeletonRiftGuardian.init(entityId++);
    EntityStrayRiftGuardian.init(entityId);
  }

  public static void registerRenders() {
    RenderZombieRiftGuardian.init();
    RenderPigZombieRiftGuardian.init();
    RenderZombieVillagerRiftGuardian.init();
    RenderHuskRiftGuardian.init();
    RenderSkeletonRiftGuardian.init();
    RenderStrayRiftGuardian.init();
  }
}
