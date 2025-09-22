package dev.doctor4t.arsenal.client;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.client.item.AnchorbladeSkinProperty;
import dev.doctor4t.arsenal.client.item.ScytheSkinProperty;
import dev.doctor4t.arsenal.client.particle.BloodBubbleParticle;
import dev.doctor4t.arsenal.client.particle.BloodBubbleSplatterParticle;
import dev.doctor4t.arsenal.client.particle.ShockwaveParticle;
import dev.doctor4t.arsenal.client.particle.SweepAttackParticle;
import dev.doctor4t.arsenal.client.render.entity.*;
import dev.doctor4t.arsenal.index.ArsenalEntities;
import dev.doctor4t.arsenal.index.ArsenalParticles;
import dev.doctor4t.arsenal.payload.BackWeaponSwapPayload;
import dev.doctor4t.arsenal.payload.IsRecallingPayload;
import dev.doctor4t.arsenal.payload.RecallNearbyTridentPayload;
import dev.doctor4t.arsenal.util.ArsenalConfig;
import dev.doctor4t.arsenal.util.ClientTickDelayScheduler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Properties;

@SuppressWarnings("unused")
public class ArsenalClient implements ClientModInitializer {

    public static KeyBinding weaponKeybind;
    public static KeyBinding swapKeybind;

    //IMPALED
    public int timeCharged = 0;
    public boolean isRecalling=false;
    @Override
    public void onInitializeClient() {

        // Register integrated resource pack
        FabricLoader.getInstance().getModContainer(Arsenal.MOD_ID).ifPresent(modContainer ->
                ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(Arsenal.MOD_ID, "classic"), modContainer, ResourcePackActivationType.NORMAL)
        );

        // Built-in Item Renderers
//        BuiltinItemRendererRegistry.INSTANCE.register(ArsenalItems.SCYTHE, new ScytheDynamicItemRenderer());
//        BuiltinItemRendererRegistry.INSTANCE.register(ArsenalItems.ANCHORBLADE, new AnchorbladeDynamicItemRenderer());
//        if (ArsenalConfig.CUSTOM_TRIDENT_RENDERING)
//            BuiltinItemRendererRegistry.INSTANCE.register(Items.TRIDENT, new TridentDynamicItemRenderer());

        // Force load the weapon models (otherwise since they're never called they wouldn't be loaded by default)
//        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(ScytheDynamicItemRenderer.MODELS_TO_REGISTER));
//        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(AnchorbladeDynamicItemRenderer.MODELS_TO_REGISTER));
//        if (ArsenalConfig.CUSTOM_TRIDENT_RENDERING)
//            ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(TridentDynamicItemRenderer.MODELS_TO_REGISTER));
//        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(WeaponRackEntityRenderer.MODEL));

        // model layers initialization
        ModEntityModelLayers.initialize();

        // entity renderers registration
        EntityRendererRegistry.register(ArsenalEntities.BLOOD_SCYTHE, BloodScytheEntityRenderer::new);
        EntityRendererRegistry.register(ArsenalEntities.ANCHORBLADE, AnchorbladeEntityRenderer::new);
        if (ArsenalConfig.CUSTOM_TRIDENT_RENDERING)
            EntityRendererRegistry.register(EntityType.TRIDENT, ArsenalTridentEntityRenderer::new);
        EntityRendererRegistry.register(ArsenalEntities.WEAPON_RACK, WeaponRackEntityRenderer::new);

        SelectProperties.ID_MAPPER.put(Arsenal.id("anchorblade_skin"), AnchorbladeSkinProperty.TYPE);
        SelectProperties.ID_MAPPER.put(Arsenal.id("scythe_skin"), ScytheSkinProperty.TYPE);

        // particle renderers registration
        ParticleFactoryRegistry.getInstance().register(ArsenalParticles.SWEEP_PARTICLE, SweepAttackParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArsenalParticles.BLOOD_BUBBLE, BloodBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArsenalParticles.BLOOD_BUBBLE_SPLATTER, BloodBubbleSplatterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArsenalParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);

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
            ClientTickDelayScheduler.tick();
            if (weaponKeybind.wasPressed() && client.player != null) {
                BackWeaponComponent.setHoldingBackWeapon(client.player, !BackWeaponComponent.isHoldingBackWeapon(client.player));
            }
            if (swapKeybind.wasPressed()) {
                ClientPlayNetworking.send(new BackWeaponSwapPayload());
            }
        });

        //IMPALED
        ClientTickEvents.END_CLIENT_TICK.register(client1 -> {
            if (client1.player != null) {
                boolean isHolding = client1.player.getMainHandStack().isEmpty() && client1.options.useKey.isPressed();
                if (isHolding) {
                    if (!isRecalling) {
                        isRecalling = true;
                        timeCharged = 0;
                        ClientPlayNetworking.send(new IsRecallingPayload());
                    }
                    timeCharged++;
                    if (timeCharged >= 30) {
                        ClientPlayNetworking.send(new RecallNearbyTridentPayload());
                        timeCharged = 0;
                    }
                } else {
                    if (isRecalling) {
                        isRecalling = false;
                        timeCharged = 0;
                        ClientPlayNetworking.send(new IsRecallingPayload());
                    }
                }
            }
        });
    }
}
