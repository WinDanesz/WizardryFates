package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.integration.FatesReskillableIntegration;
import com.windanesz.wizardryfates.item.ItemDisciplineBook;
import com.windanesz.wizardryfates.registry.WizardryFatesItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class WFEventHandler {

	private WFEventHandler() {} // No instances!

	@SubscribeEvent(priority = EventPriority.HIGH) // Apply before Forfeits
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (event.getCaster() instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) event.getCaster();
			Discipline discipline = DisciplineUtils.getPlayerDisciplines((EntityPlayer) event.getCaster());
			Spell spell = event.getSpell();
			Element element = spell.getElement();

			// Check if spell cast is possible
			if (!discipline.canPlayerCastThis(spell, event.getSource())) {
				if (!player.world.isRemote) {
					player.sendStatusMessage(new TextComponentTranslation("gui.wizardryfates:spellcast_failed"), true);
				}
				event.setCanceled(true);
			}

			// Apply potency bonus
			if (discipline.primaryDisciplines.contains(element)) {
				// apply primary discipline modifiers
				if (Settings.settings.discipline_potency_bonus != 0) {
					SpellModifiers modifiers = event.getModifiers();
					float potency = modifiers.get(SpellModifiers.POTENCY);
					float bonus = Settings.settings.discipline_potency_bonus * 0.01f;
					if (FatesReskillableIntegration.enabled()) {
						float skillPercent = FatesReskillableIntegration.getProgressPercent(player);
						bonus *= skillPercent;
					}
					modifiers.set(SpellModifiers.POTENCY, potency + bonus, false);
				}
			} else if (Settings.settings.sub_discipline_potency_bonus != 0 && discipline.secondaryDisciplines.contains(element)) {
				// apply sub-discipline modifiers

				SpellModifiers modifiers = event.getModifiers();
				float potency = modifiers.get(SpellModifiers.POTENCY);
				float bonus = Settings.settings.sub_discipline_potency_bonus * 0.01f;
				if (FatesReskillableIntegration.enabled()) {
					float skillPercent = FatesReskillableIntegration.getProgressPercent(player);
					bonus *= skillPercent;
				}
				modifiers.set(SpellModifiers.POTENCY, potency + bonus, false);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

		if (Settings.settings.book_of_fates_in_starter_inventory) {
			WizardData data = WizardData.get(event.player);
			if (data != null) {
				Boolean disciplineTag = data.getVariable(ItemDisciplineBook.RECEIVED_FATES_BOOK);

				if (disciplineTag == null || !disciplineTag) {
					ItemStack bookStack = new ItemStack(WizardryFatesItems.book_of_fates);

					if (!event.player.world.isRemote && !event.player.addItemStackToInventory(bookStack)) {
						event.player.dropItem(bookStack, false);
					}

					data.setVariable(ItemDisciplineBook.RECEIVED_FATES_BOOK, Boolean.TRUE);
					data.sync();
				}
			}

			if (!event.player.world.isRemote && Settings.settings.whitelist_for_main_disciplines_assigned_to_new_players_joining_world.length > 0) {
				List<Element> elements = Settings.convertToElementList(Settings.settings.whitelist_for_main_disciplines_assigned_to_new_players_joining_world);
				for (Element element : elements) {
					DisciplineUtils.addMainDiscipline(event.player, element, false, event.player);
				}
			}

			if (!event.player.world.isRemote && Settings.settings.whitelist_for_sub_disciplines_assigned_to_new_players_joining_world.length > 0) {
				List<Element> elements = Settings.convertToElementList(Settings.settings.whitelist_for_sub_disciplines_assigned_to_new_players_joining_world);
				for (Element element : elements) {
					DisciplineUtils.addSubDiscipline(event.player, element, false, event.player);
				}
			}
		}
	}
}
