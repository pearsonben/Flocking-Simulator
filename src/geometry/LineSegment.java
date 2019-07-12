package geometry;
import geometry.CartesianCoordinate;
import java.lang.Math;

public class LineSegment {
	private CartesianCoordinate startPoint;
	private CartesianCoordinate endPoint;
	
	public LineSegment(CartesianCoordinate start, CartesianCoordinate end){
		startPoint = start;
		endPoint = end;
	}

	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}

	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}

	public String toString(){
		return "(" + startPoint + "," + endPoint + ")";
	}
	public double length(){
		double x = startPoint.getX() - endPoint.getX();
		double y = startPoint.getY() - endPoint.getY();
		
		double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return length;
	}
}
