package com.windanesz.wizardryfates.command;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.Discipline;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import electroblob.wizardry.constants.Element;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CommandAddMainDiscipline extends CommandBase {

	public static final String COMMAND = "addmaindiscipline";

	public String getName() {
		return COMMAND;
	}

	@Override
	public int getRequiredPermissionLevel(){
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments, BlockPos pos){

		switch(arguments.length){
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

		EntityPlayer targetPlayer = getPlayer(server, sender, arguments[0]);
		Element newElement;
		try {
			newElement = Utils.getElementFromName(arguments[1]);
		}
		catch (IllegalArgumentException e) {
			sender.sendMessage(new TextComponentTranslation("message.wizardryfates:invalid_element_name").setStyle((new Style()).setColor(TextFormatting.RED)));
			return;
		}
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(targetPlayer);

		if (discipline.isMagiclessPlayer()) {
			sender.sendMessage(new TextComponentTranslation("gui.wizardryfates:cannot_add_discipline_to_magicless_player").setStyle((new Style()).setColor(TextFormatting.RED)));
			return;
		}

		if (discipline.primaryDisciplines.contains(newElement)) {
			sender.sendMessage(new TextComponentTranslation("message.wizardryfates:already_assigned_as_primary").setStyle((new Style()).setColor(TextFormatting.RED)));
			return;
		}

		if (discipline.secondaryDisciplines.contains(newElement)) {
			sender.sendMessage(new TextComponentTranslation("message.wizardryfates:already_assigned_as_sub_discipline").setStyle((new Style()).setColor(TextFormatting.RED)));
			return;
		}

		if (discipline.primaryDisciplines.size() >= Settings.settings.max_main_discipline_count) {
			sender.sendMessage(new TextComponentTranslation("message.wizardryfates:cannot_add_more_primary_disciplines").setStyle((new Style()).setColor(TextFormatting.RED)));
			return;
		}

		EntityPlayer senderPlayer = sender instanceof EntityPlayer ? (EntityPlayer) sender : null;
		if (DisciplineUtils.addMainDiscipline(targetPlayer, newElement, false, senderPlayer)) {
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".execute",
					targetPlayer.getDisplayName(),
					Utils.getElementWithStyleFormat(newElement));
			sender.sendMessage(textComponentTranslation);
		} else {
			TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".failure",
					targetPlayer.getDisplayName(), Utils.getElementWithStyleFormat(newElement));
			sender.sendMessage(textComponentTranslation);
		}
	}

	public static int getRequiredArgsCount() { return 2; }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
}
