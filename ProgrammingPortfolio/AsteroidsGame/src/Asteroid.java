import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Zaine
 *
 */

public class Asteroid extends Polygon{

	public Asteroid(Point[] shape, Point position, double rotation) {
		super(shape, position, rotation);
	}

	@Override
	public void paint(Graphics brush, Color color) {
	brush.setColor(color);
	
	Point[] pointers = getPoints();
	
	int[] x = new int[pointers.length];
	int[] y = new int[pointers.length];
	
	for(int i =0; i < pointers.length; i++) {
		
		x[i]=(int)pointers[i].x;
		y[i]=(int)pointers[i].y;
	}
	brush.drawPolygon(x,y,pointers.length);
	
	}
	
	
	@Override
	public void move() {
	super.position.x++;	
	}
	
	
	
	
	
} ///end of class
