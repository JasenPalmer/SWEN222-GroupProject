package gameworld;

import java.io.Serializable;

public class Animation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private int animationDirection;
	private int currentFrame;
	
	public Animation(){
		animationDirection = 0;
		currentFrame = 0;
	}

	public int getAnimationDirection() {
		return animationDirection;
	}

	public void setAnimationDirection(int animationDirection) {
		this.animationDirection = animationDirection;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void cycle() {
		currentFrame++;
		if(currentFrame==8){
			currentFrame = 0;
		}
	}
}
