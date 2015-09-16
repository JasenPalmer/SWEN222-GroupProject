package gameworld.entity;

import gameworld.location.InsideLocation;

import java.awt.Point;

/**
 * A rock
 * @author Jasen
 *
 */
public class Rock implements BasicEntity{

	@Override
	public String name() {
		return "Rock";
	}

	@Override
	public String description() {
		return "A rock";
	}

	@Override
	public Point position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsideLocation location() {
		// TODO Auto-generated method stub
		return null;
	}

}
