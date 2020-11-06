package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.registry.Sounds;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Discipline {

	public static final List<Element> ELEMENTS = Collections.unmodifiableList(Arrays.asList(Element.values()));
	public static final List<Element> ELEMENTS_NO_MAGIC = ELEMENTS.subList(1, ELEMENTS.size() - 1);
	private static final int SIZE = ELEMENTS.size();
	private static Random random = new Random();

	public static final IStoredVariable<NBTTagCompound> DISCIPLINE = IStoredVariable.StoredVariable.ofNBT("discipline", Persistence.ALWAYS).setSynced();

	private Discipline() {}

	/**
	 * Called from {@link com.windanesz.wizardryfates.WizardryFates#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)}
	 * Registers the player-specific WizardData attributes.
	 */
	public static void init() {
		WizardData.registerStoredVariables(DISCIPLINE);

	}

	@Nullable
	public static Element getPlayerDiscipline(EntityPlayer player) {
		WizardData data = WizardData.get(player);

		if (data != null) {
			NBTTagCompound discipline = data.getVariable(DISCIPLINE);

			if (discipline == null || !discipline.hasKey("discipline")) {
				playSound(player);
				NBTTagCompound compound = new NBTTagCompound();
				Element element = getRandomElement();
				compound.setString("discipline", element.getName());
				data.setVariable(DISCIPLINE, compound);
				data.sync();
				return element;
			} else {
				return Element.fromName(discipline.getString("discipline"));
			}
		}

		return null;
	}

	@Nullable
	public static void setPlayerDiscipline(EntityPlayer player, Element element) {
		WizardData data = WizardData.get(player);
		if (data != null) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("discipline", element.getName());
			data.setVariable(DISCIPLINE, compound);
			data.sync();
		}
	}

	public static Element getRandomElement() {
		int index = getRandomNumberInRange(1, SIZE);
		return ELEMENTS.get(index);
	}

	public static int getRandomNumberInRange(int min, int max) {
		return random.nextInt((max - min)) + min;
	}

	public static void playSound(EntityPlayer player) {
		player.world.playSound(player.posX, player.posY, player.posZ, Sounds.DISCIPLINE_REVELATION, SoundCategory.PLAYERS, 1F, 1.0F, false);

	}
}
