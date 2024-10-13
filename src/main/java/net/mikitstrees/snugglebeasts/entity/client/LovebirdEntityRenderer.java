package net.mikitstrees.snugglebeasts.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mikitstrees.snugglebeasts.Snugglebeasts;
import net.mikitstrees.snugglebeasts.entity.custom.LovebirdEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LovebirdEntityRenderer extends MobEntityRenderer<LovebirdEntity, LovebirdEntityModel> {
    private static final Identifier GREEN_TEXTURE = Identifier.of(Snugglebeasts.MOD_ID, "textures/entity/lovebird/green.png");
    private static final Identifier WHITE_TEXTURE = Identifier.of(Snugglebeasts.MOD_ID, "textures/entity/lovebird/white.png");

    public LovebirdEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new LovebirdEntityModel(context.getPart(EntityModelLayers.PARROT)), 0.3F);
    }

    public Identifier getTexture(LovebirdEntity parrotEntity) {
        return getTexture(parrotEntity.getVariant());
    }

    public static Identifier getTexture(LovebirdEntity.Variant variant) {
        return switch (variant) {
            case GREEN -> GREEN_TEXTURE;
            case WHITE -> WHITE_TEXTURE;
        };
    }

}
