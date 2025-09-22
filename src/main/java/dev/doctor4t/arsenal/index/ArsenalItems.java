package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.item.AnchorbladeItem;
import dev.doctor4t.arsenal.item.ScytheItem;
import dev.doctor4t.arsenal.item.WeaponRackItem;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import dev.doctor4t.ratatouille.util.TextUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface ArsenalItems {

    Item SCYTHE = create("scythe", new Item.Settings().component(ArsenalDataComponents.SCYTHE_SKIN, ScytheSkin.DEFAULT).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).component(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 9.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -3.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_range"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
            .build()).maxCount(1), ScytheItem::new);
    Item ANCHORBLADE = create("anchorblade", new Item.Settings().component(ArsenalDataComponents.ANCHORBLADE_SKIN, AnchorbladeSkin.DEFAULT).component(DataComponentTypes.WEAPON, new WeaponComponent(1)).pickaxe(ToolMaterial.NETHERITE, 5.0F, -3.0F), AnchorbladeItem::new);
    Item WEAPON_RACK = create("weapon_rack", new Item.Settings(), WeaponRackItem::new);

    static <T extends Item> T create(String name, Item.Settings settings, Function<Item.Settings, T> itemConstructor) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Arsenal.id(name));
        return Registry.register(Registries.ITEM, key, itemConstructor.apply(settings.registryKey(key)));
    }

    static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ArsenalItems::addCombatEntries);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ArsenalItems::addFunctionalEntries);

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            if (itemStack.isOf(ArsenalItems.ANCHORBLADE)) {
                AnchorbladeSkin skin = AnchorbladeSkin.fromString(ArsenalCosmetics.getSkin(itemStack));

                if (skin != null && skin != AnchorbladeSkin.DEFAULT) {
                    list.add(1, Text.literal(skin.tooltipName != null ? skin.tooltipName : TextUtils.formatValueString(skin.asString())).styled(style -> style.withColor(skin.getFirstColor())));
                    if (skin.lore != null) {
                        if (Screen.hasShiftDown()) {
                            MutableText translatable = Text.translatable(skin.lore);
                            for (String line : translatable.getString().split("\n")) {
                                list.add(2, Text.literal(line).styled(style -> style.withColor(Formatting.DARK_GRAY)));
                            }
                        } else {
                            list.add(2, Text.translatable("tooltip.arsenal.hidden").styled(style -> style.withColor(Formatting.DARK_GRAY)));
                        }
                    }
                }
            }
            if (itemStack.isOf(ArsenalItems.SCYTHE)) {
                ScytheSkin skin = ScytheSkin.fromString(ArsenalCosmetics.getSkin(itemStack));

                if (skin != null && skin != ScytheSkin.DEFAULT) {
                    list.add(1, Text.literal(skin.tooltipName != null ? skin.tooltipName : TextUtils.formatValueString(skin.asString())).styled(style -> style.withColor(skin.color)));
                    if (skin.lore != null) {
                        if (Screen.hasShiftDown()) {
                            MutableText translatable = Text.translatable(skin.lore);
                            for (String line : translatable.getString().split("\n")) {
                                list.add(2, Text.literal(line).styled(style -> style.withColor(Formatting.DARK_GRAY)));
                            }
                        } else {
                            list.add(2, Text.translatable("tooltip.arsenal.hidden").styled(style -> style.withColor(Formatting.DARK_GRAY)));
                        }
                    }
                }
            }
        });
    }

    private static void addCombatEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.TRIDENT, SCYTHE);
        fabricItemGroupEntries.addAfter(SCYTHE, ANCHORBLADE);
    }

    private static void addFunctionalEntries(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.addAfter(Items.GLOW_ITEM_FRAME, WEAPON_RACK);
    }
}
