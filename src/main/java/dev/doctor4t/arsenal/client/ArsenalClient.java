package dev.doctor4t.arsenal.client;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.client.render.entity.AnchorbladeEntityRenderer;
import dev.doctor4t.arsenal.client.render.entity.BloodScytheEntityRenderer;
import dev.doctor4t.arsenal.client.render.entity.ModEntityModelLayers;
import dev.doctor4t.arsenal.client.render.item.GUIHeldVaryingItemRenderer;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.index.ArsenalEntities;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.index.ArsenalParticles;
import dev.doctor4t.arsenal.util.WeaponSlotCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("unused")
public class ArsenalClient implements ClientModInitializer {
    public static ModelTransformationMode currentMode = ModelTransformationMode.NONE;

    static {
        for (var mode : ModelTransformationMode.values()) {
            ModelPredicateProviderRegistry.register(Arsenal.id(mode.name().toLowerCase()), (stack, world, entity, seed) -> mode == currentMode ? 1.0F : 0.0F);
        }
    }

    public static KeyBinding weaponKeybind;
    public static KeyBinding swapKeybind;

//    public static void registerGUIHandheldVaryingWeapon(Item item) {
//        Identifier weaponId = Registries.ITEM.getId(item);
//        GUIHeldVaryingItemRenderer GUIHeldVaryingItemRenderer = new GUIHeldVaryingItemRenderer(weaponId);
//        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(GUIHeldVaryingItemRenderer);
//        BuiltinItemRendererRegistry.INSTANCE.register(item, GUIHeldVaryingItemRenderer);
//        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
//            out.accept(new ModelIdentifier(weaponId.getNamespace(), weaponId.getPath() + "_gui", "inventory"));
//            out.accept(new ModelIdentifier(weaponId.getNamespace(), weaponId.getPath() + "_handheld", "inventory"));
//        });
//    }

    @Override
    public void onInitializeClient() {
        // custom item renderers registration
//        registerGUIHandheldVaryingWeapon(ArsenalItems.SCYTHE);
//        registerGUIHandheldVaryingWeapon(ArsenalItems.ANCHORBLADE);

        // model layers initialization
        ModEntityModelLayers.initialize();

        // entity renderers registration
        EntityRendererRegistry.register(ArsenalEntities.BLOOD_SCYTHE, BloodScytheEntityRenderer::new);
        EntityRendererRegistry.register(ArsenalEntities.ANCHORBLADE, AnchorbladeEntityRenderer::new);

        // particle renderers registration
        ArsenalParticles.registerFactories();

        // amy's bullshit sick ass custom weapon slot
        WeaponSlotCallback.EVENT.register((player, stack) -> {
            if (stack.getItem() == ArsenalItems.ANCHORBLADE) {
                return ActionResult.FAIL;
            }
            if (stack.getItem() == ArsenalItems.SCYTHE) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // keybindings
        weaponKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.arsenal.select_weapon",
                GLFW.GLFW_KEY_R,
                "category.arsenal"
        ));
        swapKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.arsenal.swap_weapon",
                GLFW.GLFW_KEY_G,
                "category.arsenal"
        ));

        // client tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (weaponKeybind.wasPressed() && client.player != null) {
                BackWeaponComponent.setHoldingBackWeapon(client.player, !BackWeaponComponent.isHoldingBackWeapon(client.player));
            }
            if (swapKeybind.wasPressed()) {
                ClientPlayNetworking.send(Arsenal.SERVERBOUND_SWAP_WEAPON_PACKET, PacketByteBufs.empty());
            }
        });
    }
}
