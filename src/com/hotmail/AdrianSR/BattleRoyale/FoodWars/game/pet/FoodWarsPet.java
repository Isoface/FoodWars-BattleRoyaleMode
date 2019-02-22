package com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.main.FoodWars;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.PetUtils;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.location.FWLoc;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.location.FWLocUtils;
import com.hotmail.AdrianSR.core.util.localization.LocUtils;

/**
 * The pet for the 
 * Battle Royale teams.
 * <p>
 * @author AdrianSR
 */
public class FoodWarsPet {
	
	private FWLoc corner1;
	private FWLoc corner2;
	private int    health;
	
	/**
	 * Construct pet.
	 */
	public FoodWarsPet() {
		this(null, null);
	}
	
	/**
	 * Construct pet.
	 * <p>
	 * @param corner1 the corner a of the area.
	 * @param corner2 the corner b of the area.
	 */
	public FoodWarsPet(Location corner1, Location corner2) {
		this(corner1, corner2, FoodWars.FOOD_WARS_PETS_HEALTH);
	}
	
	/**
	 * Construct pet.
	 * <p>
	 * @param corner1 the corner a of the area.
	 * @param corner2 the corner b of the area.
	 * @param health the health for the pet.
	 */
	public FoodWarsPet(Location corner1, Location corner2, int health) {
		this.corner1 = new FWLoc(corner1);
		this.corner2 = new FWLoc(corner2);
		this.health  = health;
	}
	
	/**
	 * Construct pet, loading
	 * if from config.
	 * <p>
	 * @param section the {@link ConfigurationSection} to load from.
	 */
	public FoodWarsPet(ConfigurationSection section) {
		if (section.isConfigurationSection("corner1")) {
			corner1 = new FWLoc(section.getConfigurationSection("corner1"));
		}
		
		if (section.isConfigurationSection("corner2")) {
			corner2 = new FWLoc(section.getConfigurationSection("corner2"));
		}
	}

	/**
	 * Copy the corners of the
	 * area from another {@link FoodWarsPet}.
	 * <p>
	 * @param other the other.
	 * @return this.
	 */
	public FoodWarsPet copy(FoodWarsPet other) {
		if (other != null) {
			this.corner1 = other.getCorner1();
			this.corner2 = other.getCorner2();
		}
		return this;
	}
	
	/**
	 * Returns true if the
	 * giving location is between
	 * the two corners of the area.
	 * <p>
	 * @param location to check.
	 */
	public boolean betweenCorners(Location location) {
		if (setUpDone(false, false)) {
			for (Block block : LocUtils.blocksFromTwoPoints(corner1.toLocation(), corner2.toLocation())) {
				if (block != null && block.getLocation().equals(location)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return the corner1
	 */
	public FWLoc getCorner1() {
		return corner1;
	}

	/**
	 * @param corner1 the corner1 to set
	 */
	public FoodWarsPet setCorner1(Location corner1) {
		this.corner1 = new FWLoc(corner1);
		return this;
	}

	/**
	 * @return the corner2
	 */
	public FWLoc getCorner2() {
		return corner2;
	}

	/**
	 * @param corner2 the corner2 to set
	 */
	public FoodWarsPet setCorner2(Location corner2) {
		this.corner2 = new FWLoc(corner2);
		return this;
	}
	
	/**
	 * Sets the world of
	 * the pet area.
	 * <p>
	 * @param world the new world.
	 */
	public FoodWarsPet setWorld(World world) {
		if (corner1 != null) {
			corner1.setWorld(world);
		}
		
		if (corner2 != null) {
			corner2.setWorld(world);
		}
		return this;
	}
	
	/**
	 * @return true if the pet is settted up.
	 */
	public boolean setUpDone(boolean ignore_null_world, boolean check_area) {
		if (!FWLocUtils.isValidFWLoc(corner1, ignore_null_world) || !FWLocUtils.isValidFWLoc(corner2, ignore_null_world)) {
			return false;
		}
		
		if (corner1.getWorld() == null || corner2.getWorld() == null) {
			if (!ignore_null_world) {
				return false;
			}
		}
		
		if (check_area) {
			if (PetUtils.hasEmptyArea(corner1.toLocation(), corner2.toLocation())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Detects the corner to set
	 * from a {@link PlayerInteractEvent}.
	 * <p>
	 * @param event the {@link PlayerInteractEvent}.
	 */
	public FoodWarsPet setCorner(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action.name().contains("LEFT")) {
			setCorner1(event.getClickedBlock().getLocation());
		} else {
			setCorner2(event.getClickedBlock().getLocation());
		}
		return this;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Calculate pet health
	 * percentage.
	 * <p>
	 * @return health percentage.
	 */
	public int getHealthPercent() {
		return (( health * 100 ) / FoodWars.FOOD_WARS_PETS_HEALTH );
	}

	/**
	 * @param health the health to set
	 */
	public FoodWarsPet setHealth(int health) {
		this.health = health;
		return this;
	}
	
	/**
	 * Save corners of the area
	 * to a {@link ConfigurationSection}.
	 */
	public FoodWarsPet save(ConfigurationSection section) {
		if (setUpDone(false, false)) {
			corner1.saveToConfig(section.createSection("corner1"));
			corner2.saveToConfig(section.createSection("corner2"));
		}
		return this;
	}
}