package gameworld;

import gameworld.Game.Direction;
import gameworld.entity.Armour;
import gameworld.entity.Container;
import gameworld.entity.Entity;
import gameworld.entity.Gold;
import gameworld.entity.Item;
import gameworld.entity.Key;
import gameworld.entity.LootBag;
import gameworld.entity.Weapon;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.Serializable;


/**
 * Represents a user controlled player within the game
 * @author Jasen
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 1L;

	private static String startingLocation = "Central Hub";

	private Game game;
	private String name;
	private int score;
	private static final int DEFAULT_INV_SIZE = 8;
	private  Item[] inventory;

	private static final int DEFAULT_HEALTH = 100;
	private int maxHealth;
	private int health;
	private boolean isDead;

	private Location location;
	private Point position;
	private Direction facing = Game.Direction.NORTH;

	private Direction direction = Game.Direction.NORTH;
	private Animation animation;

	private Tile standingOn;

	private boolean attacking;

	private Weapon weapon;
	private Armour armour;

	public Player(String name, Game game) {
		this.game = game;
		score = 0;
		//set user name
		this.name = name;
		//create inventory
		inventory = new Item[DEFAULT_INV_SIZE];
		//set location and add player to location
		location = game.getLocation(startingLocation);
		location.addPlayer(this);
		//players position
		position = new Point(location.width()/2, location.height()/2);
		//set the tile's player and the players tile
		standingOn = location.getTileAt(position);
		standingOn.setPlayer(this);
		//give the player animations
		animation = new Animation(this);
		// make the player alive
		maxHealth = DEFAULT_HEALTH;
		health = DEFAULT_HEALTH;
		isDead = false;
		setMaxHealth(health);
		// set default gear
		armour = new Armour("Robe Armour", "Robe Armour", null, null, Armour.ArmourType.Robe);
		weapon = new Weapon("Shank", "A Shank", null, null, Weapon.WeaponType.Shank);
		inventory[0] = new Gold("Gold", "Gold", null, null, 5);
		inventory[1] = new Key("Key","A Key",null,null);
	}

	/**
	 * Used to interact with containers.
	 * Will attempt to unlock the container if it is locked
	 * @return container that the player want to interact with
	 */
	protected Container performAction() {
		Tile tile = this.getTile(facing);
		if(tile == null){return null;}
		if(tile.containedEntity() == null){return null;}
		if(tile.containedEntity() instanceof Container) {
			Container con = (Container) tile.containedEntity();
			// if container is locked attempt to unlock it
			if(con.isLocked()){
				for(int i = 0; i < inventory.length; i++) {
					// if there is a key in the players inventory
					if(inventory[i] instanceof Key) {
						//unlock the container
						inventory[i].interact(this);
						//then remove the key from the players inventory
						inventory[i] = null;
						break;
					}
				}
			}
			// if the player didn't have a key to unlock it return null
			if(con.isLocked()) {return null;}
			return con;
		}
		return null;
	}
	
	/**
	 * Use the item in the players inventory at the specified index
	 * @param index of item in the inventory
	 */
	public void useItem(Item item) {
		if(item == null){return;}
		item.interact(this);
	}

	/**
	 * add an item to the players inventory. This will add the item into the players first available
	 * slot in the inventory
	 * @param item to add
	 * @return true if the item was successfully added
	 */
	public boolean addItem(Item item) {
		if(inventoryFull()){return false;}
		if(item instanceof Gold){
			Gold gold = (Gold) item;
			for(Item i : inventory) {
				if(i instanceof Gold) {
					Gold g = (Gold) i;
					g.setAmount(g.getAmount()+gold.getAmount());
					score += gold.getAmount();
					return true;
				}
			}
			score += gold.getAmount();
		}
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null) {
				// add the item to the players inventory
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes item form inventory at specified index
	 * @param index - index of item to remove
	 */
	public void removeItem(int index){
		inventory[index] = null;
	}

	/**
	 * Swap the items at the given indices in the inventory
	 * @param first - index of first item
	 * @param second - index of second item
	 */
	public void swapItems(int first, int second) {
		if(first >= inventory.length || first < 0 || second >= inventory.length || second < 0) {
			return;
		}
		Item item = inventory[first];
		inventory[first] = inventory[second];
		inventory[second] = item;
	}


	/**
	 * Make this player attack the player in the tile in front of them
	 * @return true if the attack was successful
	 */
	protected boolean attack() {
		if(weapon == null){return false;}
		Tile tile = getTile(facing);
		// nothing in front of the player
		if(tile == null){return false;}
		// if there is no player in front of the player return false
		if(tile.getPlayer() == null){return true;}
		Player opponent = tile.getPlayer();
		//calculate the amount of damage to deal
		int damage = 0;
		if(weapon != null && armour != null) {
			damage = weapon.getDamage()-opponent.getArmour().getArmourRating();
		}
		else if(weapon != null && armour == null) {
			damage = weapon.getDamage();
		}
		opponent.setHealth(opponent.getHealth()-damage);
		if(opponent.getHealth() <= 0){
			score++;
			opponent.respawn();
		}
		return true;
	}

	/**
	 * Drops the players inventory on the ground and then
	 * respawns the player in the specified starting location.
	 * 
	 */
	private void respawn() {
		//drop inventory
		Container loot = new LootBag("Loot Bag", "Player "+name+"'s items", position, location, inventory);
		loot.storeItem(weapon);
		loot.storeItem(armour);
		loot.storeItem(new Key("Key","Key", null, null));
		//loot.storeItem(new Gold("Gold", null, null, null, 4));
		weapon = new Weapon("Shank", "A Shank", null, null, Weapon.WeaponType.Shank);
		armour = new Armour("Robe Armour", "Robe Armour", null, null, Armour.ArmourType.Robe);
		standingOn.setEntitiy(loot);
		inventory = new Item[DEFAULT_INV_SIZE];
		//respawn
		standingOn.setPlayer(null);
		location.removePlayer(this);
		location = game.getLocation(startingLocation);
		location.addPlayer(this);
		position = new Point(location.width()/2, location.height()/2);
		health = DEFAULT_HEALTH;
		standingOn = location.getTileAt(position);
		standingOn.setPlayer(this);
		isDead = false;
	}

	/**
	 * Removes an item from the players inventory and places it on the ground.
	 * Places the item on the tile the player is standing on
	 *
	 * @param index of the item to drop
	 * @return true if the item was successfully removed
	 */
	protected boolean dropFromInv(int index) {
		// get the item
		Item item = inventory[index];
		if(item == null){return false;}
		// remove the item from the inventory
		inventory[index] = null;
		// place the item on the ground
		Container loot = new LootBag(item.getName(),item.getDescription(),position, location,new Item[]{item});
		Tile tile = location.getTileAt(position);
		//fail if there is already an item on the ground
		if(tile.containedEntity() != null) {return false;}
		tile.setEntity(loot);
		//update the item data
		return true;
	}


	/**
	 * Player attempts to pick-up the item on the tile in front of them
	 * @return true if the item was successfully added
	 */
	protected boolean pickupItem() {
		// if the inventory is full can't pick up item
		if(inventoryFull()){return false;}
		Tile tile = getTile(facing);
		Entity ent = tile.containedEntity();
		//nothing in front
		if(ent == null){return false;}
		// if there isn't an item in front of the player don't pick it up
		if(!(ent instanceof Item)) {return false;}
		Item item = (Item) ent;
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null) {
				// remove the item from the tile
				tile.setEntity(null);
				// add the item to the players inventory
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * check the players inventory for a free space
	 *
	 * @return true if the players inventory is full
	 */
	private boolean inventoryFull() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null){return false;}
		}
		return true;
	}


	/**
	 * Get the tile that is in a direction from the player
	 *
	 * @param dir - direction to get the tile from
	 * @return the tile in the direction
	 */
	public Tile getTile(Game.Direction dir) {
		Tile[][] array = location.getTiles();
		switch(dir) {
		case NORTH:
			if(position.y-1 >= 0){return array[position.y-1][position.x];}
			break;
		case EAST:
			if(position.x+1 < location.width()){return array[position.y][position.x+1];}
			break;
		case SOUTH:
			if(position.y+1 < location.height()){return array[position.y+1][position.x];}
			break;
		case WEST:
			if(position.x-1 >= 0){return array[position.y][position.x-1];}
			break;
		}
		return null;
	}

	/**
	 * Moves the player if a direction if it can
	 *
	 * @param dir - direction to move
	 * @return true if the player moved otherwise false
	 */
	public boolean move(Game.Direction dir, boolean clientSide) {
		if(!clientSide) dir = calcDir(dir);
		if(dir != facing){
			animation.setAnimationDirection(dir.ordinal());
			facing = dir;
			return true;
		}
		Tile tile = getTile(dir);
		if(tile == null){return false;}
		if(tile instanceof EntranceTile) {
			EntranceTile ent = (EntranceTile) tile;
			if(!ent.enter(this)){
				return false;
			}
			return true;
		}
		//don't walk onto a tile with an alive player on it
		if(tile.getPlayer() != null){
			if(!(tile.getPlayer().isDead())) {return false;}
		}
		//don't walk over a tile that doesn't allow it
		if(!tile.isPassable()){return false;}
		//can walk over items and lootBags
		if(tile.containedEntity() != null){
			if(!(tile.containedEntity() instanceof Item || tile.containedEntity() instanceof LootBag)) {
				return false;
			}
		}
		// if there is a building tile in front of the player return false
		if(location instanceof OutsideLocation) {
			OutsideLocation out = (OutsideLocation) location;
			Tile[][] tiles = out.getBuildingTiles();
			if(tiles[tile.getPosition().y][tile.getPosition().x] != null) {
				return false;
			}
		}
		facing = moveDir(dir);
		//update the tile the player is standing on
		// and the tile's player
		tile.setPlayer(this);
		standingOn.setPlayer(null);
		standingOn = tile;
		return true;
	}

	/**
	 * Calculates the direction the player should move based on the direction of the camera(direction field)
	 *
	 * @param dir - the direction the player is trying to move in
	 * @return the direction the player should move in
	 */
	private Direction calcDir(Direction dir) {
		switch(direction) {
		case EAST:
			int k = Direction.EAST.ordinal() + dir.ordinal();
			if(k>3){k = k - 4;}
			return Direction.values()[k];
		case NORTH:
			return Direction.values()[Direction.NORTH.ordinal() + dir.ordinal()];
		case SOUTH:
			int i = Direction.SOUTH.ordinal() + dir.ordinal();
			if(i > 3) { i = i - 4;}
			return Direction.values()[i];
		case WEST:
			int j = Direction.WEST.ordinal() + dir.ordinal();
			if(j > 3) { j = j - 4;}
			return Direction.values()[j];
		default:
			return Direction.NORTH;
		}
	}

	/**
	 * updates the players position field to one tile in a direction
	 *
	 * @param dir to move
	 */
	private Direction moveDir(Game.Direction dir) {
		Direction newDir = null;
		switch(dir) {
		case NORTH:
			position = new Point(position.x, position.y-1);
			animation.setAnimationDirection(0);
			newDir = Direction.NORTH;
			break;
		case EAST:
			position = new Point(position.x+1, position.y);
			animation.setAnimationDirection(1);
			newDir = Direction.EAST;
			break;
		case SOUTH:
			position = new Point(position.x, position.y+1);
			animation.setAnimationDirection(2);
			newDir = Direction.SOUTH;
			break;
		case WEST:
			position = new Point(position.x-1, position.y);
			animation.setAnimationDirection(3);
			newDir = Direction.WEST;
			break;
		default:
			break;
		}
		animation.cycle();
		return newDir;
	}

	/**
	 * Change the direction of the player based on the key that
	 * was pressed and the direction the camera is currently facing.
	 * Accepts KeyEvent.VK_E and KeyEvent.VK_Q
	 * @param key - key press
	 */
	public void changeDirection(int key) {
		switch(key) {
		case KeyEvent.VK_E:
			switch(direction){
			case NORTH:
				direction = Direction.WEST;
				return;
			case EAST:
				direction = Direction.NORTH;
				return;
			case SOUTH:
				direction = Direction.EAST;
				return;
			case WEST:
				direction = Direction.SOUTH;
				return;
			}
		case KeyEvent.VK_Q:
			switch(direction){
			case NORTH:
				direction = Direction.EAST;
				return;
			case EAST:
				direction = Direction.SOUTH;
				return;
			case SOUTH:
				direction = Direction.WEST;
				return;
			case WEST:
				direction = Direction.NORTH;
				return;
			}
		}
	}

	/**
	 * set the direction that the player is facing
	 * @param dir
	 */
	public void setFacing(Direction dir){
		this.facing = dir;
	}

	/**
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the current position of the player
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * update the player's position
	 * @param position to update to
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return current location of the player
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * update the location of the player
	 * @param location to update to
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the direction the camera is facing
	 */
	public Direction getCameraDirection() {
		return direction;
	}

	/**
	 * @return the animation object of the player
	 */
	public Animation getAnimation(){
		return animation;
	}

	/**
	 * @return current health of the player
	 */
	public int getHealth() {
		return health;
	}

	/**
	 *  set the health of the player. 
	 *  Setting the players health to a number < 0 will set the players health to 0
	 * @param health
	 */
	public void setHealth(int health) {
		if(health <= 0){
			this.health = 0;
		}
		else {
			this.health = health;
		}
	}

	/**
	 * @return true if the player is dead
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * kill or revive the player
	 * @param isDead
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * @return true if the player is currently attacking
	 */
	public boolean isAttacking(){
		return attacking;
	}

	/**
	 * set the player to be attacking or not
	 * @param b
	 */
	public void setAttacking(boolean b) {
		attacking = b;
	}

	/**
	 * @return the direction that the player is facing
	 */
	public Direction getFacing() {
		return facing;
	}

	/**
	 * @return the tile that the player is standing on
	 */
	public Tile getStandingOn() {
		return standingOn;
	}

	/**
	 * set the tile that the player is standig on
	 * @param standingOn - tile for the player to stand on
	 */
	public void setStandingOn(Tile standingOn) {
		this.standingOn = standingOn;
	}

	/**
	 * @return the weapon that the player is holding
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * set the weapon the player is holding
	 * @param weapon
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the maximum health of the player
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * set the maximum health the player can have
	 * @param maxHealth
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * @return the armour that the player is wearing
	 */
	public Armour getArmour() {
		return armour;
	}

	/**
	 * set the armour for the player to wear
	 * @param armour
	 */
	public void setArmour(Armour armour) {
		this.armour = armour;
	}

	/**
	 * @return the score of the player
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return array containing the inventory of the player
	 */
	public Item[] getInventory() {
		return inventory;
	}

}
