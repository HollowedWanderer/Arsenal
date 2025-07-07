package dev.doctor4t.arsenal.datagen;

import dev.doctor4t.arsenal.index.ArsenalDamageTypes;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.index.ArsenalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class ArsenalTagGen {
    public static class ArsenalDamageTagGen extends FabricTagProvider<DamageType> {
        public ArsenalDamageTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.builder(DamageTypeTags.IS_PROJECTILE)
                    .addOptional(ArsenalDamageTypes.ANCHOR)
                    .addOptional(ArsenalDamageTypes.BLOOD_SCYTHE);

            this.builder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                    .addOptional(ArsenalDamageTypes.SPEWING);

            this.builder(DamageTypeTags.BYPASSES_ARMOR)
                    .addOptional(ArsenalDamageTypes.SPEWING);

            this.builder(DamageTypeTags.NO_KNOCKBACK)
                    .addOptional(ArsenalDamageTypes.SPEWING);

        }
    }

    public static class ArsenalItemTagGen extends FabricTagProvider.ItemTagProvider {
        public ArsenalItemTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) { // TODO: Replace some of these with conventional tags
            this.builder(ArsenalTags.DISPLAYABLE)
                    .addTag(ArsenalTags.BIG_WEAPONS)
                    .addTag(ArsenalTags.SHIELDS)
                    .addTag(ArsenalTags.RANGED_WEAPONS)
                    .addTag(ArsenalTags.TRIDENTS)
            ;

            this.builder(ArsenalTags.BIG_WEAPONS)
                    .add(ArsenalItems.SCYTHE.getDefaultStack().getRegistryEntry().getKey().stream())
                    .add(ArsenalItems.ANCHORBLADE.getDefaultStack().getRegistryEntry().getKey().stream())
                    .addTag(ArsenalTags.TRIDENTS)
            ;

            this.builder(ArsenalTags.TRIDENTS)
                    .add(Items.TRIDENT.getDefaultStack().getRegistryEntry().getKey().stream())
            ;

            this.builder(ArsenalTags.SHIELDS)
                    .add(Items.SHIELD.getDefaultStack().getRegistryEntry().getKey().stream())
            ;

            this.builder(ArsenalTags.RANGED_WEAPONS)
                    .add(Items.BOW.getDefaultStack().getRegistryEntry().getKey().stream())
                    .add(Items.CROSSBOW.getDefaultStack().getRegistryEntry().getKey().stream())
            ;
        }
    }
}
