package com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.location;

public class FWLocUtils {

	public static boolean isValidFWLoc(FWLoc loc, boolean ignore_null_world) {
		if (loc == null) {
			return false;
		}
		
		if (loc.getWorld() == null && !ignore_null_world) {
			return false;
		}

		if (loc.getX() == null || loc.getY() == null || loc.getZ() == null) {
			return false;
		}

		if (loc.getPitch() == null || loc.getYaw() == null) {
			return false;
		}
		return true;
	}
}
