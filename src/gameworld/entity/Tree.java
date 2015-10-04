package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class Tree implements BasicEntity{

	@Override
	public String name() {
		return "Tree";
	}

	@Override
	public String description() {
		return "This is a tree";
	}

	@Override
	public Point position() {
		return null;
	}

	@Override
	public Location location() {
		return null;
	}

}
