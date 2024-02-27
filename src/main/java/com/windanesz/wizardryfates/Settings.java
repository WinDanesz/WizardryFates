package com.windanesz.wizardryfates;

import com.windanesz.wizardryfates.handler.Utils;
import electroblob.wizardry.constants.Element;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static electroblob.wizardry.Settings.toResourceLocations;

@Config(modid = WizardryFates.MODID, name = "WizardryFates") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	@Config.Ignore
	public List<Element> elements = convertToElementList(settings.element_selection_list);

	@Config.Ignore
	public ResourceLocation[] scrollInjectionLocations = toResourceLocations(settings.scroll_loot_inject_locations);

	@Config.Ignore
	public static List<String> ALLOWED_TIERS = new ArrayList<>();

	static {
		ALLOWED_TIERS.add("NONE");
		ALLOWED_TIERS.add("NOVICE");
		ALLOWED_TIERS.add("APPRENTICE");
		ALLOWED_TIERS.add("ADVANCED");
		ALLOWED_TIERS.add("MASTER");

	}

	public static List<Element> convertToElementList(String[] list) {
		List<Element> elements = new ArrayList<>();
		for (String string : list) {
			elements.add(Utils.getElementFromName(string));
		}
		return elements;
	}

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = WizardryFates.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WizardryFates.MODID)) {
				ConfigManager.sync(WizardryFates.MODID, Config.Type.INSTANCE);
			}
		}
	}

	@Config.Name("Server Settings")
	@Config.LangKey("settings.wizardryfates:server_settings")
	@Config.Comment("Settings which only take effect if they are applied on the server side (same as client for singleplayer)")
	public static GeneralSettings settings = new GeneralSettings();

	public static class GeneralSettings {

		@Config.Name("Scroll Tier Cap of Non-Discipline Elements")
		@Config.Comment("Controls how players can use scrolls which are not their discipline. If this is set to 0, players won't be able to use scrolls which doesn't belong to their discipline. Values:"
				+ "\n0: None"
				+ "\n1: Novice"
				+ "\n2: Apprentice"
				+ "\n3: Advanced"
				+ "\n4: Master")
		@Config.SlidingOption
		@Config.RangeInt(min = 0, max = 4)
		public int scroll_tier_limit = 4;

		@Config.Name("Scroll Tier Cap for Non-Wizard Players")
		@Config.Comment("Determines how players who are set to be 'MAGICLESS' can use spell scrolls. Values:"
				+ "\n0: None"
				+ "\n1: Novice"
				+ "\n2: Apprentice"
				+ "\n3: Advanced"
				+ "\n4: Master")
		@Config.SlidingOption
		@Config.RangeInt(min = 0, max = 4)
		public int scroll_tier_limit_for_magicless_players = 0;

		@Config.Name("Allow 'Other' Spell Sources For Non-Wizard Players")
		@Config.Comment("Determines if non wizard players can use items which can cast spells as 'Other' sources, (usually artefacts which can cast spells).")
		public boolean allow_other_sources_for_non_wizards = true;

		@Config.Name("Allow 'Other' Spell Sources")
		@Config.Comment("If true, it's possible to cast spells of any discipline via items which are technically not registered as Wand or Scroll, but as 'Other'. (Ancient Spellcraft artefacts with active spell abilities are registered this way for example)."
				+ "If false, players can only use these items if the underlying spell belongs to their element.")
		public boolean allow_other_sources = true;

		@Config.Name("Global Discipline Mode")
		@Config.Comment("DEPRECATED - no longer in use")
		@Config.SlidingOption
		@Config.RangeInt(min = 1, max = 3)
		public int global_discipline_mode = 1;

		@Config.Name("Max Main Discipline Count")
		@Config.Comment("Specifies the maximum number of main disciplines a player can have.")
		@Config.SlidingOption
		@Config.RangeInt(min = 1, max = 8)
		public int max_main_discipline_count = 8;

		@Config.Name("Whitelist For Main Disciplines Assigned To New Players Joining World")
		@Config.Comment("List of main disciplines that are assigned to new players when they join the world for the first time.")
		public String[] whitelist_for_main_disciplines_assigned_to_new_players_joining_world = {
		};

		@Config.Name("Whitelist For Sub Disciplines Assigned To New Players Joining World")
		@Config.Comment("List of sub-disciplines that are assigned to new players when they join the world for the first time.")
		public String[] whitelist_for_sub_disciplines_assigned_to_new_players_joining_world = {
		};

		@Config.Name("Max Sub Discipline Count")
		@Config.Comment("Specifies the maximum number of sub-disciplines a player can have.")
		@Config.SlidingOption
		@Config.RangeInt(min = 1, max = 8)
		public int max_sub_discipline_count = 8;

		@Config.Name("Sub-Discipline Tier Cap")
		@Config.Comment("Specifies the tier limit of Sub-Disciplines. Players can't cast spells which are part of their Sub-Discipline and are above this tier. Values:"
				+ "\n1: Novice"
				+ "\n2: Apprentice"
				+ "\n3: Advanced"
				+ "\n4: Master")
		@Config.SlidingOption
		@Config.RangeInt(min = 1, max = 4)
		public int sub_discipline_spellcasting_tier_limit = 1;

		@Config.Name("Any Non-Discipline Spellcasting Tier Cap")
		@Config.Comment("With this setting, you can allow players to cast spells of any other elements up to and including the tier described in this setting. Values:"
				+ "\n0: None"
				+ "\n1: Novice"
				+ "\n2: Apprentice"
				+ "\n3: Advanced"
				+ "\n4: Master")
		@Config.SlidingOption
		@Config.RangeInt(min = 0, max = 4)
		public int other_element_spellcasting_tier_limit = 0;

		@Config.Name("Discipline Potency Bonus")
		@Config.Comment("Grants the given % bonus of spell Potency to all spell casts of players for their disciplines (not including Sub-Disciplines)."
				+ "\nFor example, an Earth Mage player will get 5% potency bonus for his/her Earth spells if you set this to 5")
		@Config.RangeInt(min = 0, max = 100)
		public int discipline_potency_bonus = 5;

		@Config.Name("Sub-Discipline Base Potency")
		@Config.Comment("With this setting, you can lower (or increase) the default Potency of Sub-Discipline spellcasts. Does not affect the player's 'Primary' discipline(s)")
		@Config.RangeInt(min = -100, max = 50)
		public int sub_discipline_potency_bonus = 0;

		@Config.Name("Possible Elements List")
		@Config.Comment("Defines the selectable elements of the Book of Fates."
				+ "\nPossible values: FIRE, ICE, LIGHTNING, NECROMANCY, EARTH, SORCERY, HEALING, ANCIENT (if Ancient Spellcraft is present)")
		public String[] element_selection_list = {
				"FIRE",
				"ICE",
				"LIGHTNING",
				"NECROMANCY",
				"EARTH",
				"SORCERY",
				"HEALING",
				"ANCIENT"
		};

		@Config.Name("Hardcore Wand Usage")
		@Config.Comment("If enabled, players can only use wands of their own discipline(s)")
		public boolean hardcore_wand_usage = false;

		@Config.Name("Ultra Hardcore Wand Usage")
		@Config.Comment("If enabled, players can only use wands of their own discipline(s) and according to tier caps they have on these disciplines")
		public boolean ultra_hardcore_wand_usage = false;

		@Config.Name("Ancient Spellcraft ntegration")
		@Config.Comment("If the integration is active, the 'ANCIENT' discipline becomes available.")
		@Config.RequiresMcRestart
		public boolean ancient_spellcraft_integration = true;

		@Config.Name("Reskillable Integration")
		@Config.Comment("If the integration is active, the discipline potency bonuses are calculated based on the player's magic skill in Reskillable. "
				+ "\n For example this means that if you have a \"Discipline Potency Bonus\" set to 30 and the maximum level of the magic skill of Reskillable is 32,"
				+ "You'll get the 30 Potency bonus when you reach lvl 30 in your magic skill, and a fraction of the total potency bonus before that, depending on your magic skill."
				+ "\n E.g., for a level 12 magic skill, you'll get (12 / 32) * 30 = 11,25 potency bonus.")
		@Config.RequiresMcRestart
		public boolean reskillable_integration = true;

		@Config.Name("Allow Magic Missile For Any Wizard Players")
		@Config.Comment("If true, anyone who is not set to a non-wizard player can use the Magic Missile spell.")
		public boolean allow_magic_missile = false;

		@Config.Name("Oblivion Scroll Usage Enabled")
		@Config.Comment("If true, players can use the Scroll of Oblivion item to reset their disciplines (if they have the item).")
		public boolean oblivion_scroll_enabled = true;

		@Config.Name("Bonus Chest Scroll")
		@Config.Comment("If true, the Bonus Chest (in a new world) will contain a random Discipline Scroll.")
		public boolean bonus_chest_scroll = true;

		@Config.Name("Discipline Scroll Usage")
		@Config.Comment("If true, Discipline Scrolls can be right clicked to grant their discipline to the player (if the player is not a magicless player and haven't reached the max discipline limit yet).")
		public boolean discipline_scrolls_enabled = true;

		@Config.Name("Sub-Discipline Scroll Usage")
		@Config.Comment("If true, Lesser Discipline Scrolls can be right clicked to grant their sub-discipline to the player (if the player is not a magicless player and haven't reached the max sub-discipline limit yet).")
		public boolean sub_discipline_scrolls_enabled = true;

		@Config.Name("Book of Fates Usage")
		@Config.Comment("If true, the Book of Fates item can be used by players to set their initial discipline.")
		public boolean book_of_fates_enabled = true;

		@Config.Name("Book of Fates Can Override Current Discipline")
		@Config.Comment("If true, the Book of Fates item can be used multiple times to override the current discipline (the book is still consumed after usage!).")
		public boolean book_of_fates_override = true;

		@Config.Name("Add Book of Fates To Starter Inventory")
		@Config.Comment("If true, players will receive the Book of Fates item the first time they log in to a world.")
		public boolean book_of_fates_in_starter_inventory = true;

		@Config.Name("Discipline Scroll Looting")
		@Config.Comment("If true, main discipline scrolls will appear in loot chests.")
		public boolean discipline_scroll_looting = true;

		@Config.Name("Enforce Elemental Armour Set Wearing To Cast")
		@Config.Comment("[UNUSED, upcoming feature] If true, players can only cast spells if they are wearing a full set of wizard robe."
				+ "\n Needless to say, discipline enforcements still apply as well")
		public boolean enforce_full_wizard_robe_set = true;

		@Config.Name("Enforce Discipline Armour Set")
		@Config.Comment("[UNUSED, upcoming feature] Only has an effect if 'Enforce Elemental Armour Set Wearing To Cast' is true."
				+ "\n If true, players can only cast spells if they have a full set of MATCHING wizard armour which MATCHES WITH THE SPELL'S ELEMENT."
				+ "\n Needless to say, discipline enforcements still apply as well")
		public boolean enforce_full_matching_wizard_robe_set = true;

		@Config.Name("Allow Using The Generic Wizard Robe")
		@Config.Comment("[UNUSED, upcoming feature] This only has an effect if 'Enforce Discipline Armour Set' and 'Enforce Discipline Armour Set' are BOTH true!"
				+ "\n If true, players can wear the generic (blue, \"elementless\") robes to cast stuff."
				+ "\n Needless to say, discipline enforcements still apply as well")
		public boolean allow_using_generic_wizard_robe = true;

		@Config.Name("Discipline Scroll Loot Chance")
		@Config.Comment("Determines the chance (roughly) of finding discipline scrolls in the chests defined in the 'Discipline Scroll Loot Inject Locations' list."
				+ "\nIgnored if 'Discipline Scroll Looting' is false or the 'Discipline Scroll Loot Inject Locations' list is empty."
				+ "\nSetting is in percent value, e.g 0.5 means 50% chance to get a scroll in the loot chest.")
		@Config.RangeDouble(min = 0, max = 1)
		public double discipline_scroll_loot_chance = 0.02D;

		@Config.Name("Discipline Scroll Loot Inject Locations")
		@Config.Comment("If 'Discipline Scroll Looting' is enabled, Scrolls can be found in the chests listed here."
				+ "\nIgnored if 'Discipline Scroll Looting' is false.")
		@Config.RequiresMcRestart
		public String[] scroll_loot_inject_locations = {
				"ebwizardry:chests/wizard_tower",
				"ebwizardry:chests/shrine",
				"ebwizardry:chests/obelisk",

				"minecraft:chests/desert_pyramid",
				"minecraft:chests/jungle_temple",
				"minecraft:chests/stronghold_corridor",
				"minecraft:chests/stronghold_crossing",
				"minecraft:chests/stronghold_library",
				"minecraft:chests/igloo_chest",
				"minecraft:chests/woodland_mansion",
				"minecraft:chests/end_city_treasure"};
	}
}