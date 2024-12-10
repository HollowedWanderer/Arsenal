package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ArsenalTags {
    public static final TagKey<Item> DISPLAYABLE = create("displayable");
    public static final TagKey<Item> BIG_WEAPONS = create("big_weapons");
    public static final TagKey<Item> RANGED_WEAPONS = create("ranged_weapons");
    public static final TagKey<Item> SHIELDS = create("shields");
    public static final TagKey<Item> TRIDENTS = create("tridents");

    private static TagKey<Item> create(String id) {
        return TagKey.of(RegistryKeys.ITEM, Arsenal.id(id));
    }
}
