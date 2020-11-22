package com.windanesz.wizardryfates.registry;

import com.windanesz.wizardryfates.WizardryFates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sounds {

	public static final SoundEvent DISCIPLINE_REVELATION = createSound("discipline_revelation");
	public static final SoundEvent DISCIPLINE_BOOK_USE = createSound("discipline_book_use");
	public static final SoundEvent LESSER_DISCIPLINE_SCROLL_USE = createSound("lesser_discipline_scroll_use");
	public static final SoundEvent DISCIPLINE_SCROLL_USE = createSound("discipline_scroll_use");

	public static SoundEvent createSound(String name) {
		return createSound(WizardryFates.MODID, name);
	}

	/**
	 * Creates a sound with the given name, to be read from {@code assets/[modID]/sounds.json}.
	 */
	public static SoundEvent createSound(String modID, String name) {
		// All the setRegistryName methods delegate to this one, it doesn't matter which you use.
		return new SoundEvent(new ResourceLocation(modID, name)).setRegistryName(name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(DISCIPLINE_REVELATION);
		event.getRegistry().register(DISCIPLINE_BOOK_USE);
		event.getRegistry().register(LESSER_DISCIPLINE_SCROLL_USE);
		event.getRegistry().register(DISCIPLINE_SCROLL_USE);
	}
}
