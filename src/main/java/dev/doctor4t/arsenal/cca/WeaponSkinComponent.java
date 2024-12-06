package dev.doctor4t.arsenal.cca;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

public class WeaponSkinComponent extends ItemComponent {
    private static final String SKIN = "skin";

    public WeaponSkinComponent(ItemStack stack) {
        super(stack);
    }

    public String getSkinName() {
        if (!this.hasTag(SKIN, NbtElement.STRING_TYPE)) this.putString(SKIN, "");
        return this.getString(SKIN);
    }

    public void setSkin(String skin) {
        this.putString(SKIN, skin);
    }
}
