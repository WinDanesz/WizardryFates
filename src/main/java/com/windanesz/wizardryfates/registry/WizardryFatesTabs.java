package com.windanesz.wizardryfates.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class WizardryFatesTabs {

	public static final CreativeTabs WIZARDRYFATES = new WizardryFatesTab("wizardryfates");

	private static class WizardryFatesTab extends CreativeTabs {
		public WizardryFatesTab(String label) {
			super(label);
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(WizardryFatesItems.book_of_fates);
		}
	}
}
