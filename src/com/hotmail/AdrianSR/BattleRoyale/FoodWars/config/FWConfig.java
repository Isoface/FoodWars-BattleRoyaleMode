package com.hotmail.AdrianSR.BattleRoyale.FoodWars.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.ChatColor;

import com.hotmail.AdrianSR.BattleRoyale.main.BattleRoyale;
import com.hotmail.AdrianSR.core.util.PrintUtils;
import com.hotmail.AdrianSR.core.util.TextUtils;
import com.hotmail.AdrianSR.core.util.file.UTF8YamlConfiguration;

/**
 * Represents the configuration
 * manager of the Food Wars.
 * <p>
 * @author AdrianSR
 */
public enum FWConfig {

	PETS_HEALTH("ModeSettings.pets-health", 1000),
	USE_REDEPLOY("ModeSettings.use-redeploy", true),
	MAX_PLAYERS_PER_TEAM("ModeSettings.max-players-per-team", 50),
	RESPAWN_SECONDS("ModeSettings.respawn-seconds", 3),
	
	HEALTH_TEAM_ONE_DISPLAY_TEXT("Scoreboard.health-team-one-display-text", "&9&lTEAM A: " + FWConfig.PETS_HEALTH_VARIABLE),
	HEALTH_TEAM_TWO_DISPLAY_TEXT("Scoreboard.health-team-two-display-text", "&c&lTEAM B: " + FWConfig.PETS_HEALTH_VARIABLE),
	HEALTH_DISPLAY_MODE("Scoreboard.health-display-mode", 0),
	
	TITLE_WIN("Title.win-title", "&a&l!Your team WON!"),
	TITLE_GAME_OVER("Title.game-over-title", "&c&lGame Over"),
	;
	
	public static final String   FWCONFIG_FILE_NAME = "FoodWarsConfig.yml";
	public static final String PETS_HEALTH_VARIABLE = "{pet_health}";
	public static       UTF8YamlConfiguration  YAML = null;
	
	/**
	 * Load food wars configuration
	 * from the yaml file.
	 * <p>
	 * @param folder battle mode folder.
	 * @throws IOException if the config could not be loaded.
	 */
	public static void load(File folder) throws IOException {
		/* load configuration */
		File file = new File(folder, FWCONFIG_FILE_NAME);
		if (!file.exists() && !file.createNewFile()) {
			PrintUtils.print(ChatColor.RED, "The config of the FoodWars could not be loaded!", BattleRoyale.INSTANCE());
			return;
		}
		
		YAML = UTF8YamlConfiguration.loadConfiguration(file);
		
		/* set defaults */
		Arrays.stream(FWConfig.values()).filter(item -> !YAML.isSet(item.getPath())).forEach(item -> {
			YAML.set(item.getPath(), item.getDefault()); /* set default and save */
			try {
				YAML.save(file);
			} catch (IOException e) {
				/* ignore */
			}
		});
	}
	
	/**
	 * Config item values.
	 */
	private final String path;
	private final Object  def;
	
	/**
	 * Construct config item.
	 * <p>
	 * @param path the path in the yml.
	 * @param def the default vaule.
	 */
	FWConfig(String path, Object def) {
		this.path = path;
		this.def  = def;
	}
	
	public <T> T of(Class<T> clazz) {
		Object loaded = YAML.get(path);
		if (loaded == null && clazz.isAssignableFrom(def.getClass())) {
			return (T) def;
		}
		return ( clazz.isAssignableFrom(loaded.getClass()) ? (T) loaded : null );
	}
	
	/**
	 * Get config item as {@link Integer}.
	 * <p>
	 * @return the config item value as Integer.
	 */
	public int toInt() {
		return of(Integer.class);
	}
	
	/**
	 * Get config item as {@link Double}.
	 * <p>
	 * @return the config item value as Double.
	 */
	public double toDouble() {
		return of(Double.class);
	}
	
	/**
	 * Get config item as {@link Boolean}.
	 * <p>
	 * @return the config item value as Boolean.
	 */
	public boolean toBoolean() {
		return of(Boolean.class);
	}
	
	@Override
	public String toString() {
		if (def instanceof String) { /* check instance */
			return TextUtils.translateColors(TextUtils.getNotNull(YAML.getString(path, (String) def), (String) def));
		}
		return null;
	}
	
	/**
	 * Get the String path.
	 * <p>
	 * @return The String patch.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Get the default value.
	 * <p>
	 * @return the default value.
	 */
	public Object getDefault() {
		return def;
	}
}