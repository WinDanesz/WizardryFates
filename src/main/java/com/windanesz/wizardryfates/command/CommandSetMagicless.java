package com.windanesz.wizardryfates.command;

import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class CommandSetMagicless extends CommandBase {

	private static final List<String> vals = new ArrayList<>();

	static {
		vals.add("true");
		vals.add("false");
	}

	public static final String COMMAND = "setmagicless";

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
				return getListOfStringsMatchingLastWord(arguments, vals);
		}
		return super.getTabCompletions(server, sender, arguments, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {

		if (arguments.length != getRequiredArgsCount()) {
			throw new WrongUsageException(getUsage(sender));
		}

		EntityPlayer targetPlayer = getPlayer(server, sender, arguments[0]);

		boolean magicless = Boolean.parseBoolean(arguments[1]);

		TextComponentTranslation textComponentTranslation;
		if (DisciplineUtils.setMagicless(targetPlayer, magicless)) {
			if (magicless) {
				textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".execute_true", targetPlayer.getDisplayName());
			} else {
				textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".execute_false", targetPlayer.getDisplayName());
			}
		} else {
			textComponentTranslation = new TextComponentTranslation(getUnlocalizedName() + ".failure", targetPlayer.getDisplayName());
		}
		sender.sendMessage(textComponentTranslation);
	}

	public static int getRequiredArgsCount() { return 2; }

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return index == 0;
	}
}
