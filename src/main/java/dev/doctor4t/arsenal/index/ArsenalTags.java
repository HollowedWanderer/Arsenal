package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface ArsenalTags {
    TagKey<Item> DISPLAYABLE = create("displayable");
    TagKey<Item> BIG_WEAPONS = create("big_weapons");
    TagKey<Item> RANGED_WEAPONS = create("ranged_weapons");
    TagKey<Item> SHIELDS = create("shields");
    TagKey<Item> TRIDENTS = create("tridents");

    private static TagKey<Item> create(String id) {
        return TagKey.of(RegistryKeys.ITEM, Arsenal.id(id));
    }
}
