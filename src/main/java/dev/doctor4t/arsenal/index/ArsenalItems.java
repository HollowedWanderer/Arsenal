package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.item.AnchorbladeItem;
import dev.doctor4t.arsenal.item.ScytheItem;
import dev.doctor4t.arsenal.item.WeaponRackItem;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface ArsenalItems {

    Item SCYTHE = create("scythe", new Item.Settings().component(ArsenalDataComponents.SCYTHE_SKIN, ScytheSkin.DEFAULT).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).component(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.builder()
            .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 9.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -3.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_range"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .build()), ScytheItem::new);
    Item ANCHORBLADE = create("anchorblade", new Item.Settings().component(ArsenalDataComponents.ANCHORBLADE_SKIN, AnchorbladeSkin.DEFAULT).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).pickaxe(ToolMaterial.NETHERITE, 5.0F, -3.0F), AnchorbladeItem::new);
    Item WEAPON_RACK = create("weapon_rack", new Item.Settings(), WeaponRackItem::new);

    static <T extends Item> T create(String name, Item.Settings settings, Function<Item.Settings, T> itemConstructor) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Arsenal.id(name));
        return Registry.register(Registries.ITEM, key, itemConstructor.apply(settings.registryKey(key)));
    }

    static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ArsenalItems::addCombatEntries);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ArsenalItems::addFunctionalEntries);
    }

    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TRIDENT, SCYTHE);
        fabricItemGroupEntries.addAfter(SCYTHE, ANCHORBLADE);
    }

    private static void addFunctionalEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.GLOW_ITEM_FRAME, WEAPON_RACK);
    }
}
