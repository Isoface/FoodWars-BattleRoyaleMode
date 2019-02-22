package com.hotmail.AdrianSR.BattleRoyale.FoodWars.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.FoodWarsPet;
import com.hotmail.AdrianSR.core.util.localization.LocUtils;

public final class PetUtils {

	public static boolean hasEmptyArea(FoodWarsPet pet) {
		return pet == null || hasEmptyArea(pet.getCorner1().toLocation(), pet.getCorner2().toLocation());
	}
	
	public static boolean hasEmptyArea(Location corner1, Location corner2) {
		if (!LocUtils.isValidLocation(corner1) || !LocUtils.isValidLocation(corner2)) {
			return true;
		}

		final List<Block> list = LocUtils.blocksFromTwoPoints(corner1, corner2);
		int       empty_blocks = list.stream().filter(block -> block.isEmpty()).collect(Collectors.toList()).size();
		if (list.size() == empty_blocks) {
			return true;
		}
		return false;
	}
}
