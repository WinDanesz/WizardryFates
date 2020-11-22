package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.integration.FatesASIntegration;
import electroblob.wizardry.constants.Element;
import net.minecraft.util.text.TextFormatting;

public class Utils {

	private Utils() {}

	public static String getElementDisplayName(Element element) {
		if (isAncientElement(element)) {
			return FatesASIntegration.ANCIENT_ELEMENT.toUpperCase();
		} else
			return element.getDisplayName().toUpperCase();
	}

	public static boolean isAncientElement(Element element) {
		return element == Element.MAGIC && FatesASIntegration.enabled();
	}

	public static Element getElementFromName(String element) {
		element = element.toLowerCase();
		if (element.equals(FatesASIntegration.ANCIENT_ELEMENT) && FatesASIntegration.enabled()) {
			return Element.MAGIC;
		}

		// FIXME: apparently this loads before the integration is being initialized
		if (element.equals(FatesASIntegration.ANCIENT_ELEMENT)) {
			return Element.MAGIC;
		}
		return Element.fromName(element);
	}

	public static String getElementWithStyleFormat(Element element) {
		if (isAncientElement(element)) {
			return FatesASIntegration.getFormattingCode() + getElementDisplayName(element) + TextFormatting.RESET.toString();
		}
		return element.getFormattingCode() + getElementDisplayName(element) + TextFormatting.RESET.toString();
	}
}
