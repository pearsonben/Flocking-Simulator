package boids;

import drawing.Canvas;
import geometry.AngleWithPosition;
import geometry.CartesianCoordinate;

public class FlockingBoid extends DynamicBoid implements Entity{
	
	// radius of the boid
	private double radius = 100;
	
	// stores the slider values for cohesion, alignment, and separation
	private double cohesion;
	private double alignment;
	private double separation;
	// the flock of all boids within radius
	private Flock boidsWithinRadius;
	
	public FlockingBoid(Canvas myCanvas, CartesianCoordinate currentPosition) {
		super(myCanvas, currentPosition);
		this.setAngle(Math.toRadians(Math.random()*360));
		boidsWithinRadius = new Flock();
		
	}

	public FlockingBoid(Canvas myCanvas, double xPosition, double yPosition) {
		super(myCanvas, xPosition, yPosition);
		this.setAngle(Math.toRadians(Math.random()*360));
		boidsWithinRadius = new Flock();

	}
	
	public FlockingBoid(){
		super();
	}
	
	
	// GETTERS AND SETTERS--------------------------------
	public void setCohesion(double cohesion) {
		this.cohesion = cohesion;
	}

	public void setAlignment(double alignment) {
		this.alignment = alignment;
	}
	public void setSeparation(double separation) {
		this.separation = separation;
	}
	public void setRadius(int radius){
		this.radius = radius;
	}
	
	public double getRadius(){
		return this.radius;
	}
	
	@Override
	public void setSpeed(int value) {
		this.speed = value;
		
	}
	//---------------------------------------------------
	
	
	// empties the radius flock
	public void emptyFlock(){
		boidsWithinRadius.emptyFlock();
	}
	// function that applies the flocking behaviour rules
	public void setFlockingAttributes(Flock entireFlock){
		
		// searches the entireflock looking for boids within the radius
		boidsWithinRadius = searchFlock(entireFlock);
		
		// if there is another boid within radius
		if(boidsWithinRadius.size() > 0){
			// calculate the average position and angle of all boids within the radius
			AngleWithPosition averages = boidsWithinRadius.calculateAverages(this);
			
			// calculates the required turning angles for each rule 
			double cohesionAngle = calculateCohesionAngle(averages);
			double separationAngle = calculateSeparationAngle(averages);
			double alignmentAngle = (averages.getAverageAngle() - Math.toDegrees(this.getAngle()));
			
			for(Entity entities : boidsWithinRadius.getFlock()){
				// if a predator is within radius, turn away from the predator
				if(entities instanceof PredatorBoid){
					this.turn(separationAngle* 0.005);
				}
			}
			// otherwise, apply the standard flocking rules
			this.turn(cohesion * (cohesionAngle/4));
			this.turn(separation * separationAngle/4);
			this.turn(alignment * alignmentAngle/4);
			
		}
		
	}
	// calculates the required angle to turn towards the centre of mass of the boids within radius
	public double calculateCohesionAngle(AngleWithPosition averages){
		
		// calculating the angle between the current boid, and the centre of mass of its radius
		double xPart = averages.getAverageX() - this.getPositionX();
		double yPart = averages.getAverageY() - this.getPositionY();
	
		// https://stackoverflow.com/questions/17296066/angle-between-2-points-with-atan2
		double angle =  90 - (Math.toDegrees(this.getAngle()) + Math.toDegrees(Math.atan2(yPart, xPart)));
		// sortAngle keeps the angle between -180 and +180 degrees
		return sortAngle(angle);
		
	}
	// calculates the required angle to turn away from the centre of mass of the boids within radius
	public double calculateSeparationAngle(AngleWithPosition averages){
	
		double xPart = averages.getAverageX() - this.getPositionX();
		double yPart = averages.getAverageY() - this.getPositionY();
	
		double angle =  90 - (Math.toDegrees(this.getAngle())+ Math.toDegrees(Math.atan2(yPart, xPart)));
		return -sortAngle(angle);
	}
	
	
	// checks if a predator is within kill range, returns true or false
	public Boolean checkForPredator(Flock entireFlock){
		
		double xPart; double yPart;
		double predatorRadius = 30;	
		
		synchronized(entireFlock){
			for(Entity entities : entireFlock.getFlock()){
				// if the entity is a predator
				if(entities != this && (entities instanceof PredatorBoid)){
					xPart = entities.getPositionX();
					yPart = entities.getPositionY();
					// if the predator is within 30 pixels of the entity
					if(Math.sqrt( Math.pow(xPart - this.getX(), 2) + Math.pow(yPart - this.getY(), 2)) < predatorRadius){
						return true;
					}
				}
				
				
				
			}
		}
		// if there is no predator in range, return false
		return false;
		
	}
		
	// searches the entire flock for boids within the current boids surroundings
	public Flock searchFlock(Flock entireFlock){
		
		double xPart; double yPart;
		synchronized(entireFlock){
			for(Entity entities : entireFlock.getFlock()){
				// searches the entire flock for all entities except the current one
				if (entities != this){
					
					xPart = entities.getPositionX();
					yPart = entities.getPositionY();
					// if within radius, add to the boidsWithinRadius flock	
					if(Math.sqrt( Math.pow(xPart - this.getX(), 2) + Math.pow(yPart - this.getY(), 2)) < this.getRadius()){
								boidsWithinRadius.addToFlock(entities);
					}
					
				}
				
			}
			
		}
		
		return boidsWithinRadius;
		
	}

	

}
