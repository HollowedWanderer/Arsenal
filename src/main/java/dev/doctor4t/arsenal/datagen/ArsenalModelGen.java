package dev.doctor4t.arsenal.datagen;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.client.item.AnchorbladeSkinProperty;
import dev.doctor4t.arsenal.client.item.ScytheSkinProperty;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.model.Model;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArsenalModelGen extends FabricModelProvider {
    public static final Model BIG_WEAPON_IN_HAND = model("item/template_big_weapon_in_hand", "_in_hand", TextureKey.LAYER0);
    public static final Model TRIDENT_IN_HAND = model("item/template_trident_in_hand", "_in_hand", TextureKey.LAYER0);

    public ArsenalModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        this.registerWeapon(generator, ArsenalItems.ANCHORBLADE, BIG_WEAPON_IN_HAND, AnchorbladeSkinProperty.INSTANCE, AnchorbladeSkin.values(), AnchorbladeSkin.DEFAULT, List.of(AnchorbladeSkin.AMBESSA));
        this.registerWeapon(generator, ArsenalItems.SCYTHE, BIG_WEAPON_IN_HAND, ScytheSkinProperty.INSTANCE, ScytheSkin.values(), ScytheSkin.DEFAULT, List.of());

        registerTemplateWeaponHandheld(TRIDENT_IN_HAND, null, Arsenal.id("trident"), generator);
        registerTemplateWeaponInventory(TRIDENT_IN_HAND, null, Arsenal.id("trident"), ModelIds.getItemModelId(Items.TRIDENT), generator);

        generator.register(ArsenalItems.WEAPON_RACK, Models.GENERATED);
    }

    private <T extends StringIdentifiable, P extends SelectProperty<T>> void registerWeapon(ItemModelGenerator generator, Item item, Model handModel, P property, T[] skins, T defaultSkin, List<T> excludeFromHandModelGen) {
        List<SelectItemModel.SwitchCase<T>> cases = new ArrayList<>();
        for (T skin : skins) {
            if (excludeFromHandModelGen.contains(skin)) {
                registerTemplateWeaponInventory(handModel, skin == defaultSkin ? null : skin.asString(), item, generator);
            } else {
                registerTemplateWeapon(handModel, skin == defaultSkin ? null : skin.asString(), item, generator);
            }
            if (skin != defaultSkin) {
                cases.add(ItemModels.switchCase(skin, this.createWeaponModel(item, skin.asString())));
            }
        }
        generator.output.accept(
                item,
                ItemModels.select(
                        property,
                        this.createWeaponModel(item, null),
                        cases
                )
        );
    }

    private ItemModel.Unbaked createWeaponModel(Item item, @Nullable String name) {
        String suffix = name == null ? "" : "_" + name;
        Identifier handModelId = ModelIds.getItemSubModelId(item, suffix + "_in_hand");
        Identifier inventoryModelId = ModelIds.getItemSubModelId(item, suffix + "_inventory");
        return ItemModelGenerator.createModelWithInHandVariant(ItemModels.basic(inventoryModelId), ItemModels.basic(handModelId));
    }

    private static Model model(String parent, @Nullable String variant, TextureKey... keys) {
        return new Model(Optional.of(Arsenal.id(parent)), Optional.ofNullable(variant), keys);
    }

    private static Model model(String parent, TextureKey... keys) {
        return model(parent, null, keys);
    }

    private void registerTemplateWeapon(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        this.registerTemplateWeaponHandheld(templateModel, name, item, generator);
        this.registerTemplateWeaponInventory(templateModel, name, item, generator);
    }

    private void registerTemplateWeapon(Model templateModel, @Nullable String name, Identifier itemId, ItemModelGenerator generator) {
        this.registerTemplateWeaponHandheld(templateModel, name, itemId, generator);
        this.registerTemplateWeaponInventory(templateModel, name, itemId, generator);
    }

    private void registerTemplateWeaponHandheld(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        registerTemplateWeaponHandheld(templateModel, name, Registries.ITEM.getId(item), generator);
    }

    private void registerTemplateWeaponHandheld(Model templateModel, @Nullable String name, Identifier itemId, ItemModelGenerator generator) {
        Identifier handheldModelName = (name == null ? getItemSubId(itemId, "_in_hand") : getItemSubId(itemId, "_" + name + "_in_hand"));
        Identifier handheldTexture = (name == null ? getItemId(itemId) : getItemSubId(itemId, "_" + name));

        templateModel.upload(handheldModelName, TextureMap.layer0(handheldTexture), generator.modelCollector); // this is the actual handheld model
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        registerTemplateWeaponInventory(templateModel, name, Registries.ITEM.getId(item), generator);
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Identifier itemId, ItemModelGenerator generator) {
        Identifier inventoryTexture = (name == null ? getItemSubId(itemId, "_inventory") : getItemSubId(itemId, "_" + name + "_inventory"));
        registerTemplateWeaponInventory(templateModel, name, itemId, inventoryTexture, generator);
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Identifier itemModelId, Identifier inventoryTexture, ItemModelGenerator generator) {
        Identifier inventoryModelName = (name == null ? getItemSubId(itemModelId, "_inventory") : getItemSubId(itemModelId, "_" + name + "_inventory"));

        Models.HANDHELD.upload(inventoryModelName, TextureMap.layer0(inventoryTexture), generator.modelCollector); // this is actually the inventory model
    }

    public static Identifier getItemId(Identifier itemId) {
        return itemId.withPrefixedPath("item/");
    }

    public static Identifier getItemSubId(Identifier itemId, String suffix) {
        return itemId.withPath(path -> "item/" + path + suffix);
    }
}
