package doctor4t.anchorblade.common.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import doctor4t.anchorblade.common.init.ModItems;
import doctor4t.anchorblade.common.init.ModParticles;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.quiltmappings.constants.MiningLevels;
import xyz.amymialee.mialeemisc.util.MialeeText;

import java.util.List;
import java.util.Optional;

public class AnchorbladeItem extends PickaxeItem {
	public static final DefaultParticleType[] LUX_ANCHORBLADE_SWEEP_PARTICLES = {ModParticles.LUX_ANCHORBLADE_SWEEP_1, ModParticles.LUX_ANCHORBLADE_SWEEP_2, ModParticles.LUX_ANCHORBLADE_SWEEP_3};

	public AnchorbladeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return TypedActionResult.success(user.getStackInHand(hand));
	}

	@Override
	public Text getName(ItemStack stack) {
		return MialeeText.withColor(super.getName(stack), 0xFF8700);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
	}

	public static void spawnSweepParticles(PlayerEntity player) {
		double deltaX = -MathHelper.sin((float) (player.getYaw() * (Math.PI / 180F)));
		double deltaZ = MathHelper.cos((float) (player.getYaw() * (Math.PI / 180F)));
		if (player.world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(LUX_ANCHORBLADE_SWEEP_PARTICLES[player.getRandom().nextInt(AnchorbladeItem.LUX_ANCHORBLADE_SWEEP_PARTICLES.length)], player.getX() + deltaX, player.getBodyY(0.5D), player.getZ() + deltaZ, 0, deltaX, 0.0D, deltaZ, 0.0D);
		}
	}

	public static ItemStack getWornAnchor(LivingEntity livingEntity) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
		if (component.isPresent()) {
			for (Pair<SlotReference, ItemStack> pair : component.get().getAllEquipped()) {
				if (pair.getRight().isOf(ModItems.ANCHORBLADE)) {
					return pair.getRight();
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static class AnchorBladeToolMaterial implements ToolMaterial {
		public static final AnchorBladeToolMaterial INSTANCE = new AnchorBladeToolMaterial();

		@Override
		public int getDurability() {
			return 2560;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return 9.0F;
		}

		@Override
		public float getAttackDamage() {
			return 4.0F;
		}

		@Override
		public int getMiningLevel() {
			return MiningLevels.NETHERITE;
		}

		@Override
		public int getEnchantability() {
			return 28;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(Items.COPPER_BLOCK);
		}
	}
}
