package com.windanesz.wizardryfates.command;

import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.Discipline;
import com.windanesz.wizardryfates.handler.DisciplineMode;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import electroblob.wizardry.constants.Element;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Iterator;
import java.util.List;

public class CommandGetDiscipline extends CommandBase {

	public static final String COMMAND = "getdiscipline";

	public String getName() {
		return COMMAND;
	}

	@Override
	public int getRequiredPermissionLevel() { return 0; }

	@Override
	public String getUsage(ICommandSender sender) {
		return getUnlocalizedName() + ".usage";
	}

	public static String getUnlocalizedName() {
		return "commands." + WizardryFates.MODID + ":" + COMMAND;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments, BlockPos pos) {

		if (arguments.length == 1) {
			return getListOfStringsMatchingLastWord(arguments, server.getOnlinePlayerNames());
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {

		if (arguments.length < getRequiredArgsCount()) {
			throw new WrongUsageException(getUsage(sender));
		}

		EntityPlayerMP entityplayermp = arguments.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, arguments[0]);

		Discipline discipline = DisciplineUtils.getPlayerDisciplines(entityplayermp);
		StringBuilder disciplines = new StringBuilder();

		switch (DisciplineMode.getActiveMode()) {
			case SINGLE_DISCIPLINE_MODE:
				if (discipline.primaryDisciplines.isEmpty()) {
					disciplines.append("none");
				} else {
					disciplines.append(Utils.getElementWithStyleFormat(discipline.primaryDisciplines.get(0)));
				}
				break;

			case MULTI_DISCIPLINE_MODE:
				disciplines.append(getPrimaryDisciplines(discipline));
				break;

			case SUB_DISCIPLINE_MODE:
				disciplines.append("Primary Discipline: ");
				if (discipline.primaryDisciplines.isEmpty()) {
					disciplines.append("none");
				} else {
					disciplines.append(Utils.getElementWithStyleFormat(discipline.primaryDisciplines.get(0)));
				}
				disciplines.append(". ");
				disciplines.append(getSecondaryDisciplines(discipline));
				break;

		}

		TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".execute", entityplayermp.getDisplayName(), disciplines.toString());
		sender.sendMessage(textComponentTranslation);
	}

	private String getPrimaryDisciplines(Discipline discipline) {
		StringBuilder disciplines = new StringBuilder();

		if (discipline.primaryDisciplines.isEmpty()) {
			disciplines.append("Primary Disciplines: none");
		} else {
			disciplines.append("Primary Disciplines: ");
			Iterator<Element> iterator = discipline.primaryDisciplines.iterator();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				disciplines.append(Utils.getElementWithStyleFormat(element));
				if (iterator.hasNext()) {
					disciplines.append(", ");
				} else {
					disciplines.append(". ");
				}
			}
		}

		return disciplines.toString();
	}

	private String getSecondaryDisciplines(Discipline discipline) {
		StringBuilder disciplines = new StringBuilder();

		if (discipline.secondaryDisciplines.isEmpty()) {
			disciplines.append("Secondary Disciplines: none");
		} else {
			disciplines.append("Secondary Disciplines: ");
			Iterator<Element> iterator = discipline.secondaryDisciplines.iterator();
			while (iterator.hasNext()) {
				Element element = iterator.next();
				disciplines.append(element.getFormattingCode()).append(element.getDisplayName());
				if (iterator.hasNext()) {
					disciplines.append(", ");
				}
			}
		}

		return disciplines.toString();
	}

	public static int getRequiredArgsCount() { return 0; }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
}
