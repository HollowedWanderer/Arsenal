package doctor4t.arsenal.common;

import doctor4t.arsenal.common.init.ModEntities;
import doctor4t.arsenal.common.init.ModItems;
import doctor4t.arsenal.common.init.ModParticles;
import doctor4t.arsenal.common.init.ModSoundEvents;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class Arsenal implements ModInitializer {
	public static final String MOD_ID = "arsenal";

	@Override
	public void onInitialize(ModContainer mod) {
		ModEntities.initialize();
		ModItems.initialize();
		ModSoundEvents.initialize();
		ModParticles.initialize();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
