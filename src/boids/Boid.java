package boids;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Boid {
	
	protected Canvas myCanvas;
	
	private double angle;
	protected CartesianCoordinate currentPosition;
	
	// enable/disable drawing to the canvas
	private Boolean isPenUp;
	// stores the amount of lines that have been drawn to the canvas
	private int lines = 0;
	
	
	public Boid(Canvas myCanvas, CartesianCoordinate currentPosition) {
		this.myCanvas = myCanvas;
		this.angle = 0;
		this.currentPosition = currentPosition;
		putPenDown();
	}
	
	public Boid(Canvas myCanvas, double xPosition, double yPosition){
		this.myCanvas = myCanvas;
		this.angle = 0;
		CartesianCoordinate startPoint = new CartesianCoordinate(xPosition,yPosition);
		this.currentPosition = startPoint;
		putPenDown();
	}
	
	
	public Boid(){
		
	}

	// turns the boid by the given angle
	public void turn(double angle){
		this.angle = this.angle + Math.toRadians(angle);
	}
	
	// moves the boid distance in pixels
	public void move(double distance){
		
		// calculating the new positions co-ordinates
		double x = distance * Math.sin(this.angle) + this.currentPosition.getX();
		double y = distance * Math.cos(this.angle) + this.currentPosition.getY();
		CartesianCoordinate destination = new CartesianCoordinate(x,y);
		// if the pen is down, draw a line between points
		if(!isPenUp){
			myCanvas.drawLineBetweenPoints(currentPosition, destination);
			currentPosition = destination;
		}
		else{ // if the pen is up, move to new destination but dont draw a line
			currentPosition = destination;
		}	
	}
	// draws a small triangle which represents the boid
	public void draw(){
		 
		Boid drawBoid = new Boid(myCanvas, this.currentPosition);
		drawBoid.setAngle(angle);
	
		drawBoid.turn(180);
		drawBoid.move(6);
		drawBoid.turn(-180);
		drawBoid.putPenDown();
		drawBoid.turn(90);
		drawBoid.move(10);
		drawBoid.turn(-120);
		drawBoid.move(20);
		drawBoid.turn(-120);
		drawBoid.move(20);
		drawBoid.turn(-120);
		drawBoid.move(10);
		lines = 5;
	}
	// undraws the boid
	public void undraw(){
		// for every line drawn, undraw each one
		for(int i = 0; i < lines; i ++){
			myCanvas.removeMostRecentLine();
		}
		
		lines = 0;
		myCanvas.repaint();
		
	}
	// puts the pen down
	public void putPenDown(){
		this.isPenUp = false;
	}
	
	// puts the pen up
	public void putPenUp(){
		this.isPenUp = true;
	}

	// --------------------------------------------------GETTERS & SETTERS-------------------------------------
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public double getX(){
		return this.currentPosition.getX();
	}
	
	public double getY(){
		return this.currentPosition.getY();
	}
	
	public double getPositionX(){
		return this.currentPosition.getX();
	}
	
	public double getPositionY(){
		return this.currentPosition.getY();
	}
	//--------------------------------------------------------------------------------------------------
	
	
	// keeps the boid within bounds of the screen dimensions, if it reaches the edge, wrap around to the other side
	public void wrapPosition(double xLim, double yLim){
		
		// if boids x position is less than 0, move the boid to the other side of the screen
		if(currentPosition.getX()<0){
			currentPosition.setX(xLim);
		}
		else if(currentPosition.getY() < 0){
			currentPosition.setY(yLim);
		}
		else if(currentPosition.getX() > xLim){
			currentPosition.setX(0);
		}
		else if(currentPosition.getY() > yLim){
			currentPosition.setY(0);
		}
		
	}

	// will be overridden in subclass
	public void setSpeed(int value) {
		// TODO Auto-generated method stub
		
	}
	
	
}
