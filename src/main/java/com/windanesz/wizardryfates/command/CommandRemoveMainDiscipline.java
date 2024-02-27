package com.windanesz.wizardryfates.command;

import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.Discipline;
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

import java.util.List;

public class CommandRemoveMainDiscipline extends CommandBase {

	public static final String COMMAND = "removemaindiscipline";

	public String getName() {
		return COMMAND;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return getUnlocalizedName() + ".usage";
	}

	public static String getUnlocalizedName() {
		return "commands." + WizardryFates.MODID + ":" + COMMAND;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments, BlockPos pos) {

		switch (arguments.length) {
			case 1:
				return getListOfStringsMatchingLastWord(arguments, server.getOnlinePlayerNames());
			case 2:
				return getListOfStringsMatchingLastWord(arguments, DisciplineUtils.ELEMENTS_STRINGS);
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {

		if (arguments.length != getRequiredArgsCount()) {
			throw new WrongUsageException(getUsage(sender));
		}

		EntityPlayerMP targetPlayer = getPlayer(server, sender, arguments[0]);
		Element elementToRemove = Utils.getElementFromName(arguments[1].toLowerCase());

		Discipline discipline = DisciplineUtils.getPlayerDisciplines(targetPlayer);

		if (!discipline.primaryDisciplines.contains(elementToRemove)) {
			sender.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_remove_unassigned_discipline"));
			return;
		}

		if (DisciplineUtils.removePrimaryDiscipline(targetPlayer, elementToRemove)) {
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".execute",
					targetPlayer.getDisplayName(),
					Utils.getElementWithStyleFormat(elementToRemove));
			sender.sendMessage(textComponentTranslation);
		} else {
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".failure", targetPlayer.getDisplayName(), elementToRemove);
			sender.sendMessage(textComponentTranslation);
		}
	}

	public static int getRequiredArgsCount() { return 2; }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
}
