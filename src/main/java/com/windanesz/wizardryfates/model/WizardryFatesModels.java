package com.windanesz.wizardryfates.model;

import com.windanesz.wizardryfates.registry.WizardryFatesItems;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class WizardryFatesModels {
	private WizardryFatesModels() { // no instances
	}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {
		// ====================== Scrolls ======================

		registerItemModel(WizardryFatesItems.book_of_fates);

		registerItemModel(WizardryFatesItems.scroll_earth);
		registerItemModel(WizardryFatesItems.scroll_necromancy);
		registerItemModel(WizardryFatesItems.scroll_healing);
		registerItemModel(WizardryFatesItems.scroll_lightning);
		registerItemModel(WizardryFatesItems.scroll_ice);
		registerItemModel(WizardryFatesItems.scroll_fire);
		registerItemModel(WizardryFatesItems.scroll_sorcery);
		registerItemModel(WizardryFatesItems.scroll_ancient);

		registerItemModel(WizardryFatesItems.lesser_scroll_earth);
		registerItemModel(WizardryFatesItems.lesser_scroll_necromancy);
		registerItemModel(WizardryFatesItems.lesser_scroll_healing);
		registerItemModel(WizardryFatesItems.lesser_scroll_lightning);
		registerItemModel(WizardryFatesItems.lesser_scroll_ice);
		registerItemModel(WizardryFatesItems.lesser_scroll_fire);
		registerItemModel(WizardryFatesItems.lesser_scroll_sorcery);
		registerItemModel(WizardryFatesItems.lesser_scroll_ancient);

		registerItemModel(WizardryFatesItems.scroll_oblivion);
	}

	// below registry methods are courtesy of EB
	private static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static <T extends Item & IMultiTexturedItem> void registerMultiTexturedModel(T item) {

		if (item.getHasSubtypes()) {
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab(), items);
			for (ItemStack stack : items) {
				ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
						new ModelResourceLocation(item.getModelName(stack), "inventory"));
			}
		}
	}

	private static void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}

