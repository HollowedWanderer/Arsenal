package doctor4t.arsenal.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public interface ModDamageSources {
	public static DamageSource anchor(Entity anchor, @Nullable Entity attacker) {
		return new ProjectileDamageSource("anchor", anchor, attacker).setProjectile();
	}

}
