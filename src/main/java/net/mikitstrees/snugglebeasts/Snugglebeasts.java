package net.mikitstrees.snugglebeasts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.mikitstrees.snugglebeasts.component.ModDataComponentTypes;
import net.mikitstrees.snugglebeasts.entity.ModEntities;
import net.mikitstrees.snugglebeasts.entity.custom.LovebirdEntity;

import net.mikitstrees.snugglebeasts.item.ModItemGroups;
import net.mikitstrees.snugglebeasts.item.ModItems;
import net.mikitstrees.snugglebeasts.worldgen.ModEntitySpawns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Very important comment
public class Snugglebeasts implements ModInitializer {
	public static final String MOD_ID = "snugglebeasts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();

		ModDataComponentTypes.registerDataComponentTypes();

		FabricDefaultAttributeRegistry.register(ModEntities.LOVEBIRD, LovebirdEntity.createLovebirdAttributes());

		ModEntitySpawns.addSpawns();
	}
}