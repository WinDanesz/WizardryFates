package com.windanesz.wizardryfates.integration;

import com.windanesz.wizardryfates.Settings;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;

public class FatesASIntegration {

	public static final String ANCIENT_SPELLCRAFT_MOD_ID = "ancientspellcraft";
	public static final String ANCIENT_ELEMENT = "ancient";

	private static boolean spellcraftLoaded;

	public static void init() {

		spellcraftLoaded = Loader.isModLoaded(ANCIENT_SPELLCRAFT_MOD_ID);
		// nothing specific to do here for now
	}

	public static boolean enabled() {
		return Settings.settings.ancient_spellcraft_integration && spellcraftLoaded;
	}

	public static Style getColor() {
		return new Style().setColor(TextFormatting.GOLD);
	}

	/** Returns the string formatting code which corresponds to the colour of this element. */
	public static String getFormattingCode() {
		return getColor().getFormattingCode();
	}

}
