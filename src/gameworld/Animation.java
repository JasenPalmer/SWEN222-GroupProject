package gameworld;

import gameworld.entity.Weapon.WeaponType;

import java.io.Serializable;

/**
 * Class used to store information on player animation
 * @author Jake Dorne
 *
 */
public class Animation implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * Player animation belongs to
	 */
	private Player player;
	
	/**
	 * Number representing the direction of the animation
	 */
	private int animationDirection;
	
	/**
	 * frame number in walk cycle
	 */
	private int walkFrame;
	
	/**
	 * frame number in attack cycle
	 */
	private double attackFrame;

	public Animation(Player player){
		animationDirection = 0;
		walkFrame = 0;
		attackFrame = 0;
		this.player = player;
	}

	/**
	 * Cycles the walk animation to the next frame and resets to 0 if on the last one.
	 */
	public void cycle() {
		walkFrame++;
		if(walkFrame==8){
			walkFrame = 0;
		}
	}

	/**
	 * resets walk animation back to first frame of animation cycle.
	 */
	public void resetWalk(){
		walkFrame = 0;
	}

	/**
	 * Cycles to next frame of attack animation, resetting to start and ending attack if on the last part of animation.
	 */
	public void cycleAttack(){
		if(player.isAttacking()){
			attackFrame+=0.5;
		}
		if(player.getWeapon().getType().equals(WeaponType.Spear) && attackFrame>=7 || player.getWeapon().getType().equals(WeaponType.Shank) && attackFrame>=5){
			attackFrame = 0;
			player.setAttacking(false);
		}
	}

	public int getAttackFrame() {
		return (int)attackFrame;
	}
	
	public int getAnimationDirection() {
		return animationDirection;
	}

	public void setAnimationDirection(int animationDirection) {
		this.animationDirection = animationDirection;
	}

	public int getWalkFrame() {
		return walkFrame;
	}

	public void setWalkFrame(int walkFrame) {
		this.walkFrame = walkFrame;
	}
}
