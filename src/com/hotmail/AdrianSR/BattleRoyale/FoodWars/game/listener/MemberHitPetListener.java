package com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.config.FWConfig;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.FoodWarsPet;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.config.PetManager;
import com.hotmail.AdrianSR.BattleRoyale.config.Lang;
import com.hotmail.AdrianSR.BattleRoyale.database.StatType;
import com.hotmail.AdrianSR.BattleRoyale.game.BRPlayer;
import com.hotmail.AdrianSR.BattleRoyale.game.BRTeam;
import com.hotmail.AdrianSR.BattleRoyale.game.Member;
import com.hotmail.AdrianSR.BattleRoyale.game.Team;
import com.hotmail.AdrianSR.BattleRoyale.game.managers.GameManager;
import com.hotmail.AdrianSR.BattleRoyale.game.tasks.RespawnAndPositionSender;
import com.hotmail.AdrianSR.BattleRoyale.main.BattleRoyale;
import com.hotmail.AdrianSR.BattleRoyale.util.GameUtils;
import com.hotmail.AdrianSR.core.titles.Titles;
import com.hotmail.AdrianSR.core.util.localization.LocUtils;

/**
 * Represents a class that 
 * allow players to destroy
 * the pet of the enemy team.
 * <p>
 * @author AdrianSR.
 */
public final class MemberHitPetListener implements Listener {
	
	/**
	 * Costruct listener.
	 * <p>
	 * @param plugin the BattleRoyale plugin instance.
	 */
	public MemberHitPetListener(final BattleRoyale plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onHit(final BlockBreakEvent event) {
		if (GameManager.isNotRunning()) {
			return;
		}
		
		Block     block = event.getBlock();
		FoodWarsPet pet = null;
		Team       team = null;
		int     team_id = 0;
		if (PetManager.PET_ONE.betweenCorners(block.getLocation())) {
			pet = PetManager.PET_ONE;
		} else if (PetManager.PET_TWO.betweenCorners(block.getLocation())) {
			pet     = PetManager.PET_TWO;
			team_id = 1;
		} else {
			return;
		}
		
		if (BRTeam.getTeams().size() < ( team_id + 1 )) {
			team = new BRTeam();
		} else {
			team = BRTeam.getTeams().get(team_id);
		}
		
		Player player = event.getPlayer();
		Member member = BRPlayer.getBRPlayer(player);
		if (!member.hasTeam() || !member.isLiving()) {
			event.setCancelled(true);
			return;
		}
		
		if (member.getTeam().equals(team)) { /* when breaking the pet of the same team */
			event.setCancelled(true);
			return;
		}
		
		Team winner = member.getTeam();
		int  health = Math.max( ( pet.getHealth() - 1 ) , 0);
		if (health == 0) { 
			GameManager.stopGame(); /* stop game */
			
			/* titles */
			for (Player online : Bukkit.getOnlinePlayers()) {
				BRPlayer  br = BRPlayer.getBRPlayer(online);
				String title = "";
				if (br.hasTeam() && br.getTeam().equals(winner)) {
					title = FWConfig.TITLE_WIN.toString();
				} else {
					title = FWConfig.TITLE_GAME_OVER.toString();
				}
				
				if (br.isLiving()) {
					new RespawnAndPositionSender(br, br.getPlayer().getLocation(), 
							title,
							Lang.POSITIONS_SUBTITLE.toString()
					).runTaskLater(BattleRoyale.INSTANCE(), 2L);
				} else {
					Titles.sendTitleMessages(online, title, Lang.POSITIONS_SUBTITLE.toString(), 8, 100, 8);
				}
				
				/* won stat to winning team member */
				if (br.hasTeam() && br.getTeam().equals(winner)) {
					GameUtils.addStat(br, StatType.WON_GAMES, true);
				}
			}
		}
		
		pet.setHealth(health);
		destroyPet(pet);
		event.setCancelled(true);
	}
	
	/**
	 * Destroy blocks
	 * of a pet.
	 * <p>
	 * @param pet the pet to destroy.
	 */
	private void destroyPet(FoodWarsPet pet) {
		for (Block block : LocUtils.blocksFromTwoPoints(pet.getCorner1().toLocation(), pet.getCorner2().toLocation())) {
			block.setType(Material.AIR);
			block.getState().update();
		}
	}
}