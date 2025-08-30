package dev.doctor4t.arsenal;

import dev.doctor4t.arsenal.index.*;
import dev.doctor4t.arsenal.payload.*;
import dev.doctor4t.arsenal.util.BackStateResources;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Arsenal implements ModInitializer {
    public static final String MOD_ID = "arsenal";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ArsenalDataComponents.initialize();
        ArsenalEntities.initialize();
        ArsenalItems.initialize();
        //ArsenalEnchantments.initialize();
        ArsenalSounds.initialize();
        ArsenalParticles.initialize();
        ArsenalStatusEffects.initialize();

        PayloadTypeRegistry.playC2S().register(BackWeaponSwapPayload.ID, BackWeaponSwapPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(BackWeaponSwapPayload.ID, new BackWeaponSwapPayload.Receiver());
        PayloadTypeRegistry.playC2S().register(BackWeaponCreativePayload.ID, BackWeaponCreativePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(BackWeaponCreativePayload.ID, new BackWeaponCreativePayload.Receiver());
        PayloadTypeRegistry.playC2S().register(HoldWeaponPayload.ID, HoldWeaponPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(HoldWeaponPayload.ID, new HoldWeaponPayload.Receiver());
        PayloadTypeRegistry.playC2S().register(InventorySwapPayload.ID, InventorySwapPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(InventorySwapPayload.ID, new InventorySwapPayload.Receiver());

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new BackStateResources());

//        ServerPlayNetworking.registerGlobalReceiver(SERVERBOUND_SWAP_INVENTORY_PACKET, (server, player, handler, buf, responseSender) -> {
//            int slotId = buf.readInt();
//            if (!player.isSpectator()) {
//                if (!player.currentScreenHandler.isValid(slotId)) {
//                    return;
//                }
//                Slot slot = player.currentScreenHandler.getSlot(slotId);
//                ItemStack itemStack = BackWeaponComponent.getBackWeapon(player);
//                boolean success = BackWeaponComponent.setBackWeapon(player, slot.getStack());
//                if (success) {
//                    slot.setStack(itemStack);
//                }
//            }
//        });

        //IMPALED
        PayloadTypeRegistry.playC2S().register(RecallNearbyTridentPayload.ID, RecallNearbyTridentPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RecallNearbyTridentPayload.ID, new RecallNearbyTridentPayload.Receiver());
        PayloadTypeRegistry.playC2S().register(IsRecallingPayload.ID, IsRecallingPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(IsRecallingPayload.ID, new IsRecallingPayload.Receiver());
    }
}
