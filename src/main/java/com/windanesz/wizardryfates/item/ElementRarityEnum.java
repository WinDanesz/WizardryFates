package com.windanesz.wizardryfates.item;

import net.minecraft.util.text.TextFormatting;

public enum ElementRarityEnum implements net.minecraftforge.common.IRarity {

	MAGIC(TextFormatting.GOLD, "magic"),
	FIRE(TextFormatting.DARK_RED, "fire"),
	ICE(TextFormatting.AQUA, "ice"),
	LIGHTNING(TextFormatting.DARK_AQUA, "lightning"),
	NECROMANCY(TextFormatting.DARK_PURPLE, "necromancy"),
	EARTH(TextFormatting.DARK_GREEN, "earth"),
	SORCERY(TextFormatting.GREEN, "sorcery"),
	HEALING(TextFormatting.YELLOW, "healing");

	/**
	 * The color assigned to this rarity type.
	 */
	public final TextFormatting color;
	/**
	 * Rarity name.
	 */
	public final String rarityName;

	private ElementRarityEnum(TextFormatting color, String name) {
		this.color = color;
		this.rarityName = name;
	}

	@Override
	public TextFormatting getColor() {
		return this.color;
	}

	@Override
	public String getName() {
		return this.rarityName;
	}

	/**
	 * Returns the element with the given name, or throws an {@link java.lang.IllegalArgumentException} if no such
	 * element exists.
	 */
	public static ElementRarityEnum fromName(String name) {

		for (ElementRarityEnum element : values()) {
			if (element.rarityName.equals(name.toLowerCase()))
				return element;
		}

		throw new IllegalArgumentException("No such element with unlocalised name: " + name);
	}
}

