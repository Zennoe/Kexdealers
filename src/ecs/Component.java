package ecs;

public abstract class Component {
	// Nothing here ¯\_(ツ)_/¯
	public abstract int getEID();
	public abstract void setEID(int eID);
	
	@Override
	public abstract Component clone();
	@Override
	public abstract String toString();
}
