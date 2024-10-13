package net.mikitstrees.snugglebeasts.entity;

import net.mikitstrees.snugglebeasts.HeartsinHarmony;
import net.mikitstrees.snugglebeasts.entity.custom.LovebirdEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<LovebirdEntity> LOVEBIRD = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(HeartsinHarmony.MOD_ID, "lovebird"),
            EntityType.Builder.create(LovebirdEntity::new, SpawnGroup.CREATURE).dimensions(1f, 2.5f).build());

    public static void registerModEntities() {
        HeartsinHarmony.LOGGER.info("Registering Mod Entities for " + HeartsinHarmony.MOD_ID);
    }
}

