package net.mikitstrees.snugglebeasts.entity.client;

import net.mikitstrees.snugglebeasts.Snugglebeasts;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {
    public static final EntityModelLayer LOVEBIRD =
            new EntityModelLayer(Identifier.of(Snugglebeasts.MOD_ID, "lovebird"), "main");

}
