package com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.config.FWConfig;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.FoodWarsPet;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.config.PetManager;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.main.FoodWars;
import com.hotmail.AdrianSR.BattleRoyale.events.ScoreboardUpdateEvent;
import com.hotmail.AdrianSR.BattleRoyale.main.BattleRoyale;
import com.hotmail.AdrianSR.BattleRoyale.map.battlemap.BattleMap;
import com.hotmail.AdrianSR.BattleRoyale.map.managers.MapsManager;
import com.hotmail.AdrianSR.BattleRoyale.util.scoreboard.ScoreboardBuilder;

/**
 * Represents the class that adds 
 * to the scoreboard the information 
 * lines provided by the battle mode 'FoodWars'.
 * <p>
 * @author AdrianSR
 */
public final class ScoreboardManager implements Listener {
	
	/**
	 * Manager instance.
	 */
	private static ScoreboardManager INSTANCE;
	
	/**
	 * Battle mode instance.
	 */
	private FoodWars mode;

	/**
	 * Construct foodwars scoreboards
	 * manager.
	 * <p>
	 * @param mode
	 */
	public ScoreboardManager(FoodWars mode) {
		if (INSTANCE != null) {
			throw new UnsupportedOperationException("This mananager is already initialized!");
		}
		
		this.mode = mode;
		Bukkit.getPluginManager().registerEvents((INSTANCE = this), BattleRoyale.INSTANCE());
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onUpdate(ScoreboardUpdateEvent event) {
		ScoreboardBuilder builder = event.getBuilder();
		BattleMap             map = MapsManager.BATTLE_MAP;
		int                  mode = FWConfig.HEALTH_DISPLAY_MODE.of(Integer.class); 
		if (!PetManager.PET_ONE.setWorld(map.getWorld()).setUpDone(false, false)
				|| !PetManager.PET_TWO.setWorld(map.getWorld()).setUpDone(false, false)) {
			return;
		}
		
		FoodWarsPet pet_a = PetManager.PET_ONE; 
		int      health_a = pet_a.getHealth();
		int         per_a = pet_a.getHealthPercent();
		FoodWarsPet pet_b = PetManager.PET_TWO;
		int      health_b = pet_b.getHealth();
		int         per_b = pet_b.getHealthPercent();
		
		String ha = ( mode < 1 ? ( per_a + "%" ) : ( health_a + "" ) );
		String hb = ( mode < 1 ? ( per_b + "%" ) : ( health_b + "" ) );
		String  a = FWConfig.HEALTH_TEAM_ONE_DISPLAY_TEXT.toString().replace(FWConfig.PETS_HEALTH_VARIABLE, ha);
		String  b = FWConfig.HEALTH_TEAM_TWO_DISPLAY_TEXT.toString().replace(FWConfig.PETS_HEALTH_VARIABLE, hb);
		
		/* append */
		builder.appendAfterNextSpace(builder.getNextSpace());
		builder.appendAfterNextSpace(b);
		builder.insert(builder.indexOf(b), a);
	}
}