package boids;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class PredatorBoid extends DynamicBoid implements Entity{
	
	// the kill radius for the predator
	private int radius = 30;
	// the number of lines that have been drawn to the canvas
	private int lines = 0;
	// arraylist of all the boids within its radius
	private Flock boidsWithinRadius;
	
	public PredatorBoid(Canvas canvas, double xPosition, double yPosition){
		super(canvas, xPosition, yPosition);
	}
	
	public PredatorBoid(Canvas canvas, CartesianCoordinate currentPosition){
		super(canvas,currentPosition);
	}

	// overrides the draw function for regular boids. 
	// draws the exact same shape, except much bigger
	@Override
	public void draw(){
		
		PredatorBoid drawBoid = new PredatorBoid(myCanvas, this.currentPosition);
		drawBoid.setAngle(this.getAngle());
		drawBoid.turn(180);
		drawBoid.move(12);
		drawBoid.turn(-180);
		drawBoid.putPenDown();
		drawBoid.turn(90);
		drawBoid.move(20);
		drawBoid.turn(-120);
		drawBoid.move(40);
		drawBoid.turn(-120);
		drawBoid.move(40);
		drawBoid.turn(-120);
		drawBoid.move(20);
		lines = 5;
		
		
	}
	// searches through all entities in the flock to see which boids are within the predators radius
	public Flock searchFlock(Flock entireFlock){
		
		double xPart; double yPart;
				
		synchronized(entireFlock){
			for(Entity entities : entireFlock.getFlock()){
				// if the entity isnt a predator
				if ((entities != this) && !(entities instanceof PredatorBoid)){
					// gets the x and y position
					xPart = entities.getPositionX();
					yPart = entities.getPositionY();
					
					// if the entity is within the predators radius
					// https://math.stackexchange.com/questions/198764/how-to-know-if-a-point-is-inside-a-circle
					if(Math.sqrt( Math.pow(xPart - this.getX(), 2) + Math.pow(yPart - this.getY(), 2)) < this.radius){
								// add the entity to the withinradius flock
								boidsWithinRadius.addToFlock(entities);
					}
					
				}
				
			}
			
		}
		// returns arraylist of boids within the radius
		return boidsWithinRadius;
		
	}
	
	// undraws the Predator
	@Override
	public void undraw(){
		
		for(int i = 0; i < lines; i++){
			// for every line drawn to the canvas, remove the most previous line
			myCanvas.removeMostRecentLine();
		}
		
		lines = 0;
		myCanvas.repaint();
	}
	// sets the speed of the predator
	@Override
	public void setSpeed(int value) {
		this.speed = value;
	}

	
	// -----------------------------------------------------------------INTERFACE------------------------------------------------
	
	// these functions below are used in FlockingBoid, and have no use in predator boid 
	
	
	@Override
	public void setSeparation(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRadius(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCohesion(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAlignment(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFlockingAttributes(Flock entireFlock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emptyFlock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean checkForPredator(Flock entireFlock) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
