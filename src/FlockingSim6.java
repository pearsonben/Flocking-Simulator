import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boids.Boid;
import boids.Entity;
import boids.Flock;
import boids.FlockingBoid;
import boids.PredatorBoid;
import drawing.Canvas;
import tools.Utils;

public class FlockingSim6 {
	
	private JFrame frame;
	private Canvas canvas;
	private int WINDOW_X_SIZE = 1600;
	private int WINDOW_Y_SIZE = 1000;
	private Boolean continueRunning;
	private JLabel boidCounter;
	private JLabel predatorCounter;
	private JLabel scoreLabel;
	private JLabel gameDescription;
	private JPanel lowerPanel;
	private JPanel upperPanel;
	private JButton addBoidButton;
	private JButton removeBoidButton;
	private JButton addPredatorButton;
	private JButton removePredatorButton;
	private JButton startGameButton;
	private JSlider boidSpeedSlider;
	private JLabel boidSpeedLabel;
	private JSlider cohesionSlider;
	private JSlider separationSlider;
	private JSlider alignmentSlider;
	private JSlider rangeSlider;
	private Flock entireFlock;
	private int boids = 0;
	private int predators = 0;
	private double score = 0;
	private Entity entityToBeRemoved = new FlockingBoid();
	private long startTime;
	private long currentTime;
	private Boolean startGame = false;
	
	// setting up the JFrame
	private void setupGUI(){
		frame = new JFrame();
		frame.setTitle("Flocking Simulator");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new Canvas();

		// initialising the upper and lower panels, which will contain sliders, buttons, and scores
		lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7,2));
		upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
	}
	
	private void setupSliders(){
		
		// -------------------------------------------------------SLIDERS-------------------------------------------------------------
		
		boidSpeedSlider.addChangeListener(new ChangeListener(){

			// do this when the value of the slider is changed
			@Override
			public void stateChanged(ChangeEvent arg0) {
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						// if the entity is a predator, do nothing
						if(entities instanceof PredatorBoid){
							
						}
						else{ // otherwise, update the speed of the boids
							entities.setSpeed(boidSpeedSlider.getValue());
							boidSpeedLabel.setText("Speed: " + boidSpeedSlider.getValue());
						}
		
					}
				}
				
			}
		});
		
		// Note: Rest of the sliders operate exactly the same as above
		
		separationSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						if (!(entities instanceof PredatorBoid)){

						entities.setSeparation((double)separationSlider.getValue()/(10*5));	
						}
					}
				}
				
			}
		});
		
		// listener for the cohesion slider
		cohesionSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						if (!(entities instanceof PredatorBoid)){
							entities.setCohesion((double)cohesionSlider.getValue()/(10*5));
						}
					}
				}
				
			}
		});
		
		// alignment slider listener
		alignmentSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						if (!(entities instanceof PredatorBoid)){
								
						entities.setAlignment((double)alignmentSlider.getValue()/(10*5));
						}
					}
				}
				
			}
		});
		
		// listener for the Range slider
				rangeSlider.addChangeListener(new ChangeListener(){
					
					@Override
					public void stateChanged(ChangeEvent arg0){
						for(Entity entities : entireFlock.getFlock()){
							if (!(entities instanceof PredatorBoid)){

							entities.setRadius(rangeSlider.getValue());	
							}
						}
					}
				});
		
		//--------------------------------------------------------BUTTONS---------------------------------------------------------------------
		
		
		
		addBoidButton.addActionListener(new ActionListener(){

			// when a button is pressed, do this
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					// adding a new boid to the flock. setting all of its fields to values set by the sliders, as well as 
					// random starting angle and position
					entireFlock.addToFlock(new FlockingBoid(canvas, Utils.randomNumberBetween(500, 900),Utils.randomNumberBetween(300, 700)));
					entireFlock.getFlockElement(entireFlock.size()-1).setSpeed(boidSpeedSlider.getValue());
					entireFlock.getFlockElement(entireFlock.size()-1).setRadius(rangeSlider.getValue());
					entireFlock.getFlockElement(entireFlock.size()-1).setAngle(Math.toRadians(Utils.randomNumberBetween(0, 360)));
					entireFlock.getFlockElement(entireFlock.size()-1).setCohesion((double)cohesionSlider.getValue()/10);
					entireFlock.getFlockElement(entireFlock.size()-1).setAlignment((double)alignmentSlider.getValue()/10);
					entireFlock.getFlockElement(entireFlock.size()-1).setSeparation((double)separationSlider.getValue()/10);
					
					// boids is a counter for how many boids are present
					boids++;
					boidCounter.setText("Boids: " + boids + "     |     ");
				}
				// when pressing the button quickly, Java throws a ConcurrentModificationException and breaks the program
				// this catch block catches the error, and stops the program from crashing
				// implementation from https://www.baeldung.com/java-concurrentmodificationexception
				catch(java.util.ConcurrentModificationException exception){
					
				}
				
			}
			
		});
		
		// listener for the remove Boid button
		removeBoidButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					// counter is a dummy variable used to iterate through the flock until the element is a boid and not a predator
					int counter = 1;
					// if the flock is not empty
					if(entireFlock.size()!= 0){
							// checks last element in array, if its a predator boid, check the next element along until no longer predator
							while(entireFlock.getFlockElement(entireFlock.size() - counter) instanceof PredatorBoid){
								counter++;
							}
							// undraws the element to be deleted, then removes it from the Flock
							entireFlock.getFlockElement(entireFlock.size()-counter).undraw();
							entireFlock.removeElement(entireFlock.size()-counter);
							
							boids--;
							boidCounter.setText("Boids: " + boids + "     |     ");
						
					}
				}
				catch(java.util.ConcurrentModificationException exception){
					
				}
				
			}
			
		});
		
		// add predator button listener
		addPredatorButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				try{
					// adds a predator to the flock, with random posititon and angle.
					entireFlock.addToFlock(new PredatorBoid(canvas, Utils.randomNumberBetween(100, 1300), Utils.randomNumberBetween(100, 900)));
					entireFlock.getFlockElement(entireFlock.size()-1).setSpeed(100);
					entireFlock.getFlockElement(entireFlock.size()-1).setAngle(Utils.randomNumberBetween(0, 180));
					predators++;
					predatorCounter.setText("Predators: " + predators + "     |     ");
				}
				catch(java.util.ConcurrentModificationException exception){
					
				}
			}
		});
		
		// remove predator button listener
		// exactly same concept and code as the remove boid button listener, only difference is removing predator this time
		removePredatorButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					int counter = 1;
					if(entireFlock.size()!= 0){
							while(entireFlock.getFlockElement(entireFlock.size() - counter) instanceof FlockingBoid){
								counter++;
							}
							entireFlock.getFlockElement(entireFlock.size()-counter).undraw();
							entireFlock.removeElement(entireFlock.size()-counter);
							
							predators--;
							predatorCounter.setText("Predators: " + predators + "     |     ");
						
					}
				}
				catch(java.util.ConcurrentModificationException exception){
					
				}
				
			}
			
		});
		// start game button listener
		startGameButton.addActionListener(new ActionListener(){
			// suppress warnings disables compilation warnings when trying to hide swing elements such as JButtons, JLabels, etc
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				// notes the current time as this is used to tell if 30 seconds has gone by for the game to end
				startTime = System.currentTimeMillis();
				startGame = true;
				// hides all the buttons while game is in progress
				startGameButton.hide();
				gameDescription.hide();
				addPredatorButton.hide();
				addBoidButton.hide();
				removePredatorButton.hide();
				removeBoidButton.hide();
				
			}
			
		});
	}
	
	public FlockingSim6() {
		
		setupGUI();
		
		// entireFlock is the flock that contains every entity on the screen, including both predators and boids
		entireFlock = new Flock();

		// adding the canvas to the frame, and setting background colour to gray
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setBackground(Color.gray);
		
		// setting the text of all the buttons
		removeBoidButton = new JButton("Remove Boid");
		addBoidButton = new JButton("Add Boid");
		addPredatorButton = new JButton("Add Predator");
		removePredatorButton = new JButton("Remove Predator");
		startGameButton = new JButton("Start Game!");
		
		// setting the values of the speed slider
		boidSpeedSlider = new JSlider(0,300,150);
		
		// Hashtable is used to label JSliders
		//  code adapted from https://www.geeksforgeeks.org/hashtable-in-java/
		Hashtable<Integer, JLabel>Speed = new Hashtable<Integer, JLabel>();
		// puts the label below the 150 mark on the slider
		Speed.put(150, new JLabel("Speed"));
		
		// setting the sliders to have notches, with major and minor spacing
		boidSpeedSlider.setLabelTable(Speed);
		boidSpeedSlider.setPaintTicks(true);
		boidSpeedSlider.setPaintLabels(true);
		boidSpeedSlider.setMajorTickSpacing(150);
		boidSpeedSlider.setMinorTickSpacing(30);
		
		// rest of the sliders function exactly the same
		cohesionSlider = new JSlider(0,10,0);
		Hashtable<Integer, JLabel>Cohesion = new Hashtable<Integer, JLabel>();
		Cohesion.put(5, new JLabel("Cohesion"));
		cohesionSlider.setLabelTable(Cohesion);
		cohesionSlider.setPaintTicks(true);
		cohesionSlider.setPaintLabels(true);
		cohesionSlider.setMajorTickSpacing(5);
		cohesionSlider.setMinorTickSpacing(1);
		
		rangeSlider = new JSlider(0,100,50);
		Hashtable<Integer, JLabel>Range = new Hashtable<Integer, JLabel>();
		Range.put(50, new JLabel("Range"));
		rangeSlider.setLabelTable(Range);
		rangeSlider.setPaintTicks(true);
		rangeSlider.setPaintLabels(true);
		rangeSlider.setMajorTickSpacing(50);
		rangeSlider.setMinorTickSpacing(10);
		
		alignmentSlider = new JSlider(0,10,0);
		Hashtable<Integer, JLabel>Alignment = new Hashtable<Integer, JLabel>();
		Alignment.put(5,  new JLabel("Alignment"));
		alignmentSlider.setLabelTable(Alignment);
		alignmentSlider.setPaintTicks(true);
		alignmentSlider.setPaintLabels(true);
		alignmentSlider.setMajorTickSpacing(5);
		alignmentSlider.setMinorTickSpacing(1);
		
		separationSlider = new JSlider(0,10,0);
		Hashtable<Integer, JLabel>Separation = new Hashtable<Integer, JLabel>();
		Separation.put(5, new JLabel("Separation"));
		separationSlider.setLabelTable(Separation);
		separationSlider.setPaintTicks(true);
		separationSlider.setPaintLabels(true);
		separationSlider.setMajorTickSpacing(5);
		separationSlider.setMinorTickSpacing(1);
		
		setupSliders();
	
		// Setting the text of the JLabels 
		boidSpeedLabel = new JLabel("Speed: " + boidSpeedSlider.getValue());
		boidCounter = new JLabel("Boids: " + boids + "     |     ");
		predatorCounter = new JLabel("Predators: " + predators + "     |     ");	
		scoreLabel = new JLabel("Score: " + score + "     |     ");
		
		gameDescription = new JLabel("You have 30 seconds to gain as much score as you can. For each boid that gets eaten, you lose half of your score.\n "
				+ "However, there are score multipliers for the amount of predators and boids you have chosen.");
		
		// adding all components to the frame
		frame.add(lowerPanel, BorderLayout.SOUTH);
		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(canvas, BorderLayout.CENTER);
		
		// adding all elements to the components
		// FlowLayout.LEFT means elements get added from the left hand side, pushing everything to the right
		lowerPanel.add(removePredatorButton, FlowLayout.LEFT);
		lowerPanel.add(removeBoidButton, FlowLayout.LEFT);
		lowerPanel.add(addBoidButton, FlowLayout.LEFT);
		lowerPanel.add(addPredatorButton, FlowLayout.LEFT);
		lowerPanel.add(boidSpeedSlider, FlowLayout.LEFT);
		lowerPanel.add(separationSlider, FlowLayout.LEFT);
		lowerPanel.add(cohesionSlider, FlowLayout.LEFT);
		lowerPanel.add(alignmentSlider, FlowLayout.LEFT);
		lowerPanel.add(rangeSlider, FlowLayout.LEFT);		
		upperPanel.add(boidSpeedLabel, FlowLayout.LEFT);
		upperPanel.add(boidCounter, FlowLayout.LEFT);
		upperPanel.add(predatorCounter, FlowLayout.LEFT);
		upperPanel.add(scoreLabel, FlowLayout.LEFT);
		canvas.add(startGameButton);
		canvas.add(gameDescription);
		
		// making the frame visible
		frame.setVisible(true);
		
		// magic number, could be any value above System.currentTimeInMillis()
		startTime = 1000000000;
		
		
		gameLoop();
		
		// when the game has ended, close the frame 
		frame.dispose();
		
		
		double scoreMultiplier = 0.5;
		
		// the end score is recalculated depending on the predator and boid multipliers
		score = score * (predators * scoreMultiplier);
		score = score - (boids * scoreMultiplier/10);
		
		// empties the Flock 
		entireFlock.emptyFlock();
		
		// displays the final score in the console
		System.out.println("Predators Multiplier: score * 0.5 * (" + predators + " predators)");
		System.out.println("Boids Multiplier: score - (0.5 * (" + boids + " boids))");
		System.out.println("Final score is: " + score);
		
	}
	
	private void gameLoop(){
		
		double deltaTime = 5;
		continueRunning = true;
		
		Boolean toBeRemoved = false;
		int scoreCounter = 0;
		
		while(continueRunning){
			try{
				// if the speed slider is on 0, stop boids from moving by pausing
				if(boidSpeedSlider.getValue() == 0){
					Utils.pause((int)deltaTime);
				}
				
				// synchronized loops ensure there are no errors when accessing every element in quick succession
				// https://stackoverflow.com/questions/1085709/what-does-synchronized-mean
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						// undraws every entity on the screen
						entities.undraw();
						
					}
				}
				
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						// only if the start game button has been pressed
						if(startGame == true){
							// if score counter is a factor of 10
							// in other words, every 10 loops, do this
							if(scoreCounter % 10 == 0){
								// every 10 loops increase the score by 1
								score+= 1;
								scoreLabel.setText("Score: " + score + "     |     ");
							}
							// if the entity is not a predator
							if(!(entities instanceof PredatorBoid)){
								// if the entity has a predator within range
								if(entities.checkForPredator(entireFlock) == true){
									// halve the score
									score /= 2;
									// need to copy the current entity to another variable, the entity needs to be referenced correctly when removing it later
									entityToBeRemoved = entities;
									toBeRemoved = true;
									break;
								}
								
							}
						}
						// setFlockingAttributes checks what other boids are within radius, updates the flock, and applies the relevant 
						// behaviours for cohesion, separation, and alignment
						entities.setFlockingAttributes(entireFlock);
						entities.update(deltaTime);
					}
				}
				
				
				
				synchronized(entireFlock){
					for(Entity entities : entireFlock.getFlock()){
						// if an entity needs to be removed (when a boid gets eaten by a predator)
						if(toBeRemoved == true){
							// deletes the entity
							entireFlock.removeEntity(entityToBeRemoved);
							toBeRemoved = false;
						}
						
						
						entities.draw();
						// empties the radius flock, as boids come in and out of range all the time
						entities.emptyFlock();
						
					}
					
				}
				
				
			} // this catch block stops all errors in the gameloop when any button is being spam clicked
			catch(java.util.ConcurrentModificationException exception){
				
			}
			
			Utils.pause((int)deltaTime);
			
			scoreCounter++;
			
			// making sure that scoreCounter never overflows the integer buffer, resets to 0 once it starts getting high
			if(scoreCounter == 1000000){
				scoreCounter = 0;
			}
			
			// after the start game button is pressed
			if(startGame == true){
				
				
				currentTime = System.currentTimeMillis();
				// constantly checks the time until 30 seconds has passed 
				if(currentTime > startTime + 30000){
					// after 30 seconds has passed, break out of the gameLoop
					return;
				}
			}
			
		
		}
		
	}
	
	public static void main(String[] args){
		new FlockingSim6();
	}

}

//----------------------------------------------------------ALL REFERENCES----------------------------------------------------------------

// 1. Hashtables - https://www.geeksforgeeks.org/hashtable-in-java/

// 2. Synchronized - https://stackoverflow.com/questions/1085709/what-does-synchronized-mean , also introduced in Lab Scripts

// 3. ConcurrentModificationException try/catch block - https://www.baeldung.com/java-concurrentmodificationexception

// 4. Flock.java - Synchronized List - https://www.geeksforgeeks.org/collections-synchronizedlist-method-in-java-with-examples/ , also in lab scripts

// 5. PredatorBoid.java - check if co-ordinates are within circle - https://math.stackexchange.com/questions/198764/how-to-know-if-a-point-is-inside-a-circle

// 6. FlockingBoid.java - calculating angle between two points - https://stackoverflow.com/questions/17296066/angle-between-2-points-with-atan2

// 7. Utils.java - Getting random number between two integers - https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java




