package dev.doctor4t.arsenal.entity;

import dev.doctor4t.arsenal.index.ArsenalEntities;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.index.ArsenalTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WeaponRackEntity extends ItemFrameEntity {

    public WeaponRackEntity(EntityType<? extends WeaponRackEntity> entityType, World world) {
        super(entityType, world);
    }

    public WeaponRackEntity(World world, BlockPos pos, Direction facing) {
        this(ArsenalEntities.WEAPON_RACK, world, pos, facing);
    }

    public WeaponRackEntity(EntityType<? extends WeaponRackEntity> type, World world, BlockPos pos, Direction facing) {
        super(type, world, pos, facing);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.isSneaking() && !this.getHeldItemStack().isEmpty()) {
            this.setInvisible(!this.isInvisible());
            return ActionResult.SUCCESS;
        }

        ItemStack stackInHand = player.getStackInHand(hand);
        if (!this.getHeldItemStack().isEmpty() || (this.getHeldItemStack().isEmpty() && stackInHand.isIn(ArsenalTags.DISPLAYABLE))) {
            return super.interact(player, hand);
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean isInvisible() {
        return super.isInvisible() && !this.getHeldItemStack().isEmpty();
    }

    @Override
    public boolean canStayAttached() {
        return this.getWorld().getOtherEntities(this, this.getBoundingBox(), PREDICATE).isEmpty();
    }

    protected ItemStack getAsItemStack() {
        return new ItemStack(ArsenalItems.WEAPON_RACK);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return damageSource.getSource() instanceof PlayerEntity player && this.getHeldItemStack().isEmpty() && !player.isSneaking();
    }
}
