package boids;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import tools.Utils;
public class DynamicBoid extends Boid{
	
	// randomCounter and randomDouble are used to simulate realistically random movements of the boids
	int randomCounter;
	double randomDouble;
	// movement speed of the boid
	protected int speed;
	
	public DynamicBoid(Canvas myCanvas, CartesianCoordinate currentPosition) {
		// invokes the superclasses constructor with the fields being passed
		super(myCanvas, currentPosition);
		randomCounter = 0;
		// setting randomDouble to a random value
		setRandomDouble();
	}

	public DynamicBoid(Canvas myCanvas, double xPosition, double yPosition) {
		super(myCanvas, xPosition, yPosition);
		randomCounter = 0;
		setRandomDouble();
	}
	
	public DynamicBoid(){
		super();
	}

	// getter for speed
	public int getSpeed() {
		return speed;
	}
	// setter for speed
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	// updates the position and angle of the boid
	public void update(double time){
		// turn a small random amount for the first 10 iterations
		if(randomCounter < 10){
			this.turn(randomDouble);
			setRandomDouble();
		}
		// ensures that the current angle is still within the bounds of -180 to +180
		this.setAngle(sortAngle(this.getAngle()));
		
		// converting time to seconds
		time = time / 1000;
		// putting the pen up means stop drawing to the canvas
		putPenUp();
		// distance = speed * time, so move speed * time
		move(speed*time);
		//  enable drawing to canvas
		putPenDown();
		// ensures the boids stay within the boundaries of the screen, when they reach the edge, wrap around to the other side
		wrapPosition(1600,920);
		//  on the 40th iteration, reset the randomCounter enabling the boid to start turning randomly again
		if(randomCounter >= 40){
			randomCounter = 0;
		}
		randomCounter++;
		
	}
	// sets randomDouble to a random number between 0 and 360 degrees
	public void setRandomDouble(){
		
		randomDouble = sortAngle(Utils.randomNumberBetween(0,360));
		randomDouble = randomDouble/30;
		
	}
	// ensures the angle stays between -180 and +180 degrees
	public double sortAngle(double angle){
		// while the angle is more than 180, - 360 from the value to stay within -180 to 180 bounds
		while(angle > 180){
			angle = angle - 360;
		}
		// and vice versa
		while(angle < -179){
			angle = angle + 360;
		}
		return angle;
	}
	
}
