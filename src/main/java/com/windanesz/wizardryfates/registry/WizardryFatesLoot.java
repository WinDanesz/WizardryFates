package com.windanesz.wizardryfates.registry;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.integration.FatesASIntegration;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.minecraft.world.storage.loot.LootTableList.CHESTS_SPAWN_BONUS_CHEST;

@Mod.EventBusSubscriber
public class WizardryFatesLoot {

	// TODO: hook into net.minecraftforge.event.LootTableLoadEvent to add items to starter chest

	private static List<Item> scrolls = new ArrayList<>();

	static {
		scrolls.add(WizardryFatesItems.scroll_earth);
		scrolls.add(WizardryFatesItems.scroll_necromancy);
		scrolls.add(WizardryFatesItems.scroll_healing);
		scrolls.add(WizardryFatesItems.scroll_lightning);
		scrolls.add(WizardryFatesItems.scroll_ice);
		scrolls.add(WizardryFatesItems.scroll_fire);
		scrolls.add(WizardryFatesItems.scroll_sorcery);
		if (FatesASIntegration.enabled()) {
			scrolls.add(WizardryFatesItems.scroll_ancient);
		}
	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		if (Settings.settings.bonus_chest_scroll && event.getName() == CHESTS_SPAWN_BONUS_CHEST) {
			Random rand = new Random();
			Item scroll = scrolls.get(rand.nextInt(scrolls.size()));
			LootEntryItem[] lootEntryItems = {new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "discipline_scroll")};
			LootCondition[] lootConditions = {};
			RandomValueRange range = new RandomValueRange(1, 1);
			RandomValueRange bonus = new RandomValueRange(0, 0);

			LootPool pool = new LootPool(lootEntryItems, lootConditions, range, bonus, "discipline_scroll");
			event.getTable().addPool(pool);
		}
		// Scroll loot
		if (Settings.settings.discipline_scroll_looting && Arrays.asList(WizardryFates.settings.scrollInjectionLocations).contains(event.getName())) {
			List<LootEntryItem> lootEntryItemList = new ArrayList<>();

			int scrollWeight = (int) Math.round((Settings.settings.discipline_scroll_loot_chance * 1000) / scrolls.size());

			int emptyWeight = 1000 - (scrollWeight * scrolls.size());

			for (Item scroll : scrolls) {
				LootEntryItem lootEntryItem = new LootEntryItem(scroll, scrollWeight, 0, new LootFunction[0], new LootCondition[0], scroll.getRegistryName().getPath());
				lootEntryItemList.add(lootEntryItem);
			}

			LootEntryItem EmptyItem = new LootEntryItem(Items.AIR, emptyWeight, 0, new LootFunction[0], new LootCondition[0], "empty");
			lootEntryItemList.add(EmptyItem);

			LootEntryItem[] lootEntryItems = lootEntryItemList.stream().toArray(LootEntryItem[]::new);


			LootCondition[] lootConditions = {};
			RandomValueRange range = new RandomValueRange(1, 1);
			RandomValueRange bonus = new RandomValueRange(0, 0);

			LootPool pool = new LootPool(lootEntryItems, lootConditions, range, bonus, "fates_scrolls");
			event.getTable().addPool(pool);
		}
	}

	/**
	 * Injects every element of sourcePool into targetPool
	 */
	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		/** Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries} */
		if (sourcePool != null && targetPool != null) {
			List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

			for (LootEntry entry : lootEntries) {
				targetPool.addEntry(entry);
			}
		} else {
			WizardryFates.logger.warn("Attempted to inject to null pool source or target.");
		}

	}
}
