package net.mikitstrees.snugglebeasts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.mikitstrees.snugglebeasts.entity.ModEntities;
import net.mikitstrees.snugglebeasts.entity.client.LovebirdEntityModel;
import net.mikitstrees.snugglebeasts.entity.client.LovebirdEntityRenderer;
import net.mikitstrees.snugglebeasts.entity.client.ModEntityModelLayers;

public class SnugglebeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.LOVEBIRD, LovebirdEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.LOVEBIRD, LovebirdEntityModel::getTexturedModelData);
    }
}
