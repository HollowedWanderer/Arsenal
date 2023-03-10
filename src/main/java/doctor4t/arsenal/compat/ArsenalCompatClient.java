package doctor4t.arsenal.compat;

import org.quiltmc.loader.api.QuiltLoader;

public class ArsenalCompatClient {
	public static void init() {
		if (QuiltLoader.isModLoaded("amarite")) {
			AmariteCompatClient.init();
		}
	}
}
