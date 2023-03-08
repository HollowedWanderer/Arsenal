package doctor4t.anchorblade.common.init;

import doctor4t.anchorblade.common.Arsenal;
import doctor4t.anchorblade.common.item.AnchorbladeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import xyz.amymialee.mialeemisc.itemgroup.MialeeItemGroup;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModItems {
	Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
	MialeeItemGroup MOD_ITEMS = MialeeItemGroup.create(Arsenal.id("arsenal"));

	Item ANCHORBLADE = createItem("anchorblade", new AnchorbladeItem(AnchorbladeItem.AnchorBladeToolMaterial.INSTANCE, 5, -3.0f, new QuiltItemSettings().rarity(Rarity.EPIC)));

	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(item, Arsenal.id(name));
		return item;
	}

	static void initialize() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
		initItemGroups();
	}

	static void initItemGroups() {
		DefaultedList<ItemStack> stacks = DefaultedList.of();
		ITEMS.keySet().forEach(item -> stacks.add(new ItemStack(item)));
		MOD_ITEMS.appendStacks(stacks);
		MOD_ITEMS.setIcon(stacks.toArray(new ItemStack[0]));
	}
}
