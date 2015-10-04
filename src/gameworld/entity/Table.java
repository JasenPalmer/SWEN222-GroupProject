package gameworld.entity;

import gameworld.location.Location;

import java.awt.Point;

public class Table implements BasicEntity{

	@Override
	public String name() {
		return "Table";
	}

	@Override
	public String description() {
		return "This is a table";
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
