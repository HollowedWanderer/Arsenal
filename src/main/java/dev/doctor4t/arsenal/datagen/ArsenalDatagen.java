package dev.doctor4t.arsenal.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ArsenalDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(ArsenalModelGen::new);
        pack.addProvider(ArsenalLangGen::new);
        pack.addProvider(ArsenalRecipeGen::new);
        pack.addProvider(ArsenalTagGen.DefileDamageTagGen::new);
    }
}
