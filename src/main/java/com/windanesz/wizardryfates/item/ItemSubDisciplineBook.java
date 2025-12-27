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
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSubDisciplineBook extends Item {

	public ItemSubDisciplineBook() {
		setCreativeTab(WizardryFatesTabs.WIZARDRYFATES);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (Settings.settings.book_of_fates_enabled) {

			// OPEN GUI
			player.openGui(WizardryFates.instance, FatesGuiHandler.BOOK_OF_SUB_DISCIPLINES, world, 0, 0, 0);

		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
		tooltip.add(I18n.format("item." + getRegistryName() + ".tooltip"));
		if (!Settings.settings.book_of_fates_enabled) {
			tooltip.add(I18n.format("tooltip." + WizardryFates.MODID + ":disabled_item"));
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
