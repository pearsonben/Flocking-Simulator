package geometry;

// class that stores a cartesian co-ordinate and angle, used for returning the average values of position and angle for the boid radiuses
public class AngleWithPosition{

	// storing current position as a CartesianCoordinate, and also storing the angle
	private CartesianCoordinate position;
	private double angle;
	
	public AngleWithPosition(CartesianCoordinate position, double angle) {
		this.position = position;
		this.angle = angle;
	}
	
	// returns the average angle
	public double getAverageAngle(){
		return this.angle;
	}
	// returns the X position
	public double getAverageX(){
		return position.getX();
	}
	// returns the Y Position
	public double getAverageY(){
		return position.getY();
	}
	
	

}
