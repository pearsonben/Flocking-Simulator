package tools;

public class Utils {

	public static void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// We are happy with interruptions, so do not report exception
		}
	}
	
	// generates random number between -180 and 180, from below
	// https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
	public static double randomNumberBetween(double min, double max){
		double randomNumber = min + (int)(Math.random() * ((min + max) + 1));
		return randomNumber;

	}
	
	
	
}
