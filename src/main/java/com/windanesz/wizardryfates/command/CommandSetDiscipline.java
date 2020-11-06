package com.windanesz.wizardryfates.command;

import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.Discipline;
import electroblob.wizardry.constants.Element;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

import static com.windanesz.wizardryfates.handler.Discipline.ELEMENTS_NO_MAGIC;

public class CommandSetDiscipline extends CommandBase {

	public String getName() {
		return "setdiscipline";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands." + WizardryFates.MODID + ":setdiscipline.usage";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] arguments,
			BlockPos pos){
		switch(arguments.length){
			case 1:
				return getListOfStringsMatchingLastWord(arguments, server.getOnlinePlayerNames());
			case 2:
				return getListOfStringsMatchingLastWord(arguments, ELEMENTS_NO_MAGIC);
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {
		EntityPlayerMP player = null;

		try{
			player = getCommandSenderAsPlayer(sender);
		}catch (PlayerNotFoundException exception){
			// Nothing here since the player specifying is done later, I just don't want it to throw an exception
			// here.
		}

		EntityPlayer targetPlayer = getPlayer(server, sender, arguments[0]);
		Element newElement = Element.fromName(arguments[1].toLowerCase());
		Element oldElement = Discipline.getPlayerDiscipline(targetPlayer);

		Discipline.setPlayerDiscipline(targetPlayer, newElement);

		TextComponentTranslation TextComponentTranslation2 = new TextComponentTranslation("commands." + WizardryFates.MODID + ":setdiscipline.updated", targetPlayer.getDisplayName(), oldElement, newElement);
		player.sendMessage(TextComponentTranslation2);
		return;
	}
}
