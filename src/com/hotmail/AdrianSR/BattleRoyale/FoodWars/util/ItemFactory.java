package com.hotmail.AdrianSR.BattleRoyale.FoodWars.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hotmail.AdrianSR.core.util.ItemUtils;

public final class ItemFactory {

	public static ItemStack getPetSelector() {
		return ItemUtils
				.addSoulbound(ItemUtils.setName(new ItemStack(Material.BLAZE_ROD), ChatColor.GOLD + "Pet Selector"));
	}

	public static boolean isPetSelector(ItemStack stack) {
		return 
		ItemUtils.isSoulbound(stack) && 
		ItemUtils.extractName(stack, false)
		.equals(getPetSelector().getItemMeta().getDisplayName());
	}

	public static void givePetSelector(Player player) {
		if (player != null && player.isOnline()) {
			player.getInventory().addItem(getPetSelector());
			player.updateInventory();
		}
	}
}
