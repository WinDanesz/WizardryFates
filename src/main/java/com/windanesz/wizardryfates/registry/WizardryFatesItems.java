package com.windanesz.wizardryfates.registry;

import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.item.ItemDisciplineBook;
import com.windanesz.wizardryfates.item.ItemDisciplineScroll;
import com.windanesz.wizardryfates.item.ItemFatesOblivionScroll;
import com.windanesz.wizardryfates.item.ItemSubDisciplineScroll;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(WizardryFates.MODID)
@Mod.EventBusSubscriber
public class WizardryFatesItems {

	private WizardryFatesItems() {} // no instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static final Item book_of_fates = placeholder();

	public static final Item scroll_earth = placeholder();
	public static final Item scroll_necromancy = placeholder();
	public static final Item scroll_healing = placeholder();
	public static final Item scroll_lightning = placeholder();
	public static final Item scroll_ice = placeholder();
	public static final Item scroll_fire = placeholder();
	public static final Item scroll_sorcery = placeholder();
	public static final Item scroll_ancient = placeholder();

	public static final Item lesser_scroll_earth = placeholder();
	public static final Item lesser_scroll_necromancy = placeholder();
	public static final Item lesser_scroll_healing = placeholder();
	public static final Item lesser_scroll_lightning = placeholder();
	public static final Item lesser_scroll_ice = placeholder();
	public static final Item lesser_scroll_fire = placeholder();
	public static final Item lesser_scroll_sorcery = placeholder();
	public static final Item lesser_scroll_ancient = placeholder();

	public static final Item scroll_oblivion = placeholder();

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item) {
		registerItem(registry, name, item, false);
	}

	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(WizardryFates.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		Item itemblock = new ItemBlock(block).setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();

		registerItem(registry, "book_of_fates", new ItemDisciplineBook());

		registerItem(registry, "scroll_earth", new ItemDisciplineScroll(Element.EARTH));
		registerItem(registry, "scroll_necromancy", new ItemDisciplineScroll(Element.NECROMANCY));
		registerItem(registry, "scroll_healing", new ItemDisciplineScroll(Element.HEALING));
		registerItem(registry, "scroll_lightning", new ItemDisciplineScroll(Element.LIGHTNING));
		registerItem(registry, "scroll_ice", new ItemDisciplineScroll(Element.ICE));
		registerItem(registry, "scroll_fire", new ItemDisciplineScroll(Element.FIRE));
		registerItem(registry, "scroll_sorcery", new ItemDisciplineScroll(Element.SORCERY));
		registerItem(registry, "scroll_ancient", new ItemDisciplineScroll(Element.MAGIC));


		registerItem(registry, "lesser_scroll_earth", new ItemSubDisciplineScroll(Element.EARTH));
		registerItem(registry, "lesser_scroll_necromancy", new ItemSubDisciplineScroll(Element.NECROMANCY));
		registerItem(registry, "lesser_scroll_healing", new ItemSubDisciplineScroll(Element.HEALING));
		registerItem(registry, "lesser_scroll_lightning", new ItemSubDisciplineScroll(Element.LIGHTNING));
		registerItem(registry, "lesser_scroll_ice", new ItemSubDisciplineScroll(Element.ICE));
		registerItem(registry, "lesser_scroll_fire", new ItemSubDisciplineScroll(Element.FIRE));
		registerItem(registry, "lesser_scroll_sorcery", new ItemSubDisciplineScroll(Element.SORCERY));
		registerItem(registry, "lesser_scroll_ancient", new ItemSubDisciplineScroll(Element.MAGIC));

		registerItem(registry, "scroll_oblivion", new ItemFatesOblivionScroll());

		// AS support
	}

}
