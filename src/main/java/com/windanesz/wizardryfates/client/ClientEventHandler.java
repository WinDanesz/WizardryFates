package com.windanesz.wizardryfates.client;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.handler.Discipline;
import com.windanesz.wizardryfates.handler.DisciplineMode;
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

	// Add the discipline info into the arcane workbench gui
	@SubscribeEvent
	public static void onGuiDrawForegroundEvent(GuiContainerEvent.DrawForeground event) {

		if (event.getGuiContainer() instanceof GuiArcaneWorkbench) {

			// Bookshelf interface
			EntityPlayerSP player = Minecraft.getMinecraft().player;

			// apply bookshelf gui offset
			int bookshelfOffset = -122;
			if (player.openContainer instanceof ContainerArcaneWorkbench && ((ContainerArcaneWorkbench) player.openContainer).hasBookshelves()) {
				bookshelfOffset = 0;
			}

			int modeYOffset = 0;
			int iconVerticalPos = 21;

			DisciplineMode mode = DisciplineMode.getActiveMode();
			// apply discipline mode Y offset
			if (mode == DisciplineMode.SUB_DISCIPLINE_MODE) {
				modeYOffset = 20;
				iconVerticalPos = 0;
			}

			Discipline discipline = DisciplineUtils.getPlayerDisciplines(Minecraft.getMinecraft().player);

			Minecraft.getMinecraft().renderEngine.bindTexture(GuiArcaneWorkbench.texture);

			/*
			Removed the background rectangle for now because of the Z index issues
			// draw the top half of the rect
						GlStateManager.pushMatrix();
						GlStateManager.translate(0,0,-100000000);
						DrawingUtils.drawTexturedRect(X_POS + bookshelfOffset, 190 - modeYOffset, 176, 0, 144, 20 + modeYOffset, 512, 512);
			// draw the bottom half of the rect
						DrawingUtils.drawTexturedRect(X_POS + bookshelfOffset, 200, 176, 200, 144, 20, 512, 512);
						GlStateManager.popMatrix();
			*/

			// draw the labels and icons
			if (mode == DisciplineMode.SINGLE_DISCIPLINE_MODE) {
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:discipline"), 304 + bookshelfOffset, 196 - modeYOffset, 0xFFFFFF);
				if (!discipline.primaryDisciplines.isEmpty()) {
					Element element = discipline.primaryDisciplines.get(0);
					Minecraft.getMinecraft().renderEngine.bindTexture(element.getIcon());
					DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset, 185 + iconVerticalPos, 8, 8);
				}
			} else if (mode == DisciplineMode.MULTI_DISCIPLINE_MODE) {
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:disciplines"), 304 + bookshelfOffset, 196 - modeYOffset, 0xFFFFFF);

				for (int i = 0; i < discipline.primaryDisciplines.size(); i++) {
					Element element = discipline.primaryDisciplines.get(i);
					Minecraft.getMinecraft().renderEngine.bindTexture(element.getIcon());
					DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset + ICON_X_SPACING * i, 185 + iconVerticalPos, 8, 8);
				}
			} else if (mode == DisciplineMode.SUB_DISCIPLINE_MODE) {
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:main_discipline"), 304 + bookshelfOffset, 196 - modeYOffset, 0xFFFFFF);
				if (!discipline.primaryDisciplines.isEmpty()) {
					if (Settings.settings.max_multi_disciplines_count == 1) {
						Element element = discipline.primaryDisciplines.get(0);
						Minecraft.getMinecraft().renderEngine.bindTexture(element.getIcon());
						DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset, 185 + iconVerticalPos, 8, 8);
					} else {
						for (int i = 0; i < discipline.primaryDisciplines.size(); i++) {
							Element element = discipline.primaryDisciplines.get(i);
							Minecraft.getMinecraft().renderEngine.bindTexture(element.getIcon());
							DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset + ICON_X_SPACING * i, 185 + iconVerticalPos, 8, 8);
						}
					}
				}

				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(I18n.format("gui.wizardryfates:sub_disciplines"), 304 + bookshelfOffset, 196, 0xFFFFFF);
				for (int i = 0; i < discipline.secondaryDisciplines.size(); i++) {
					Element element = discipline.secondaryDisciplines.get(i);
					Minecraft.getMinecraft().renderEngine.bindTexture(element.getIcon());
					DrawingUtils.drawTexturedRect(ICON_X_POS + bookshelfOffset + ICON_X_SPACING * i, 206, 8, 8);
				}
			}
		}
	}

}
