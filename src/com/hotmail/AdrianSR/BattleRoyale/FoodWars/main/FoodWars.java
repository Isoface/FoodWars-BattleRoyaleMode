package com.hotmail.AdrianSR.BattleRoyale.FoodWars.main;

import java.io.IOException;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.config.FWConfig;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.listener.MemberHitPetListener;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.config.PetManager;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.scoreboard.ScoreboardManager;
import com.hotmail.AdrianSR.BattleRoyale.game.mode.complex.ComplexBattleMode;
import com.hotmail.AdrianSR.BattleRoyale.main.BattleRoyale;

/**
 * Represents the Battle Royale
 * Game Mode 'Food Wars'.
 * <p>
 * @author AdrianSR
 */
public final class FoodWars extends ComplexBattleMode {
	
	public static final double 		   FOOD_WARS_START_HEALTH = 20.0D;
	public static final double			 FOOD_WARS_MAX_HEALTH = 20.0D;
	public static final int               FOOD_WARS_MAX_KILLS = 0;
	public static       boolean            FOOD_WARS_REDEPLOY = true;
	public static final int               FOOD_WARS_MAX_TEAMS = 2;
	public static       int   FOOD_WARS_MAX_PLAYERS_PER_TEAMS = 50;
	public static final boolean                FOOD_WARS_SOLO = false;
	public static final boolean       FOOD_WARS_RESUSCITATION = false;
	public static final int   FOOD_WARS_RESUSCITATION_SECONDS = 0;
	public static final double FOOD_WARS_RESUSCITATION_HEALTH = 0;
	public static final boolean             FOOD_WARS_RESPAWN = true;
	public static       int         FOOD_WARS_RESPAWN_SECONDS = 3;
	public static       int             FOOD_WARS_PETS_HEALTH = 1000;
	
	private PetManager          pet_manager;
	private ScoreboardManager board_manager;
	
	@Override
	public void onInitialize() {
		/* load config */
		checkDataFolder();
		
		try {
			FWConfig.load(getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FOOD_WARS_PETS_HEALTH           = Math.max(0, FWConfig.PETS_HEALTH.of(Integer.class));
		FOOD_WARS_REDEPLOY              = FWConfig.USE_REDEPLOY.of(Boolean.class);
		FOOD_WARS_MAX_PLAYERS_PER_TEAMS = Math.max(2, FWConfig.MAX_PLAYERS_PER_TEAM.of(Integer.class));
		FOOD_WARS_RESPAWN_SECONDS       = Math.max(0, FWConfig.RESPAWN_SECONDS.of(Integer.class));
		
		/* init managament */
		this.board_manager = new ScoreboardManager(this);
		this.pet_manager   = new PetManager(this);
		
		/* init listener */
		new MemberHitPetListener((BattleRoyale) BattleRoyale.INSTANCE());
	}

	@Override
	public boolean canStart(boolean battlemap_loaded) { /* check game can start */
		if (!battlemap_loaded) {
			return true;
		}
		
		if (!pet_manager.canStart()) {
			return false;
		}
		return true;
	}
	
	@Override
	public double getStartHealth() {
		return FOOD_WARS_START_HEALTH;
	}

	@Override
	public double getMaxHealth() {
		return FOOD_WARS_MAX_HEALTH;
	}

	@Override
	public int getMaxKills() {
		return FOOD_WARS_MAX_KILLS;
	}

	@Override
	public boolean isRedeployOn() {
		return FOOD_WARS_REDEPLOY;
	}

	@Override
	public int getMaxTeams() {
		return FOOD_WARS_MAX_TEAMS;
	}

	@Override
	public int getMaxPlayersPerTeam() {
		return FOOD_WARS_MAX_PLAYERS_PER_TEAMS;
	}

	@Override
	public boolean isSolo() {
		return FOOD_WARS_SOLO;
	}

	@Override
	public boolean isResuscitationOn() {
		return FOOD_WARS_RESUSCITATION;
	}

	@Override
	public int getResuscitationSeconds() {
		return FOOD_WARS_RESUSCITATION_SECONDS;
	}

	@Override
	public double getHealthAfterResuscitation() {
		return FOOD_WARS_RESUSCITATION_HEALTH;
	}

	@Override
	public boolean isRespawnOn() {
		return FOOD_WARS_RESPAWN;
	}

	@Override
	public int getRespawnSeconds() {
		return FOOD_WARS_RESPAWN_SECONDS;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}