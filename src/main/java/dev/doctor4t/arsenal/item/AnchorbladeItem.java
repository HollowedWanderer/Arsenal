package dev.doctor4t.arsenal.item;

import dev.doctor4t.arsenal.entity.AnchorbladeEntity;
import dev.doctor4t.arsenal.index.*;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import dev.doctor4t.arsenal.particle.SweepParticleEffect;
import dev.doctor4t.arsenal.util.AnchorOwner;
import dev.doctor4t.arsenal.util.EnchantmentListener;
import dev.doctor4t.ratatouille.item.CustomHitParticleItem;
import dev.doctor4t.ratatouille.item.CustomHitSoundItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AnchorbladeItem extends Item implements CustomHitParticleItem, CustomHitSoundItem, ArsenalWeaponItem {

    public AnchorbladeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState blockStateClicked = context.getWorld().getBlockState(context.getBlockPos());
        PlayerEntity user = context.getPlayer();
        if (user != null && user.isSneaking() && (blockStateClicked.isIn(BlockTags.ANVIL) || blockStateClicked.isOf(Blocks.SMITHING_TABLE))) {
            if (ArsenalCosmetics.isSupporter(user.getUuid())) {
                context.getStack().set(ArsenalDataComponents.SKIN_OWNER, user.getUuid().toString());
                AnchorbladeSkin currentSkin = AnchorbladeSkin.fromString(ArsenalCosmetics.getSkin(context.getStack()));

                if (currentSkin == null) {
                    currentSkin = AnchorbladeSkin.DEFAULT;
                }
                if (context.getWorld().isClient) {
                    ArsenalCosmetics.setSkin(user.getUuid(), context.getStack(), AnchorbladeSkin.getNext(currentSkin).asString());
                    context.getPlayer().playSound(SoundEvents.BLOCK_SMITHING_TABLE_USE, 0.5f, 1.0f);
                } else {
                    context.getStack().set(ArsenalDataComponents.ANCHORBLADE_SKIN, currentSkin);
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
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user instanceof AnchorOwner owner) {
            boolean reeling = EnchantmentListener.hasEnchantment(stack, "arsenal:reeling");
            if (owner.arsenal$isAnchorActive(hand, reeling)) {
                if (!(user.getOffHandStack().isOf(ArsenalItems.ANCHORBLADE) && !owner.arsenal$isAnchorActive(Hand.OFF_HAND, reeling))) {
                    owner.arsenal$getAnchor(hand, reeling).setRecalled(true);
                }
                return ActionResult.FAIL;
            }
            if (!world.isClient) {
                stack.damage(1, user);
                AnchorbladeEntity anchorbladeEntity = new AnchorbladeEntity(ArsenalEntities.ANCHORBLADE, world);
                anchorbladeEntity.setRecalled(false);
                anchorbladeEntity.setReeling(reeling);
                anchorbladeEntity.setItem(stack);
                anchorbladeEntity.setOwner(user);
                anchorbladeEntity.setPosition(user.getX(), user.getEyeY() - 0.10000000149011612, user.getZ());

                float f = -MathHelper.sin(user.getYaw() * 0.017453292F) * MathHelper.cos(user.getPitch() * 0.017453292F);
                float g = -MathHelper.sin(user.getPitch() * 0.017453292F);
                float h = MathHelper.cos(user.getYaw() * 0.017453292F) * MathHelper.cos(user.getPitch() * 0.017453292F);
                Vec3d throwDir = new Vec3d(f, g, h).normalize();
                Vec3d playerVel = user.getVelocity().normalize();
                double dot = throwDir.dotProduct(playerVel); // Value between -1 and 1
                float strength = 3.0F;
                if (dot > 0.1) {
                    strength += (float) (dot * 2.0);
                }
                anchorbladeEntity.setVelocity(f, g, h, strength, 1.0F);
                owner.arsenal$setAnchor(hand, anchorbladeEntity);
                world.spawnEntity(anchorbladeEntity);
                world.playSoundFromEntity(null, anchorbladeEntity, ArsenalSounds.ITEM_ANCHORBLADE_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);

                return ActionResult.SUCCESS;
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void spawnHitParticles(PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            AnchorbladeSkin skin = player.getMainHandStack().getOrDefault(ArsenalDataComponents.ANCHORBLADE_SKIN, AnchorbladeSkin.DEFAULT);

            Pair<Integer, Integer> colorPair = skin.getRandomParticleColorPair(player.getRandom());
            serverWorld.spawnParticles(
                    new SweepParticleEffect(colorPair.getLeft(), colorPair.getRight()),
                    player.getX() - MathHelper.sin(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                    player.getBodyY(0.5D),
                    player.getZ() + MathHelper.cos(player.getYaw() * MathHelper.RADIANS_PER_DEGREE),
                    1, 0, 0, 0, 0
            );
        }
    }

    @Override
    public void playHitSound(PlayerEntity player) {
        player.playSound(ArsenalSounds.ITEM_ANCHORBLADE_HIT, 1.0F, (float) (1.0F + player.getRandom().nextGaussian() / 10f));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (entity instanceof PlayerEntity player) {
            stack.set(ArsenalDataComponents.SKIN_OWNER, player.getUuid().toString());
            AnchorbladeSkin currentSkin = AnchorbladeSkin.fromString(ArsenalCosmetics.getSkin(stack));

            if (currentSkin == null || !ArsenalCosmetics.isSupporter(player.getUuid())) {
                currentSkin = AnchorbladeSkin.DEFAULT;
            }

            if (!stack.getOrDefault(ArsenalDataComponents.ANCHORBLADE_SKIN, AnchorbladeSkin.DEFAULT).equals(currentSkin)) {
                stack.set(ArsenalDataComponents.ANCHORBLADE_SKIN, currentSkin);
            }
        }
    }
}
