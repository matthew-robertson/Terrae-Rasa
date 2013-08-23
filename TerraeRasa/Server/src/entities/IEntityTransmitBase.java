package entities;

public interface IEntityTransmitBase {
	public abstract int getEntityID();
	
	public abstract int getEntityType();
	
	public abstract void setPosition(double x, double y);
}
