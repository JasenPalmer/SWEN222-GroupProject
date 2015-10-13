package gameworld;

import gameworld.entity.Weapon.WeaponType;

import java.io.Serializable;

public class Animation implements Serializable{
	private static final long serialVersionUID = 1L;

	private Player player;
	private int animationDirection;
	private int walkFrame;
	private double attackFrame;

	public Animation(Player player){
		animationDirection = 0;
		walkFrame = 0;
		attackFrame = 0;
		this.player = player;
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

	public void cycle() {
		walkFrame++;
		if(walkFrame==8){
			walkFrame = 0;
		}
	}

	public void resetWalk(){
		walkFrame = 0;
	}

	public void cycleAttack(){
		if(player.isAttacking()){
			attackFrame+=0.25;
		}
		if(player.getWeapon().getType().equals(WeaponType.Spear) && attackFrame>=7 || player.getWeapon().getType().equals(WeaponType.Shank) && attackFrame>=5){
			attackFrame = 0;
			player.setAttacking(false);
		}
	}

	public int getAttackFrame() {
		return (int)attackFrame;
	}
}
