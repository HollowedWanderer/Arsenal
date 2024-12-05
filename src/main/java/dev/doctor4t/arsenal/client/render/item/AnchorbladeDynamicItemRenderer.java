package dev.doctor4t.arsenal.client.render.item;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.ArsenalComponents;
import dev.doctor4t.arsenal.cca.WeaponSkinComponent;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

public class AnchorbladeDynamicItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final Pair<ModelIdentifier, ModelIdentifier> DEFAULT_MODEL_IDENTIFIER = new Pair<>(new ModelIdentifier(Arsenal.id("anchorblade_inventory"), "inventory"), new ModelIdentifier(Arsenal.id("anchorblade_in_hand"), "inventory"));
    public static final Pair<ModelIdentifier, ModelIdentifier> LUX_MODEL_IDENTIFIER = new Pair<>(new ModelIdentifier(Arsenal.id("anchorblade_lux_inventory"), "inventory"), new ModelIdentifier(Arsenal.id("anchorblade_lux_in_hand"), "inventory"));

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean inHand = mode.isFirstPerson() || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || mode == ModelTransformationMode.HEAD || mode == ModelTransformationMode.FIXED;

        matrices.push();
        matrices.translate(.5, .5, .5);

        Pair<ModelIdentifier, ModelIdentifier> modelIdentifierPair = DEFAULT_MODEL_IDENTIFIER;
        WeaponSkinComponent weaponSkinComponent = ArsenalComponents.WEAPON_SKIN_COMPONENT.getNullable(stack);
        if (weaponSkinComponent != null && weaponSkinComponent.getSkin().equals("lux")) {
            modelIdentifierPair = LUX_MODEL_IDENTIFIER;
        }
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(!inHand ? modelIdentifierPair.getLeft() : modelIdentifierPair.getRight());

        if (!inHand) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, model);
        ((VertexConsumerProvider.Immediate) vertexConsumers).draw();

        if (!inHand) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        matrices.pop();
    }
}
