package dev.doctor4t.arsenal.datagen;

import dev.doctor4t.arsenal.index.ArsenalItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class ArsenalRecipeGen extends FabricRecipeProvider {
    public ArsenalRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        this.offerWeaponRack(exporter);
    }

    public void offerWeaponRack(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ArsenalItems.WEAPON_RACK)
                .input('S', Items.STICK)
                .input('I', Items.IRON_INGOT)
                .input('L', Items.LEATHER)
                .pattern("SSS")
                .pattern("ILI")
                .pattern("SSS")
                .criterion("has_stick", conditionsFromItem(Items.STICK))
                .criterion("has_iron_ingot", conditionsFromItem(Items.IRON_INGOT))
                .criterion("has_leather", conditionsFromItem(Items.LEATHER))
                .offerTo(exporter);
    }
}