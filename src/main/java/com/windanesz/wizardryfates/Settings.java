package com.windanesz.wizardryfates;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = WizardryFates.MODID, name = "WizardryFates") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	//	public ResourceLocation[] lootInjectionLocations = toResourceLocations(DEFAULT_LOOT_INJECTION_LOCATIONS);

	//	public ResourceLocation[] artefactInjectionLocations = toResourceLocations(EPIC_ARTEFACT_INJECTION_LOCATIONS);

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
		public static GeneralSettings settings = new GeneralSettings();

		public static class GeneralSettings {

			@Config.Name("Allow Scroll Usage of Other Elements")
			@Config.Comment("Determines whether players can cast scrolls of spells which are not their Discipline (element).")
			@Config.RequiresMcRestart
			public boolean allow_other_scrolls = true;

			@Config.Name("Elemental potency bonus % (additive)")
			@Config.Comment("Grants the given % bonus of elemental Potency to all spell casts of players for spells of their own Discipline. For example, an Earth Mage player will get 5% potency bonus for his/her Earth spells cast with wands and scrolls")
			@Config.RequiresMcRestart
			@Config.RangeInt(min = 0, max = 50)
			public int discipline_potency_bonus = 5;
		}


}