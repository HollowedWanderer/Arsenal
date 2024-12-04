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
//        this.offRatMaidPlush(exporter);
    }

    public void offRatMaidPlush(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ArsenalItems.SCYTHE)
                .input('G', Items.GRAY_WOOL)
                .input('B', Items.BLACK_WOOL)
                .input('P', Items.PINK_WOOL)
                .input('p', Items.PURPLE_WOOL)
                .input('R', Items.RED_WOOL)
                .pattern("PpP")
                .pattern("pGp")
                .pattern("BRB")
                .criterion("has_white_wool", conditionsFromItem(Items.WHITE_WOOL))
                .criterion("has_black_wool", conditionsFromItem(Items.BLACK_WOOL))
                .criterion("has_pink_wool", conditionsFromItem(Items.PINK_WOOL))
                .criterion("has_gray_wool", conditionsFromItem(Items.GRAY_WOOL))
                .criterion("has_red_wool", conditionsFromItem(Items.RED_WOOL))
                .offerTo(exporter);
    }
}