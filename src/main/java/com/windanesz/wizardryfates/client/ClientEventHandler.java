package com.windanesz.wizardryfates.client;

import com.windanesz.wizardryfates.handler.Discipline;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.client.gui.GuiArcaneWorkbench;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.inventory.ContainerArcaneWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {

	private ClientEventHandler() {} // no instances

	private static final int X_POS = 298;
	private static final int ICON_X_POS = 304;
	private static final int ICON_X_SPACING = 10;

	@SubscribeEvent
	public static void onGuiDrawForegroundEvent(GuiContainerEvent.DrawForeground event) {
		if (!(event.getGuiContainer() instanceof GuiArcaneWorkbench)) return;

		EntityPlayerSP player = Minecraft.getMinecraft().player;
		int bookshelfOffset = player.openContainer instanceof ContainerArcaneWorkbench && ((ContainerArcaneWorkbench) player.openContainer).hasBookshelves() ? 0 : -122;

		drawMainDisciplines(bookshelfOffset);
		drawSubDisciplines(bookshelfOffset);
	}

	private static void drawMainDisciplines(int bookshelfOffset) {
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(Minecraft.getMinecraft().player);

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(GuiArcaneWorkbench.texture);
		mc.fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:main_discipline"), ICON_X_POS + bookshelfOffset, 196 - 20, 0xFFFFFF);

		int iconVerticalPos = 0;
		for (int i = 0; i < discipline.primaryDisciplines.size(); i++) {
			Element element = discipline.primaryDisciplines.get(i);
			mc.renderEngine.bindTexture(element.getIcon());
			DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset + ICON_X_SPACING * i, 185 + iconVerticalPos, 8, 8);
		}
	}

	private static void drawSubDisciplines(int bookshelfOffset) {
		Discipline discipline = DisciplineUtils.getPlayerDisciplines(Minecraft.getMinecraft().player);

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(GuiArcaneWorkbench.texture);
		mc.fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:sub_disciplines"), ICON_X_POS + bookshelfOffset, 196, 0xFFFFFF);

		for (int i = 0; i < discipline.secondaryDisciplines.size(); i++) {
			Element element = discipline.secondaryDisciplines.get(i);
			mc.renderEngine.bindTexture(element.getIcon());
			DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset + ICON_X_SPACING * i, 206, 8, 8);
		}
	}
}
