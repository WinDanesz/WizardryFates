package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.integration.FatesASIntegration;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DisciplineUtils {
	public static final IStoredVariable<NBTTagCompound> DISCIPLINE = IStoredVariable.StoredVariable.ofNBT("discipline", Persistence.ALWAYS).setSynced();

	public static final List<String> ELEMENTS_STRINGS = new ArrayList();

	public static final String PRIMARY_DISCIPLINE_TAG = "primary_disciplines";
	public static final String SECONDARY_DISCIPLINE_TAG = "secondary_disciplines";
	public static final String MAGICLESS_TAG = "magicless";

	static {
		ELEMENTS_STRINGS.add(Element.SORCERY.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.HEALING.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.NECROMANCY.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.ICE.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.FIRE.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.LIGHTNING.getName().toUpperCase());
		ELEMENTS_STRINGS.add(Element.EARTH.getName().toUpperCase());

		if (FatesASIntegration.enabled()) {
			ELEMENTS_STRINGS.add(FatesASIntegration.ANCIENT_ELEMENT.toUpperCase());
		}
	}

	private DisciplineUtils() {}

	/**
	 * Called from {@link com.windanesz.wizardryfates.WizardryFates#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)}
	 * Registers the player-specific WizardData attributes.
	 */
	public static void init() {
		WizardData.registerStoredVariables(DISCIPLINE);
	}

	public static Discipline getPlayerDisciplines(EntityPlayer player) {
		WizardData data = WizardData.get(player);

		if (data != null) {
			NBTTagCompound disciplineTag = data.getVariable(DISCIPLINE);

			if (disciplineTag != null) {
				List<Element> primaryElements = new ArrayList<>();
				List<Element> secondaryElements = new ArrayList<>();
				boolean magicless = false;

				if (disciplineTag.hasKey(PRIMARY_DISCIPLINE_TAG)) {
					NBTTagList primaryDisciplines = (disciplineTag.getTagList(PRIMARY_DISCIPLINE_TAG, 8));
					for (NBTBase primaryDiscipline : primaryDisciplines) {
						primaryElements.add(Utils.getElementFromName((((NBTTagString) primaryDiscipline).getString().toLowerCase())));
					}
				}

				if (disciplineTag.hasKey(SECONDARY_DISCIPLINE_TAG)) {
					NBTTagList secondaryDisciplines = (disciplineTag.getTagList(SECONDARY_DISCIPLINE_TAG, 8));
					for (NBTBase secondaryDiscipline : secondaryDisciplines) {
						secondaryElements.add(Utils.getElementFromName((((NBTTagString) secondaryDiscipline).getString().toLowerCase())));
					}
				}

				if (disciplineTag.hasKey(MAGICLESS_TAG)) {
					magicless = disciplineTag.getBoolean(MAGICLESS_TAG);
				}

				return new Discipline(player, primaryElements, secondaryElements, magicless);
			}
		}
		return new Discipline(player, new ArrayList<>(), new ArrayList<>(), false);
	}

	public static boolean addPrimaryDiscipline(EntityPlayer player, Element element, boolean purgeExisting, EntityPlayer caller) {
		return addDiscipline(player, element, PRIMARY_DISCIPLINE_TAG, purgeExisting, caller);
	}

	public static boolean addSecondaryDiscipline(EntityPlayer player, Element element, boolean purgeExisting, EntityPlayer caller) {
		return addDiscipline(player, element, SECONDARY_DISCIPLINE_TAG, purgeExisting, caller);
	}

	private static boolean addDiscipline(EntityPlayer player, Element element, String typeTag, boolean purgeExisting, @Nullable EntityPlayer caller) {
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(player);

		if (discipline.isMagiclessPlayer()) {
			if (caller != null && !caller.world.isRemote)
				caller.sendMessage(new TextComponentTranslation("gui.wizardryfates:cannot_add_discipline_to_magicless_player"));
			return false;
		}

		if (typeTag.equals(PRIMARY_DISCIPLINE_TAG) && discipline.primaryDisciplines.size() == Settings.settings.max_main_discipline_count) {
			if (caller != null && !caller.world.isRemote) {
				if (caller == player) {
					caller.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_receive_more_primary_disciplines"));
				} else {
					caller.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_add_more_primary_disciplines", player.getDisplayName()));
				}
			}
			return false;
		} else if (typeTag.equals(SECONDARY_DISCIPLINE_TAG) && discipline.secondaryDisciplines.size() == Settings.settings.max_sub_discipline_count) {
			if (caller != null && !caller.world.isRemote) {
				if (caller == player) {
					caller.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_receive_more_sub_disciplines"));
				} else {
					caller.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_add_more_sub_disciplines", player.getDisplayName()));
				}
			}
			return false;
		}

		WizardData data = WizardData.get(player);
		List<Element> disciplines = new ArrayList<>();

		if (data != null) {
			NBTTagCompound disciplineTag = data.getVariable(DISCIPLINE);

			if (disciplineTag != null) {

				if (disciplineTag.hasKey(typeTag)) {
					NBTTagList disciplineTagTagList = (disciplineTag.getTagList(typeTag, 8));
					for (NBTBase base : disciplineTagTagList) {
						String test = (((NBTTagString) base).getString().toLowerCase());
						disciplines.add(Utils.getElementFromName(test));
					}
				}
			} else {
				disciplineTag = new NBTTagCompound();
			}

			if (!disciplines.contains(element)) {
				disciplines.add(element);
				NBTTagList list = new NBTTagList();
				if (purgeExisting) {
					NBTTagString elementString = new NBTTagString(element.getName());
					list.appendTag(elementString);
				} else {
					for (Element element1 : disciplines) {
						NBTTagString elementString = new NBTTagString(element1.getName());
						list.appendTag(elementString);
					}
				}
				disciplineTag.setTag(typeTag, list);

				if (list.tagCount() > Settings.settings.max_main_discipline_count) {
					return false;
				}

				data.setVariable(DISCIPLINE, disciplineTag);
				data.sync();
				return true;
			}
		}
		return false;
	}

	public static boolean removePrimaryDiscipline(EntityPlayer player, Element element) {
		return removeDiscipline(player, element, PRIMARY_DISCIPLINE_TAG);
	}

	public static boolean removeSubDiscipline(EntityPlayer player, Element element) {
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(player);
		if (!discipline.secondaryDisciplines.contains(element)) {
			return false;
		}
		return removeDiscipline(player, element, SECONDARY_DISCIPLINE_TAG);
	}

	private static boolean removeDiscipline(EntityPlayer player, Element element, String typeTag) {
		WizardData data = WizardData.get(player);

		if (data != null) {
			NBTTagCompound disciplineTag = data.getVariable(DISCIPLINE);

			if (disciplineTag != null) {

				if (disciplineTag.hasKey(typeTag)) {
					NBTTagList disciplineTagTagList = (disciplineTag.getTagList(typeTag, 8));
					NBTTagList list = new NBTTagList();

					for (NBTBase base : disciplineTagTagList) {
						String test = (((NBTTagString) base).getString().toLowerCase());

						// here is the magic, we just don't add the one we want to remove
						if (element != Utils.getElementFromName(test)) {
							NBTTagString elementString = new NBTTagString(test);
							list.appendTag(elementString);
						}
					}
					disciplineTag.setTag(typeTag, list);
					data.setVariable(DISCIPLINE, disciplineTag);
					data.sync();
					return true;
				}
			}
		}
		return false;
	}

	public static boolean setMagicless(EntityPlayer player, Boolean magicless) {
		WizardData data = WizardData.get(player);

		if (data != null) {
			NBTTagCompound disciplineTag = data.getVariable(DISCIPLINE);

			if (disciplineTag != null) {
				disciplineTag = new NBTTagCompound();

				disciplineTag.setBoolean(MAGICLESS_TAG, magicless);
				data.setVariable(DISCIPLINE, disciplineTag);
				data.sync();
				return true;
			}
		}
		return false;
	}

	public static boolean purgeDisciplines(EntityPlayer player) {
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(player);
		WizardData data = WizardData.get(player);

		if (data != null) {
			NBTTagCompound disciplineTag = data.getVariable(DISCIPLINE);

			if (disciplineTag != null) {
				disciplineTag = new NBTTagCompound();

				disciplineTag.setBoolean(MAGICLESS_TAG, discipline.isMagiclessPlayer());
				data.setVariable(DISCIPLINE, disciplineTag);
				data.sync();
				return true;
			}
		}
		return false;

	}

	public static boolean isTierSufficient(int setting, Tier tier) {
		return setting >= tier.ordinal() + 1;
	}
}
