package com.hotmail.AdrianSR.BattleRoyale.FoodWars.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.NumberConversions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.hotmail.AdrianSR.core.util.TextUtils;
import com.hotmail.AdrianSR.core.util.classes.ReflectionUtils;
import com.hotmail.AdrianSR.core.util.file.YmlUtils;

public class FWLoc {

	/**
	 * Values.
	 */
	private Number      x, y, z = null;
	private Float    pitch, yaw = null;
	private BlockFace direction = null;
	private World         world = null;

	/**
	 * Construct a Loc from a location.
	 * 
	 * @param loc the location.
	 * @param precise if is false the location will be from the location block.
	 */
	public FWLoc(Location loc, boolean precise) {
		if (loc == null) {
			return;
		}
		
		if (precise) {
			world = loc.getWorld();
			x     = loc.getX();
			y     = loc.getY();
			z     = loc.getZ();
			pitch = loc.getPitch();
			yaw   = loc.getYaw();
		} else {
			world = loc.getWorld();
			x     = loc.getBlockX();
			y     = loc.getBlockY();
			z     = loc.getBlockZ();
			pitch = 0F;
			yaw   = 0F;
		}
	}

	/**
	 * Construct a Loc from a location.
	 * 
	 * @param loc the location.
	 */
	public FWLoc(Location loc) {
		this(loc, false);
	}

	/**
	 * Construct a Loc from world name, and coords.
	 * 
	 * @param world the world name.
	 * @param x is the coord X.
	 * @param y is the coord Y.
	 * @param z is the coord Z.
	 */
	public FWLoc(Number x, Number y, Number z) {
		this(x, y, z, 0, 0);
	}

	/**
	 * Construct a Loc from world name, and coords.
	 * 
	 * @param world the world name.
	 * @param x is the coord X.
	 * @param y is the coord Y.
	 * @param z is the coord Z.
	 * @param pitch is the coord pitch.
	 * @param yaw is the coord yaw.
	 */
	public FWLoc(Number x, Number y, Number z, float pitch, float yaw) {
		this(x, y, z, pitch, yaw, null);
	}
	
	/**
	 * Construct a Loc from world name, and coords.
	 * 
	 * @param world the world name.
	 * @param x is the coord X.
	 * @param y is the coord Y.
	 * @param z is the coord Z.
	 * @param pitch is the coord pitch.
	 * @param yaw is the coord yaw.
	 * @param world the location world
	 */
	public FWLoc(Number x, Number y, Number z, float pitch, float yaw, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.world = world;
	}

	/**
	 * Load Loc from config.
	 * 
	 * @param section the saved Loc section.
	 */
	public FWLoc(ConfigurationSection section) {
		// check section.
		assert section != null : "Cannot load a Loc from this section!";

		// load values.
		pitch     = (float) section.getDouble("Pitch");
		yaw       = (float) section.getDouble("Yaw");
		direction = ReflectionUtils.getEnumFromString(BlockFace.class, TextUtils.notNull(section.getString("FaceDirection")));
		
		// load coords.
		for (String prf : new String[] {"X", "Y", "Z"}) {
			// get numb.
			Number numb = section.isDouble(prf) ? section.getDouble(prf) : section.getInt(prf);
			
			// set value.
			switch(prf) {
			case "X":
				x = numb;
				break;
			case "Y":
				y = numb;
				break;
			case "Z":
				z = numb;
				break;
			}
		}
	}

	/**
	 * @return x.
	 */
	public Number getX() {
		return x;
	}

	/**
	 * @return block X.
	 */
	public int getBlockX() {
		return x != null ? x.intValue() : null;
	}

	/**
	 * @return get y.
	 */
	public Number getY() {
		return y != null ? y.doubleValue() : null;
	}

	/**
	 * @return block Y.
	 */
	public int getBlockY() {
		return y != null ? y.intValue() : null;
	}

	/**
	 * @return get z.
	 */
	public Number getZ() {
		return z != null ? z.doubleValue() : null;
	}

	/**
	 * @return block Z.
	 */
	public int getBlockZ() {
		return z != null ? z.intValue() : null;
	}

	/**
	 * @return location pitch.
	 */
	public Float getPitch() {
		return pitch;
	}

	/**
	 * @return location yaw.
	 */
	public Float getYaw() {
		return yaw;
	}

	/**
	 * Get direction.
	 * 
	 * @return the direction.
	 */
	public BlockFace getDirection() {
		return direction;
	}

	public double distance(FWLoc o) {
		return Math.sqrt(distanceSquared(o));
	}

	public double distanceExcludingX(FWLoc o) {
		return Math.sqrt(distanceSquaredExcludingX(o));
	}

	public double distanceExcludingY(FWLoc o) {
		return Math.sqrt(distanceSquaredExcludingY(o));
	}

	public double distanceExcludingZ(FWLoc o) {
		return Math.sqrt(distanceSquaredExcludingZ(o));
	}

	public double distanceSquared(FWLoc o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		}
		return    NumberConversions.square(x.doubleValue() - o.x.doubleValue())
				+ NumberConversions.square(y.doubleValue() - o.y.doubleValue())
				+ NumberConversions.square(z.doubleValue() - o.z.doubleValue());
	}

	public double distanceSquaredExcludingX(FWLoc o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		}
		return    NumberConversions.square(y.doubleValue() - o.y.doubleValue())
				+ NumberConversions.square(z.doubleValue() - o.z.doubleValue());
	}

	public double distanceSquaredExcludingY(FWLoc o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		}
		return    NumberConversions.square(x.doubleValue() - o.x.doubleValue())
				+ NumberConversions.square(z.doubleValue() - o.z.doubleValue());
	}

	public double distanceSquaredExcludingZ(FWLoc o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		}
		return    NumberConversions.square(x.doubleValue() - o.x.doubleValue())
				+ NumberConversions.square(y.doubleValue() - o.y.doubleValue());
	}

	/**
	 * Get a bukkit location from this.
	 * 
	 * @return a bukkit location from this coords.
	 */
	public Location toLocation() {
		if (world == null || x == null || y == null || z == null) {
			return null;
		}
		return new Location(world, x.doubleValue(), y.doubleValue(), z.doubleValue(), yaw, pitch);
	}

	public Vector3d toVector3d() {
		return new Vector3d(x.doubleValue(), y.doubleValue(), z.doubleValue());
	}

	public Vector3i toVector3i() {
		return new Vector3i(getBlockX(), getBlockY(), getBlockZ());
	}
	
	public World getWorld() {
		return world;
	}

	/**
	 * Set world.
	 * 
	 * @param newWorld the world
	 */
	public void setWorld(final World newWorld) {
		this.world = newWorld;
	}

	/**
	 * Set direction.
	 * 
	 * @param face the direction.
	 * @return this.
	 */
	public FWLoc setDirection(final BlockFace face) {
		direction = face;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof FWLoc) {
			FWLoc l = (FWLoc) obj;
			return world.equals(l.world) && this.getBlockX() == l.getBlockX() && this.getBlockY() == l.getBlockY()
					&& this.getBlockZ() == l.getBlockZ();
		} else if (obj instanceof Location) {
			Location l = (Location) obj;
			return world.equals(l.getWorld()) && this.getBlockX() == l.getBlockX()
					&& this.getBlockY() == l.getBlockY() && this.getBlockZ() == l.getBlockZ();
		}
		return false;
	}

	/**
	 * Save this loc.
	 * 
	 * @param section the section to save.
	 * @return the number of changes.
	 */
	public int saveToConfig(ConfigurationSection section) {
		int save = 0;
		save += YmlUtils.setUpdatedIfNotEqual(section, "X", x.doubleValue());
		save += YmlUtils.setUpdatedIfNotEqual(section, "Y", y.doubleValue());
		save += YmlUtils.setUpdatedIfNotEqual(section, "Z", z.doubleValue());
		save += YmlUtils.setUpdatedIfNotEqual(section, "Pitch", (double) pitch);
		save += YmlUtils.setUpdatedIfNotEqual(section, "Yaw", (double) yaw);
		save += (direction != null ? YmlUtils.setDefaultIfNotSet(section, "FaceDirection", direction.name()) : 0);
		return save > 0 ? 1 : 0;
	}

	/**
	 * Save this loc.
	 * 
	 * @param section the section to save.
	 * @return the number of changes.
	 */
	public int saveToConfig(ConfigurationSection section, boolean saveWorld, boolean savePitch, boolean saveYaw) {
		int save = 0;
		save += YmlUtils.setUpdatedIfNotEqual(section, "X", x.doubleValue());
		save += YmlUtils.setUpdatedIfNotEqual(section, "Y", y.doubleValue());
		save += YmlUtils.setUpdatedIfNotEqual(section, "Z", z.doubleValue());
		save += savePitch ? YmlUtils.setUpdatedIfNotEqual(section, "Pitch", (double) pitch) : 0;
		save += saveYaw ? YmlUtils.setUpdatedIfNotEqual(section, "Yaw", (double) yaw) : 0;
		save += (direction != null ? YmlUtils.setDefaultIfNotSet(section, "FaceDirection", direction.name()) : 0);
		return save > 0 ? 1 : 0;
	}

	/**
	 * Clone this.
	 */
	public FWLoc clone() {
		try {
			return (FWLoc) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public String toString() {
		return "World: " + world.getName() + " X:" + x + " Y:" + y + " Z:" + z;
	}
}