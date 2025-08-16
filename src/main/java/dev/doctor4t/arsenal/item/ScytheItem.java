package dev.doctor4t.arsenal.item;

import dev.doctor4t.arsenal.entity.BloodScytheEntity;
import dev.doctor4t.arsenal.index.*;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import dev.doctor4t.arsenal.particle.SweepParticleEffect;
import dev.doctor4t.arsenal.util.EnchantmentListener;
import dev.doctor4t.ratatouille.item.CustomHitParticleItem;
import dev.doctor4t.ratatouille.item.CustomHitSoundItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ScytheItem extends Item implements CustomHitParticleItem, CustomHitSoundItem, ArsenalWeaponItem {

    public ScytheItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState blockStateClicked = context.getWorld().getBlockState(context.getBlockPos());
        PlayerEntity user = context.getPlayer();
        if (user != null && user.isSneaking() && (blockStateClicked.isIn(BlockTags.ANVIL) || blockStateClicked.isOf(Blocks.SMITHING_TABLE))) {
            if (ArsenalCosmetics.isSupporter(user.getUuid())) {
                ScytheSkin currentSkin = ScytheSkin.fromString(ArsenalCosmetics.getSkin(context.getStack()));

                if (currentSkin == null) {
                    currentSkin = ScytheSkin.DEFAULT;
                }
                if (context.getWorld().isClient) {
                    ArsenalCosmetics.setSkin(user.getUuid(), context.getStack(), ScytheSkin.getNext(currentSkin).asString());
                    context.getPlayer().playSound(SoundEvents.BLOCK_SMITHING_TABLE_USE, 0.5f, 1.0f);
                } else {
                    context.getStack().set(ArsenalDataComponents.SCYTHE_SKIN, currentSkin);
                }

                return ActionResult.SUCCESS;
            } else {
                if (context.getWorld().isClient) {
                    user.sendMessage(Text.translatable("tooltip.supporter_only").styled(style -> style.withColor(0xCC0000)), false);
                    context.getPlayer().playSound(SoundEvents.ITEM_SHIELD_BREAK.value(), 0.5f, 1.0f);
                }
                return ActionResult.FAIL;
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (EnchantmentListener.hasEnchantment(player.getStackInHand(hand), "arsenal:spewing")) {
            float f = 1.0f;

            if (world instanceof ServerWorld serverWorld) {
                BloodScytheEntity bloodScythe = new BloodScytheEntity(ArsenalEntities.BLOOD_SCYTHE, world);
                bloodScythe.setOwner(player);
                bloodScythe.setPosition(player.getX(), player.getEyeY() - 0.10000000149011612, player.getZ());
                bloodScythe.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, f * 3.0f, 1.0f);
                player.getStackInHand(hand).damage(1, player);
                bloodScythe.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                ArrayList<StatusEffectInstance> statusEffectsHalved = new ArrayList<>();
                float absorption = player.getAbsorptionAmount();
                for (StatusEffectInstance statusEffect : player.getStatusEffects()) {
                    StatusEffectInstance statusHalved = new StatusEffectInstance(statusEffect.getEffectType(), statusEffect.getDuration() / 2, statusEffect.getAmplifier(), statusEffect.isAmbient(), statusEffect.shouldShowParticles(), statusEffect.shouldShowIcon());
                    bloodScythe.addEffect(statusHalved);
                    statusEffectsHalved.add(statusHalved);
                }
                player.clearStatusEffects();
                for (StatusEffectInstance statusEffectInstance : statusEffectsHalved) {
                    player.addStatusEffect(statusEffectInstance);
                }
                player.setAbsorptionAmount(absorption);

                player.damage(serverWorld, world.getDamageSources().create(ArsenalDamageTypes.SPEWING), 3f);
                player.getItemCooldownManager().set(this.getDefaultStack(), 20);

                world.spawnEntity(bloodScythe);
                ScytheSkin skin = player.getMainHandStack().getOrDefault(ArsenalDataComponents.SCYTHE_SKIN, ScytheSkin.DEFAULT);

                serverWorld.spawnParticles(
                        new SweepParticleEffect(skin.color, skin.shadowColor),
                        player.getX() - MathHelper.sin(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                        player.getBodyY(0.5D),
                        player.getZ() + MathHelper.cos(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                        1, 0, 0, 0, 0
                );
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(), ArsenalSounds.ITEM_SCYTHE_SPEWING, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }
        return super.use(world, player, hand);
    }

    @Override
    public void spawnHitParticles(PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld serverWorld) {

            ScytheSkin skin = player.getMainHandStack().getOrDefault(ArsenalDataComponents.SCYTHE_SKIN, ScytheSkin.DEFAULT);

            serverWorld.spawnParticles(
                    new SweepParticleEffect(skin.color, skin.shadowColor),
                    player.getX() - MathHelper.sin(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                    player.getBodyY(0.5D),
                    player.getZ() + MathHelper.cos(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                    1, 0, 0, 0, 0
            );
        }
    }

    @Override
    public void playHitSound(PlayerEntity player) {
        player.playSound(ArsenalSounds.ITEM_SCYTHE_HIT, 1.0F, (float) (1.0F + player.getRandom().nextGaussian() / 10f));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (entity instanceof PlayerEntity player) {
            stack.set(ArsenalDataComponents.SKIN_OWNER, player.getUuid().toString());
            ScytheSkin currentSkin = ScytheSkin.fromString(ArsenalCosmetics.getSkin(stack));

            if (currentSkin == null || !ArsenalCosmetics.isSupporter(player.getUuid())) {
                currentSkin = ScytheSkin.DEFAULT;
            }

            if (!stack.getOrDefault(ArsenalDataComponents.SCYTHE_SKIN, ScytheSkin.DEFAULT).equals(currentSkin)) {
                stack.set(ArsenalDataComponents.SCYTHE_SKIN, currentSkin);
            }
        }
    }
}
