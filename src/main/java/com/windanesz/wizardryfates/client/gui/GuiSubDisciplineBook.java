package com.windanesz.wizardryfates.client.gui;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.Discipline;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.handler.Utils;
import com.windanesz.wizardryfates.integration.FatesASIntegration;
import com.windanesz.wizardryfates.packet.FatesPacketHandler;
import com.windanesz.wizardryfates.packet.PacketDisciplineButtonConfirm;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.registry.WizardrySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiSubDisciplineBook extends GuiScreen {

	private int xSize, ySize;
	public List<String> elementStrings;
	private List<Element> selection = new ArrayList<>();
	private static int CONFIRM = 0;
	private static int DESELECT_ALL = -1;

	public GuiSubDisciplineBook() {
		super();
		xSize = 512;
		ySize = 256;

		List<String> elements = new ArrayList<>(Arrays.asList(Settings.settings.element_selection_list));
		if (!FatesASIntegration.enabled()) {
			elements.remove("ANCIENT");
		}
		this.elementStrings = elements;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {

		int xPos = this.width / 2 - (xSize - 224) / 2;
		int yPos = this.height / 2 - 180 / 2;

		GlStateManager.color(1, 1, 1, 1); // Just in case

		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(WizardryFates.MODID, "textures/gui/book_of_fates.png"));
		DrawingUtils.drawTexturedRect(xPos, yPos, 0, 0, xSize, ySize, xSize, 256);

		super.drawScreen(par1, par2, par3);

		this.fontRenderer.drawString("Choose Your Sub-Discipline", xPos + 17, yPos + 17, 0);
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		int xPos = (this.width / 2 - (xSize - 224) / 2);
		int yPos = (this.height / 2 - 180 / 2) + 50;

		this.buttonList.add(new GuiButton(CONFIRM, this.width / 2 - 50, yPos + 132, 100, 20, I18n.format("gui.wizardryfates:confirm")));
		this.buttonList.add(new GuiButton(DESELECT_ALL, this.width / 2 - 50, yPos + 152, 100, 20, I18n.format("gui.wizardryfates:deselect_all")));

		Discipline disciplines = DisciplineUtils.getPlayerDisciplines(mc.player);
		selection.addAll(disciplines.secondaryDisciplines);

		for (int i = 0; i < elementStrings.size(); i++) {
			try {
				String element = elementStrings.get(i);
				Element element1 = Utils.getElementFromName(element);
				String buttonText = selection.contains(element1) ? "[x] " + Utils.getElementWithStyleFormat(element1) : Utils.getElementWithStyleFormat(element1);

				if (i < elementStrings.size() / 2) {
					GuiButton button = new GuiButton(i + 1, xPos + 27, yPos + 30 * i, 100, 20, buttonText);
					this.buttonList.add(button);
				} else {
					GuiButton button = new GuiButton(i + 1, xPos + 160, yPos + 30 * i - (((elementStrings.size() / 2)) * 30), 100, 20, buttonText);
					this.buttonList.add(button);
				}

			} catch (Exception e) {
				// A bit more user-friendly than crashing the game
				// invalid element string
				Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("No such element with unlocalised name: " + elementStrings.get(i) + ". Verify your 'Possible Elements List' in the Wizardry Fates settings."));
				Minecraft.getMinecraft().player.closeScreen();
			}
		}

		updateButtons();
		this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(WizardrySounds.MISC_BOOK_OPEN, 1));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {

		if (button.id == CONFIRM && !selection.isEmpty()) {
			IMessage msg = new PacketDisciplineButtonConfirm.Message(selection, true);
			FatesPacketHandler.net.sendToServer(msg);
			Minecraft.getMinecraft().player.closeScreen();
		}

		if (button.id == DESELECT_ALL) {
			selection.clear();
		}

		if (button.id > 0) {
			int elementId = button.id - 1;
			if (elementId >= 0 && elementId < elementStrings.size()) {
				Element clickedElement = Utils.getElementFromName(elementStrings.get(elementId));
				if (selection.contains(clickedElement)) {
					selection.remove(clickedElement);
				} else {
					if (selection.size() < Settings.settings.max_sub_discipline_count) {
						selection.add(clickedElement);
					}
				}
			}
		}

		updateButtons();
		super.actionPerformed(button);
	}

	private void updateButtons() {
		for (GuiButton button : buttonList) {
			if(button.id > 0) {
				int elementId = button.id - 1;
				if (elementId >= 0 && elementId < elementStrings.size()) {
					Element element = Utils.getElementFromName(elementStrings.get(elementId));
					button.displayString = selection.contains(element) ? "[x] " + Utils.getElementWithStyleFormat(element) : Utils.getElementWithStyleFormat(element);
				}
			}
		}
		buttonList.get(0).enabled = !selection.isEmpty() && selection.size() <= Settings.settings.max_sub_discipline_count;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Wizardry.settings.booksPauseGame;
	}

}
