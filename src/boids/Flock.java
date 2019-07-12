package boids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import geometry.AngleWithPosition;
import geometry.CartesianCoordinate;

public class Flock {
	// an arraylist containing both boids and predators
	public List<Entity>flock;
	
	public Flock() {
		// Collections is used to stop errors when accessing elements in the arraylist
		// https://www.geeksforgeeks.org/collections-synchronizedlist-method-in-java-with-examples/
		flock = Collections.synchronizedList(new ArrayList<Entity>());
		flock = new ArrayList<Entity>();	
	}
	
	// function to return the arrayList of elements within the flock
	public List<Entity> getFlock(){
		return flock;
	}
	
	// adds an entity to the flock
	public void addToFlock(Entity entity){
		flock.add(entity);
	}
	
	// removes the most recent entity of the flock
	public void removeLastElement(){
		flock.remove(flock.size()-1);
	}
	
	// removes a specific element of the flock
	public void removeElement(int index){
		flock.remove(index);
	}
	
	// returns the entity at the given index of the flock
	public Entity getFlockElement(int index){
		return flock.get(index);
	}
	
	// returns the number of elements in the flock
	public int size(){
		return flock.size();
	}
	
	// removes a specific entity from the flock
	public void removeEntity(Entity entity){
		flock.remove(entity);
	}
	
	// empties everything in the flock
	public void emptyFlock(){
		flock.removeAll(flock);
	}
	
	// checks if the list has any elements
	public Boolean isEmpty(){
		return flock.isEmpty();
	}
	
	
	// returns an angleWithPosition containing the average X position, average Y Position, and average Angle of all elements in the flock
	public AngleWithPosition calculateAverages(FlockingBoid boid){
		
		double averageAngle = 0; double averageX = 0; double averageY = 0; int counter = 0;
		
		synchronized(flock){
			
			for(Entity entities : flock){
				// checks all entities except for the current one
				if(entities != boid){
					
					// adds the total angle and positions of all the entities
					averageAngle = averageAngle + Math.toDegrees(entities.getAngle()); 
					averageX = averageX + entities.getPositionX();
					averageY = averageY + entities.getPositionY();
					counter++;
				}
				
			}
			
		}
		
		// divides the averages by the number of entities to obtain an average
		averageAngle = averageAngle / counter;
		averageX = averageX / counter;
		averageY = averageY / counter;
		// creates a new angleWithPosition with the average values
		AngleWithPosition averages = new AngleWithPosition(new CartesianCoordinate(averageX, averageY), averageAngle);
		
		return averages;
		
	}
	
	
	
	
	

}
