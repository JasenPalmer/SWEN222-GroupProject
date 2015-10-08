package gameworld;

import gameworld.Game.Direction;
import gameworld.entity.Entity;
import gameworld.entity.Item;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.Tile;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Player implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Default size of the players inventory
	 */
	private static final int DEFAULT_INV_SIZE = 8;
	
	/**
	 * Players damage
	 */
	private int playerDamage = 100;

	/**
	 * Players name
	 */
	private String name;

	/**
	 * The items that the player has in their inventory
	 */
	private  Item[] inventory;
	
	/**
	 * The current location the player is in
	 */
	private Location location;

	/**
	 * The current position of the player
	 */
	private Point position;

	/**
	 * Direction that the screen is facing
	 */
	private Direction direction = Game.Direction.NORTH;

	/**
	 * Used for storing animation fields for player such as direction and cycle point
	 */
	private Animation animation;

	/**
	 * The tile that the player is standing on
	 */
	private Tile standingOn;

	/**
	 * Amount of health the player has
	 */
	private int health;

	/**
	 * is the player dead ie health <= 0
	 */
	private boolean isDead;

	/**
	 * Is the player currently attacking
	 */
	private boolean attacking;

	/**
	 * Direction the player is facing
	 */
	private Direction facing = Game.Direction.NORTH;

	public Player(String name, Game game) {
		//set user name
		this.name = name;	
		//create inventory
		inventory = new Item[DEFAULT_INV_SIZE];	
		//set location and add player to location
		location = game.getLocations().iterator().next();
		location.addPlayer(this);
		//players position
		position = new Point(2, 2);
		//set the tile's player and the players tile
		standingOn = location.getTileAt(position);
		standingOn.setPlayer(this);
		//give the player animations
		animation = new Animation(this);
		// make the player alive
		health = 100;
		isDead = false;
	}

	/**
	 * Make this player attack the player in the tile in fron of them
	 * @return true if the attack was successful
	 */
	public boolean attack() {
		Tile tile = getTile(getFacing());
		// nothing in front of the player
		if(tile == null){return false;}
		// if there is no player in front of the player return false
		if(tile.getPlayer() == null){return false;}
		Player opponent = tile.getPlayer();
		opponent.setHealth(opponent.getHealth()-playerDamage);
		return true;
	}

	/**
	 * Removes an item from the players inventory and places it on the ground
	 *
	 * @param item to be removed
	 * @return true if the item was successfully removed
	 */
	public boolean dropFromInv(int index) {
		// get the item
		Item item = inventory[index];
		if(item == null){return false;}
		// remove the item from the inventory
		inventory[index] = null;
		// place the item on the ground
		location.getTileAt(position).setEntity(item);
		return true;
	}

	/**
	 * Player attempts to pick-up the item on the tile infront of them
	 * @return true if the item was successfully added
	 */
	public boolean pickupItem() {
		// if the inventory is full can't pick up item
		if(inventoryFull()){return false;}
		Tile tile = getTile(getFacing());
		Entity ent = tile.containedEntity();
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
	 * @param dir to get the tile from
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
	public boolean move(Game.Direction dir) {
		dir = calcDir(dir);
		if(dir != facing){
			animation.setAnimationDirection(dir.ordinal());
			facing = dir;
			return true;
		}
		Tile tile = getTile(dir);
		if(tile == null){return false;}
		if(tile instanceof EntranceExitTile) {
			EntranceExitTile ent = (EntranceExitTile) tile;
			ent.enter(this);
		}
		if(tile.getPlayer() != null){return false;}
		if(!tile.isPassable()){return false;}
		if(tile.containedEntity() != null){
			if(!(tile.containedEntity() instanceof Item)) {
				return false;
			}
		}
		// if there is a building tile in front of the player return false
		if(location instanceof OutsideLocation) {
			OutsideLocation out = (OutsideLocation) location;
			Tile[][] tiles = out.getBuildingTiles();
			if(tiles[tile.getPos().y][tile.getPos().x] != null) {
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
	 * Change the direction of the player based on the key that was pressed and the direction the camera is currently facing.
	 * Accepts KeyEvent.VK_E and KeyEvent.VK_Q
	 * @param key - key press
	 */
	public void changeDirection(int key) {
		switch(key) {
		case KeyEvent.VK_E:
			switch(direction){
			case NORTH:
				direction = Direction.WEST;
				break;
			case EAST:
				direction = Direction.NORTH;
				break;
			case SOUTH:
				direction = Direction.EAST;
				break;
			case WEST:
				direction = Direction.SOUTH;
				break;
			}
		case KeyEvent.VK_Q:
			switch(direction){
			case NORTH:
				direction = Direction.EAST;
				break;
			case EAST:
				direction = Direction.SOUTH;
				break;
			case SOUTH:
				direction = Direction.WEST;
				break;
			case WEST:
				direction = Direction.NORTH;
				break;
			}
		}
	}

	// getters and setters

	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Direction getDirection() {
		return direction;
	}

	public Animation getAnimation(){
		return animation;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isAttacking(){
		return attacking;
	}
	
	public void setAttacking(boolean b) {
		attacking = b;

	}

	public Direction getFacing() {
		return facing;
	}
	
	public Tile getStandingOn() {
		return standingOn;
	}

	public void setStandingOn(Tile standingOn) {
		this.standingOn = standingOn;
	}
}
