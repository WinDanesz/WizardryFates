package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.integration.FatesASIntegration;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class Discipline {

	public final List<Element> primaryDisciplines;
	public final List<Element> secondaryDisciplines;
	private final boolean magicless;
	public final EntityPlayer player;

	public Discipline(EntityPlayer player, List<Element> primaryDisciplines, List<Element> secondaryDisciplines, boolean magicless) {
		this.player = player;
		this.magicless = magicless;
		this.primaryDisciplines = primaryDisciplines;
		this.secondaryDisciplines = secondaryDisciplines;
	}

	public boolean canPlayerCastThis(Spell spell, SpellCastEvent.Source source) {

		Element element = spell.getElement();
		Tier tier = spell.getTier();

		if (Settings.settings.enforce_full_wizard_robe_set) {

		}

		// prevent casting scrolls if disabled in config
		if (source == SpellCastEvent.Source.SCROLL) {
			return canPlayerUseThisScroll(spell);
		}

		if (source == SpellCastEvent.Source.OTHER) {
			return canPlayerUseMiscItem(spell);
		}
		// if enabled, spells of any element can be casted up to the specified tier
		if (DisciplineUtils.isTierSufficient(Settings.settings.other_element_spellcasting_tier_limit, tier)) {
			return true;
		}

		if (source == SpellCastEvent.Source.WAND) {
			if (!canPlayerUseHeldWands()) {
				return false;
			}
		}

		if (spell == Spells.none) {
			return true;
		}

		if (spell == Spells.magic_missile) {
			if (Settings.settings.allow_magic_missile) {
				return true;
			} else if (FatesASIntegration.enabled() && primaryDisciplines.contains(Element.MAGIC)) {
				return false;
			}
		}

		if (primaryDisciplines.contains(element)) {
			return true;
		}

		if (secondaryDisciplines.contains(element)) {
			return DisciplineUtils.isTierSufficient(Settings.settings.sub_discipline_spellcasting_tier_limit, tier);
		}

		return false;
	}

	public boolean canPlayerUseThisScroll(Spell spell) {
		Tier tier = spell.getTier();

		if (isMagiclessPlayer()) {
			return DisciplineUtils.isTierSufficient(Settings.settings.scroll_tier_limit_for_magicless_players, tier);
		} else {
			return DisciplineUtils.isTierSufficient(Settings.settings.scroll_tier_limit, tier);
		}
	}

	private boolean canPlayerUseHeldWands() {
		if (!Settings.settings.hardcore_wand_usage) {
			return true;
		}

		boolean mainHandHasWand = !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemWand;
		boolean offhandHasHandWand = !player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemWand;
		boolean bothHands = mainHandHasWand && offhandHasHandWand;
		boolean mainHandCanUseWand = false;
		boolean offHandCanUseWand = false;
		if (mainHandHasWand) {
			mainHandCanUseWand = checkHeldItemWand(player.getHeldItemMainhand());
			if (!bothHands) {
				return mainHandCanUseWand;
			}
		}
		if (offhandHasHandWand) {
			offHandCanUseWand = checkHeldItemWand(player.getHeldItemOffhand());
			if (!bothHands) {
				return offHandCanUseWand;
			}
		}
		if (bothHands) {
			return mainHandCanUseWand && offHandCanUseWand;
		}
		return true;
	}

	private boolean checkHeldItemWand(ItemStack stack) {
		Tier wandTier = ((ItemWand) stack.getItem()).tier;
		Element wandElement = ((ItemWand) stack.getItem()).element;
		if (primaryDisciplines.contains(wandElement)) {
			return true;
		} else if (secondaryDisciplines.contains(wandElement) && Settings.settings.ultra_hardcore_wand_usage) {
			return DisciplineUtils.isTierSufficient(Settings.settings.sub_discipline_spellcasting_tier_limit, wandTier);
		}

		return false;
	}

	private boolean canPlayerUseMiscItem(Spell spell) {
		if (magicless) {
			return Settings.settings.allow_other_sources_for_non_wizards;
		}
		if (!Settings.settings.allow_other_sources) {
			return primaryDisciplines.contains(spell.getElement()) || secondaryDisciplines.contains(spell.getElement());
		}
		return true;
	}

	public boolean isMagiclessPlayer() {
		return magicless;
	}

	//	/**
	//	 * Returns whether the given entity is wearing a full set of wizard armour of the given class and element.
	//	 * @param entity The entity to query.
	//	 * @param element The element to check, or null to accept any element as long as they are all the same.
	//	 * @param armourClass The class to check, or null to accept any class as long as they are all the same.
	//	 * @return True if the entity is wearing a full set of the given element and class, false otherwise.
	//	 */
	//	private static boolean isWearingFullSet(EntityLivingBase entity, @Nullable Element element, @Nullable ArmourClass armourClass){
	//		ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
	//		if(!(helmet.getItem() instanceof ItemWizardArmour)) return false;
	//		Element e = element == null ? ((ItemWizardArmour)helmet.getItem()).element : element;
	//		ArmourClass ac = armourClass == null ? ((ItemWizardArmour)helmet.getItem()).armourClass : armourClass;
	//		return Arrays.stream(InventoryUtils.ARMOUR_SLOTS)
	//				.allMatch(s -> entity.getItemStackFromSlot(s).getItem() instanceof ItemWizardArmour
	//						&& ((ItemWizardArmour)entity.getItemStackFromSlot(s).getItem()).element == e
	//						&& ((ItemWizardArmour)entity.getItemStackFromSlot(s).getItem()).armourClass == ac);
	//	}
}
