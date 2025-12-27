package com.windanesz.wizardryfates;

import com.windanesz.wizardryfates.client.gui.GuiDisciplineBook;
import com.windanesz.wizardryfates.client.gui.GuiSubDisciplineBook;
import com.windanesz.wizardryfates.item.ItemDisciplineBook;
import com.windanesz.wizardryfates.item.ItemSubDisciplineBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class FatesGuiHandler implements IGuiHandler {

	/**
	 * Incrementable index for the gui ID
	 */
	private static int nextGuiId = 0;

	public static final int BOOK_OF_FATES = nextGuiId++;
	public static final int BOOK_OF_SUB_DISCIPLINES = nextGuiId++;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id == BOOK_OF_FATES && (player.getHeldItemMainhand().getItem() instanceof ItemDisciplineBook || player.getHeldItemOffhand().getItem() instanceof ItemDisciplineBook)) {
			return new GuiDisciplineBook();
		}

		if (id == BOOK_OF_SUB_DISCIPLINES && (player.getHeldItemMainhand().getItem() instanceof ItemSubDisciplineBook || player.getHeldItemOffhand().getItem() instanceof ItemSubDisciplineBook)) {
			return new GuiSubDisciplineBook();
		}
		return null;
	}
}
