package dev.doctor4t.arsenal.entity;

import dev.doctor4t.arsenal.index.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AnchorbladeEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> ANCHOR_FLAGS = DataTracker.registerData(AnchorbladeEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(AnchorbladeEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public int returnTimer;

    public AnchorbladeEntity(EntityType<? extends AnchorbladeEntity> entityType, World world) {
        super(entityType, world);
        this.setItem(ArsenalItems.ANCHORBLADE.getDefaultStack());
        this.setNoGravity(true);
    }

    public void setItem(ItemStack stack) {
        this.getDataTracker().set(ITEM, stack.copyWithCount(1));
    }

    public ItemStack getStack() {
        return this.getDataTracker().get(ITEM);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANCHOR_FLAGS, (byte) 0);
        builder.add(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        view.put("stack", ItemStack.CODEC, this.getDataTracker().get(ITEM));
        view.putByte("flags", this.getDataTracker().get(ANCHOR_FLAGS));
    }

    @Override
    public void readData(ReadView view) {
        super.readData(view);
        this.dataTracker.set(ITEM, view.read("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.dataTracker.set(ANCHOR_FLAGS, view.getByte("flags", (byte) 0));
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();
        double d = 2;

        if (!this.getWorld().isClient) {
            if (owner == null || !owner.isAlive()) {
                this.discard();
                return;
            }
            if (this.hasDealtDamage() || this.isNoClip()) {
                this.setNoClip(true);
                Vec3d vec3d = owner.getEyePos().subtract(this.getPos());
                if (this.getWorld().isClient) {
                    this.lastRenderY = this.getY();
                }

                double length = vec3d.length();
                this.setVelocity(vec3d.normalize().multiply(Math.min(length, d * 3)));
                if (owner.getVelocity().length() > this.getVelocity().length() - 2) {
                    this.setPosition(owner.getPos());
                }
            }
            if (this.getPos().distanceTo(owner.getPos()) > 30) {
                this.setDealtDamage(true);
            }
        }

        if (this.isInGround() && !this.hasDealtDamage()) {
            if (this.hasReeling()) {
                if (this.returnTimer++ > 100 || (this.getDataTracker().get(ANCHOR_FLAGS) & (1 << 2)) != 0) {
                    this.setDealtDamage(true);
                }
                if (owner == null) {
                    this.setDealtDamage(true);
                    return;
                }
                float e = (float) (d / 5f);
                Vec3d vec3d = this.getPos().subtract(owner.getEyePos());
                owner.setVelocity(owner.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(e)));
                owner.velocityModified = true;
                owner.fallDistance = 0;
            } else {
                float radius = 5f;
                // impact
                this.getWorld().addParticleClient(ArsenalParticles.SHOCKWAVE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                for (LivingEntity hitLivingEntity : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(radius), LivingEntity::isAlive)) {
                    float strength = this.getKnockbackForEntity(hitLivingEntity);
                    if (!(strength <= 0.0)) {
                        this.velocityDirty = true;
                        Vec3d distance = hitLivingEntity.getPos().add(0, hitLivingEntity.getHeight() / 2f, 0).subtract(this.getPos());
                        Vec3d footDistance = hitLivingEntity.getPos().subtract(this.getPos());
                        if (footDistance.y > distance.y) {
                            distance = footDistance;
                        }
                        float proximity = (float) MathHelper.lerp(MathHelper.clamp(distance.length() / radius, 0, 1), 1, 0);
                        Vec3d direction = distance.normalize().multiply(proximity * strength);
                        hitLivingEntity.addVelocity(direction.x, direction.y, direction.z);
                        hitLivingEntity.fallDistance = 0;
                    }
                }
                this.setDealtDamage(true);
            }
        }

        super.tick();
    }

    @Override
    public void setPitch(float pitch) {
        if (!this.hasDealtDamage()) {
            super.setPitch(pitch);
        }
    }

    @Override
    public void setYaw(float yaw) {
        if (!this.hasDealtDamage()) {
            super.setYaw(yaw);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity hitEntity = entityHitResult.getEntity();
        float damage = 10F;
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            damage = EnchantmentHelper.getDamage(serverWorld, this.getStack(), hitEntity, this.getWorld().getDamageSources().create(ArsenalDamageTypes.ANCHOR, this, this.getOwner()), damage);
        }
        Entity owner = this.getOwner();
        this.setDealtDamage(true);
        SoundEvent soundEvent = this.getHitSound();
        hitEntity.timeUntilRegen = 0;
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            if (hitEntity.damage(serverWorld, this.getWorld().getDamageSources().create(ArsenalDamageTypes.ANCHOR, this, this.getOwner()), damage)) {
                if (hitEntity.getType() == EntityType.ENDERMAN) {
                    return;
                }

                if (hitEntity instanceof LivingEntity hitLivingEntity) {
                    if (owner instanceof LivingEntity) {
                        float strength = this.getKnockbackForEntity(hitLivingEntity);
                        if (!(strength <= 0.0)) {
                            this.velocityDirty = true;
                            Vec3d dir = hitLivingEntity.getPos().subtract(owner.getPos()).normalize().multiply(strength);
                            if (this.hasReeling()) {
                                dir = owner.getPos().subtract(hitLivingEntity.getPos()).multiply(strength / 10f);
                            }
                            hitLivingEntity.addVelocity(dir.x, dir.y, dir.z);
                        }
                    }
                    this.onHit(hitLivingEntity);
                }

                if (this.getOwner() instanceof PlayerEntity player && !player.isCreative()) {
                    player.getItemCooldownManager().set(ArsenalItems.ANCHORBLADE.getDefaultStack(), 40);
                }
            }
            if (hitEntity instanceof TntEntity) {
                if (owner instanceof LivingEntity) {
                    float strength = 2;
                    this.velocityDirty = true;
                    Vec3d dir = hitEntity.getPos().subtract(owner.getPos()).normalize().multiply(strength);
                    if (this.hasReeling()) {
                        dir = owner.getPos().subtract(hitEntity.getPos()).multiply(strength / 10f);
                    }
                    hitEntity.addVelocity(dir.x, dir.y, dir.z);
                }
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(soundEvent, 1.0f, 1.0f);
    }

    private float getKnockbackForEntity(LivingEntity hitLivingEntity) {
        return (float) (1f * (1.0 - hitLivingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)));
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isOwner(player);
    }

    @Override
    protected float getDragInWater() {
        return 0.99F;
    }

    @Override
    protected SoundEvent getHitSound() {
        return ArsenalSounds.ENTITY_ANCHORBLADE_LAND;
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    public boolean hasDealtDamage() {
        return this.getAnchorFlag(0);
    }

    public void setDealtDamage(boolean dealtDamage) {
        this.setAnchorFlag(0, dealtDamage);
    }

    public boolean hasReeling() {
        return this.getAnchorFlag(1);
    }

    public void setReeling(boolean reeling) {
        this.setAnchorFlag(1, reeling);
    }

    public void setRecalled(boolean recalled) {
        if (recalled) this.setDealtDamage(true);
        this.setAnchorFlag(2, recalled);
    }

    private boolean getAnchorFlag(int flag) {
        if (flag < 0 || flag > 8) {
            return false;
        }
        return (this.dataTracker.get(ANCHOR_FLAGS) >> flag & 0x01) == 1;
    }

    private void setAnchorFlag(int flag, boolean value) {
        if (flag < 0 || flag > 8) {
            return;
        }
        if (value) {
            this.dataTracker.set(ANCHOR_FLAGS, (byte) (this.dataTracker.get(ANCHOR_FLAGS) | 1 << flag));
        } else {
            this.dataTracker.set(ANCHOR_FLAGS, (byte) (this.dataTracker.get(ANCHOR_FLAGS) & ~(1 << flag)));
        }
    }
}
