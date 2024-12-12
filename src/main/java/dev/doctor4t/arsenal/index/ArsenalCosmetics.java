package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.WeaponOwnerComponent;
import dev.doctor4t.arsenal.util.WeaponSkinsSupporterData;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.SyncToken;
import dev.upcraft.datasync.api.util.Entitlements;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public interface ArsenalCosmetics {
    Identifier WEAPON_SKINS_DATA_ID = Arsenal.id("weapon_skins");
    SyncToken<WeaponSkinsSupporterData> WEAPON_SKINS_DATA = DataSyncAPI.register(WeaponSkinsSupporterData.class, WEAPON_SKINS_DATA_ID, WeaponSkinsSupporterData.CODEC);

    static String getSkin(UUID playerUuid, String stackName) {
        Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(playerUuid);
        if (optional.isPresent()) {
            String serialized = optional.get().serialized();
            String[] namesAndSkins = serialized.split(";");
            for (String nameAndSkin : namesAndSkins) {
                if (nameAndSkin.matches(stackName + ":.+")) {
                    String[] split = nameAndSkin.split(":");
                    return split[1];
                }
            }
        }

        return "default";
    }

    static void setSkin(UUID playerUuid, String stackName, String skinName) {
        StringBuilder serializedBuilder = new StringBuilder();
        Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(playerUuid);

        String[] namesAndSkins = new String[]{};
        if (optional.isPresent()) {
            namesAndSkins = optional.get().serialized().split(";");
        }

        for (String nameAndSkin : namesAndSkins) {
            if (!nameAndSkin.matches(stackName + ":.+")) {
                serializedBuilder.append(nameAndSkin).append(";");
            }
        }

        serializedBuilder.append(stackName).append(":").append(skinName);
        String string = serializedBuilder.toString();
        WeaponSkinsSupporterData newData = new WeaponSkinsSupporterData(string);
        WEAPON_SKINS_DATA.setData(newData); // upload to server
    }

    static String getSkinFromStack(ItemStack stack, WeaponOwnerComponent weaponOwnerComponent) {
        return ArsenalCosmetics.getSkin(weaponOwnerComponent.getOwner(), stack.getName().getString());
    }

    static boolean isSupporter(UUID uuid) {
        Optional<Entitlements> entitlements = Entitlements.token().get(uuid);
        return entitlements.map(value -> value.keys().stream().anyMatch(identifier -> identifier.equals(WEAPON_SKINS_DATA_ID))).orElse(false);
    }
}
