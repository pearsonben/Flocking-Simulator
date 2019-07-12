package boids;

// the interface is used to enable the use of a polymorphic array in the flock class
// this interfaces between PredatorBoid and FlockingBoid, which are both is-a Boid.

public interface Entity {
	
	public abstract void draw();
	
	public abstract void undraw();
	
	public abstract double getAngle();
	
	public abstract double getPositionX();
	
	public abstract double getPositionY();

	public abstract void setSpeed(int value);

	public abstract void setSeparation(double d);

	public abstract void setRadius(int value);

	public abstract void setCohesion(double d);

	public abstract void setAlignment(double d);

	public abstract void setAngle(double radians);

	public abstract void update(double deltaTime);

	public abstract void setFlockingAttributes(Flock entireFlock);

	public abstract void emptyFlock();
		
	public abstract Boolean checkForPredator(Flock entireFlock);
}
