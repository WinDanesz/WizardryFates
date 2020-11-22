package com.windanesz.wizardryfates.item;

import com.windanesz.wizardryfates.Settings;
import com.windanesz.wizardryfates.WizardryFates;
import com.windanesz.wizardryfates.handler.DisciplineUtils;
import com.windanesz.wizardryfates.registry.WizardryFatesTabs;
import electroblob.wizardry.Wizardry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFatesOblivionScroll extends Item {

	public ItemFatesOblivionScroll() {
		setCreativeTab(WizardryFatesTabs.WIZARDRYFATES);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (Settings.settings.oblivion_scroll_enabled) {
			if (DisciplineUtils.purgeDisciplines(player)) {
				if (!player.world.isRemote) {
					player.sendMessage(new TextComponentTranslation("Suddenly you feel that you no longer have an affinity for any element."));
				}
				stack.shrink(1);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		} else {
			if (!player.world.isRemote) {
				player.sendMessage(new TextComponentTranslation("This item is disabled in the configuration."));
			}
		}

		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
		tooltip.add(Wizardry.proxy.translate("item." + getRegistryName() + ".tooltip"));
		if (!Settings.settings.oblivion_scroll_enabled) {
			tooltip.add(Wizardry.proxy.translate("tooltip." + WizardryFates.MODID + ":disabled_item"));
		}
	}
}
