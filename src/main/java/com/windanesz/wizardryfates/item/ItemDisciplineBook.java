package com.windanesz.wizardryfates.item;

import com.windanesz.wizardryfates.FatesGuiHandler;
import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.registry.WizardryFatesTabs;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDisciplineBook extends Item {

	public static final IStoredVariable<Boolean> RECEIVED_FATES_BOOK = IStoredVariable.StoredVariable.ofBoolean("received_fates_book", Persistence.ALWAYS).setSynced();
	public static final String RECEIVED_FATES_BOOK_TAG = "received_fates_book";

	public static void init() {
		WizardData.registerStoredVariables(RECEIVED_FATES_BOOK);
	}

	public ItemDisciplineBook() {
		setCreativeTab(WizardryFatesTabs.WIZARDRYFATES);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (Settings.settings.discipline_scrolls_enabled) {

			// OPEN GUI
			player.openGui(WizardryFates.instance, FatesGuiHandler.BOOK_OF_FATES, world, 0, 0, 0);

		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
		tooltip.add(Wizardry.proxy.translate("item." + getRegistryName() + ".tooltip"));
		if (!Settings.settings.book_of_fates_enabled) {
			tooltip.add(Wizardry.proxy.translate("tooltip." + WizardryFates.MODID + ":disabled_item"));
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
