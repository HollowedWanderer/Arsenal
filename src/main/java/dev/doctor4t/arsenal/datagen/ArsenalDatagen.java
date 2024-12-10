package dev.doctor4t.arsenal.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ArsenalDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(ArsenalModelGen::new);
        pack.addProvider(ArsenalLangGen::new);
        pack.addProvider(ArsenalTagGen.ArsenalItemTagGen::new);
        pack.addProvider(ArsenalTagGen.ArsenalDamageTagGen::new);
        pack.addProvider(ArsenalRecipeGen::new);
    }
}
