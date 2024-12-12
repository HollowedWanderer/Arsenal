package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.ArsenalComponents;
import dev.doctor4t.arsenal.util.WeaponSkinsSupporterData;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.SyncToken;
import dev.upcraft.datasync.api.util.Entitlements;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface ArsenalCosmetics {
    Identifier WEAPON_SKINS_DATA_ID = Arsenal.id("weapon_skins");
    SyncToken<WeaponSkinsSupporterData> WEAPON_SKINS_DATA = DataSyncAPI.register(WeaponSkinsSupporterData.class, WEAPON_SKINS_DATA_ID, WeaponSkinsSupporterData.CODEC);

    static String getSkin(ItemStack itemStack) {
        UUID owner = ArsenalComponents.WEAPON_OWNER_COMPONENT.get(itemStack).getOwner();
        String itemName = itemStack.getItem().getName().getString().toLowerCase(Locale.ROOT);
        String stackName = itemStack.getName().getString().toLowerCase(Locale.ROOT);

        if (owner != null) {
            Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(owner);
            if (optional.isPresent()) {
                String serialized = optional.get().serialized();
                String[] namesAndSkins = serialized.split(";");
                for (String nameAndSkin : namesAndSkins) {
                    if (nameAndSkin.matches(itemName + "-" + stackName + ":.+")) {
                        String[] split = nameAndSkin.split(":");
                        return split[1];
                    }
                }
            }
        }

        return "default";
    }

    static void setSkin(UUID playerUuid, ItemStack itemStack, String skinName) {
        StringBuilder serializedBuilder = new StringBuilder();
        Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(playerUuid);
        String itemName = itemStack.getItem().getName().getString().toLowerCase(Locale.ROOT);
        String stackName = itemStack.getName().getString().toLowerCase(Locale.ROOT);

        String[] namesAndSkins = new String[]{};
        if (optional.isPresent()) {
            namesAndSkins = optional.get().serialized().split(";");
        }

        for (String nameAndSkin : namesAndSkins) {
            if (!nameAndSkin.matches(itemName + "-" + stackName + ":.+")) {
                serializedBuilder.append(nameAndSkin).append(";");
            }
        }

        serializedBuilder.append(itemName).append("-").append(stackName).append(":").append(skinName);
        String string = serializedBuilder.toString();
        WeaponSkinsSupporterData newData = new WeaponSkinsSupporterData(string);
        WEAPON_SKINS_DATA.setData(newData); // upload to server
    }

    static boolean isSupporter(UUID uuid) {
        Optional<Entitlements> entitlements = Entitlements.token().get(uuid);
        return entitlements.map(value -> value.keys().stream().anyMatch(identifier -> identifier.equals(WEAPON_SKINS_DATA_ID))).orElse(false);
    }
}
