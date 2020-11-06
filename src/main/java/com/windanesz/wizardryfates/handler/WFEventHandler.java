package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.Settings;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WFEventHandler {

	private WFEventHandler() {} // No instances!

	@SubscribeEvent
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (event.getCaster() instanceof EntityPlayer) {

			if (event.getSource() == SpellCastEvent.Source.SCROLL && Settings.settings.allow_other_scrolls) {
				return;
			}

			Spell spell = event.getSpell();
			Element element = spell.getElement();
			EntityPlayer player = (EntityPlayer) event.getCaster();
			Element discipline = Discipline.getPlayerDiscipline((EntityPlayer) event.getCaster());
			if (element != discipline) {

				WizardData data = WizardData.get(player);
				if (data != null) {
					if (!data.hasSpellBeenDiscovered(spell)) {
						if (!player.world.isRemote) {
							player.sendStatusMessage(new TextComponentTranslation(I18n.format("gui.wizardryfates:unknown_other_element")), true);
						}
					} else if (!player.world.isRemote) {
						player.sendStatusMessage(new TextComponentTranslation(I18n.format("gui.wizardryfates:failed_other_element")), true);
					}
				}

				event.setCanceled(true);
			}

			if (element == discipline && Settings.settings.discipline_potency_bonus != 0D) {
				SpellModifiers modifiers = event.getModifiers();
				modifiers.set(SpellModifiers.POTENCY, 1f + Settings.settings.discipline_potency_bonus, false);
				modifiers = event.getModifiers();
			}

		}

	}

}
