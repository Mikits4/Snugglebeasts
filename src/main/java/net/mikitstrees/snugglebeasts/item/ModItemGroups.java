package net.mikitstrees.snugglebeasts.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.mikitstrees.snugglebeasts.HeartsinHarmony;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup LOVELY_BIRDIES_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(HeartsinHarmony.MOD_ID, "hih_items"), FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.LOVEBIRD_SPAWN_EGG)) //group icon
                    .displayName(Text.translatable("itemGroup.heartsinharmony.hih_items"))
                    .entries((displayContext, entries) -> { //group entries into this group, yoinks it from ModBlocks
                        entries.add(ModItems.LOVEBIRD_SPAWN_EGG);
                    }).build());

    public static void registerItemGroups() {
        HeartsinHarmony.LOGGER.info("Registering Item Groups for " + HeartsinHarmony.MOD_ID);
    }
}
