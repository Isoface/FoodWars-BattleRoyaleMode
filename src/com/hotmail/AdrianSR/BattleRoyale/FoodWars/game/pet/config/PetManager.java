package com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.hotmail.AdrianSR.BattleRoyale.FoodWars.game.pet.FoodWarsPet;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.main.FoodWars;
import com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.ItemFactory;
import com.hotmail.AdrianSR.BattleRoyale.main.BattleRoyale;
import com.hotmail.AdrianSR.BattleRoyale.map.battlemap.BattleMap;
import com.hotmail.AdrianSR.BattleRoyale.map.managers.MapsManager;
import com.hotmail.AdrianSR.core.util.Matcher;
import com.hotmail.AdrianSR.core.util.PrintUtils;

/**
 * The battle modes
 * pet manager.
 * <p>
 * @author AdrianSR
 */
public final class PetManager implements Listener {
	
	/**
	 * Manager instance.
	 */
	private static PetManager INSTANCE;
	
	/**
	 * Command matcher.
	 */
	private static final Matcher CORCHETES = new Matcher("[]");
	
	/**
	 * Selection mode.
	 */
	private static final Map<UUID, SelectionMode> MODES = new HashMap<UUID, SelectionMode>();
	public static enum SelectionMode {
		
		PET_ONE,
		PET_TWO;
		
		/**
		 * @return true if the player have selected this pet.
		 */
		public boolean isSelectedBy(Player player) {
			return this.equals(MODES.get(player.getUniqueId()));
		}
		
		/**
		 * Sets the selected pet
		 * by the player.
		 * <p>
		 * @param player to set.
		 */
		public void set(Player player) {
			MODES.put(player.getUniqueId(), this);
		}
		
		/**
		 * @return true if the player have selected some pet.
		 */
		public static boolean haveSelected(Player player) {
			return MODES.get(player.getUniqueId()) != null;
		}
	}
	
	/**
	 * Game pets.
	 */
	public static final FoodWarsPet PET_ONE = new FoodWarsPet();
	public static final FoodWarsPet PET_TWO = new FoodWarsPet();
	
	private FoodWars mode;
	
	/**
	 * Construct manager.
	 * <p>
	 * @param mode the battle mode.
	 */
	public PetManager(FoodWars mode) {
		if (INSTANCE != null) {
			throw new UnsupportedOperationException("This mananager is already initialized!");
		}
		
		this.mode = mode;
		Bukkit.getPluginManager().registerEvents((INSTANCE = this), BattleRoyale.INSTANCE());
	}
	
	/**
	 * Returns true if the pets
	 * setup are done.
	 */
	public boolean canStart() {
		loadPets(); /* load pets after map loading */
		
		/* check pets */
		BattleMap map = MapsManager.BATTLE_MAP;
		if (!PetManager.PET_ONE.setWorld(map.getWorld()).setUpDone(false, false)
				|| !PetManager.PET_TWO.setWorld(map.getWorld()).setUpDone(false, false)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Load pets from config.
	 */
	private void loadPets() {
		File file = new File(mode.getDataFolder(), "FoodWarsPets.yml");
		if (!file.exists()) {
			return;
		}
		
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if (yml == null) {
			return;
		}
		
		ConfigurationSection pets_sc = yml.getConfigurationSection("PetsConfiguration");
		if (pets_sc == null) {
			return;
		}
				
		for (int x = 0; x < 2; x++) {
			String           pet_sc_name = ( x == 0 ? "pet-one" : "pet-two" );
			ConfigurationSection  pet_sc = pets_sc.getConfigurationSection(pet_sc_name);
			if (pet_sc == null) {
				continue;
			}
			
			FoodWarsPet pet = new FoodWarsPet(pet_sc);
			if (pet.setUpDone(true, false)) {
				if (x == 0) {
					PET_ONE.copy(pet);
				} else {
					PET_TWO.copy(pet);
				}
				BattleRoyale.INSTANCE().getLogger().fine("The pet " + ( x == 0 ? "one" : "two" ) + " has been loaded from config successfully!");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCornerSet(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block   block = event.getClickedBlock();
		if (block == null || !ItemFactory.isPetSelector(event.getItem())) {
			return;
		}
		
		if (MapsManager.BATTLE_MAP == null) {
			return;
		}
		
		if (!player.isOp()) {
			event.setCancelled(true);
			return;
		}
		
		if (!SelectionMode.haveSelected(player)) {
			player.sendMessage(ChatColor.RED + "You have not selected the pet to set!");
			player.sendMessage(ChatColor.RED + "use /setpet [one/two], to select the pet!");
			event.setCancelled(true);
			return;
		}
		
		/* set and info */
		String message = "The corner " + ( event.getAction().name().contains("LEFT") ? 1 : 2 );
		(SelectionMode.PET_ONE.isSelectedBy(player) ? PET_ONE : PET_TWO).setCorner(event);
		if (SelectionMode.PET_ONE.isSelectedBy(player)) {
			message += " of the pet one ";
		} else {
			message += " of the pet two ";
		}
		player.sendMessage(ChatColor.GREEN + ( message += "has been set!" ));
		event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player  player = event.getPlayer();
		String message = event.getMessage();
		String[]  args = message.split(" ");
		if (!(args.length > 0)) {
			return;
		}
		
		if (MapsManager.BATTLE_MAP == null) {
			return;
		}
		
		if (args[0].equalsIgnoreCase("/setpet")) {
			if (!player.isOp()) {
				player.sendMessage(ChatColor.RED + "You donnot have permission to use this command!");
				return;
			}
			
			String argument = CORCHETES.replace(args[1], "");
			if (argument.equalsIgnoreCase("one") || argument.equalsIgnoreCase("1")) {
				SelectionMode.PET_ONE.set(player);
			} else if (argument.equalsIgnoreCase("two") || argument.equalsIgnoreCase("2")) {
				SelectionMode.PET_TWO.set(player);
			} else if (argument.equalsIgnoreCase("setter") || argument.equalsIgnoreCase("item")) {
				ItemFactory.givePetSelector(player);
				player.sendMessage(ChatColor.GREEN + "Puff!!!!");
				event.setCancelled(true);
				return;
			} else {
				player.sendMessage(ChatColor.RED + "The pet '" + argument + "' is unknown!");
				event.setCancelled(true);
				return;
			}
			player.sendMessage(ChatColor.GREEN + "The pet '" + argument + "' has been selected!");
			event.setCancelled(true);
		} else if (args[0].equalsIgnoreCase("/savepet")) { /* save pet */
			if (!player.isOp()) {
				player.sendMessage(ChatColor.RED + "You donnot have permission to use this command!");
				return;
			}
			
			if (!SelectionMode.haveSelected(player)) {
				player.sendMessage(ChatColor.RED + "You have not selected the pet to set!");
				player.sendMessage(ChatColor.RED + "use /setpet [one/two], to select the pet!");
				event.setCancelled(true);
				return;
			}
			
			FoodWarsPet pet = (SelectionMode.PET_ONE.isSelectedBy(player) ? PET_ONE : PET_TWO);
			if (!pet.setUpDone(false, true)) {
				player.sendMessage(ChatColor.RED
						+ "You have not selected the two corners of the selected pet or the area between the selected corners is empty!");
				event.setCancelled(true);
				return;
			}
			
			savePet(pet, MODES.get(player.getUniqueId()));
			player.sendMessage(ChatColor.GREEN + "Pet saved!");
			event.setCancelled(true);
		}
	}
	
	/**
	 * Save pet to config.
	 * <p>
	 * @param pet pet to save.
	 * @param slmode the selection mode.
	 */
	private void savePet(FoodWarsPet pet, SelectionMode slmode) {
		File file = new File(mode.getDataFolder(), "FoodWarsPets.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				PrintUtils.print(ChatColor.RED, "The pet could not be saved: ", BattleRoyale.INSTANCE());
				e.printStackTrace();
				return;
			}
		}
		
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
		if (yml == null) {
			return;
		}
		
		String           pet_sc_name = ( slmode == SelectionMode.PET_ONE ? "pet-one" : "pet-two" );
		ConfigurationSection pets_sc = ( yml.isConfigurationSection("PetsConfiguration") ? yml.getConfigurationSection("PetsConfiguration") : yml.createSection("PetsConfiguration") );
		ConfigurationSection pet_sc  = ( pets_sc.isConfigurationSection(pet_sc_name) ? pets_sc.getConfigurationSection(pet_sc_name) : pets_sc.createSection(pet_sc_name) );
		
		try {
			pet.save(pet_sc);
			yml.save(file);
		} catch (IOException e) {
			PrintUtils.print(ChatColor.RED, "The pet could not be saved: ", BattleRoyale.INSTANCE());
			e.printStackTrace();
		}
	}
}