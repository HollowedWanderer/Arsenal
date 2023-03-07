package doctor4t.anchorblade.common.init;

import doctor4t.anchorblade.Anchorblade;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModItems {
	Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

//	Item MOD_ITEM = createItem("mod_item", new ModItem(new QuiltItemSettings()));

	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(item, new Identifier(Anchorblade.MOD_ID, name));
		return item;
	}

	static void initialize() {
		ITEMS.keySet().forEach(item -> {
			Registry.register(Registries.ITEM, ITEMS.get(item), item);
			ModItemGroup.addToItemGroup(ModItemGroup.MOD_ITEMS, item);
		});
	}
}
