
/*
CLASS: AsteroidsGame
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
Original code by Dan Leyzberg and Art Simon
 */
import java.awt.*;

public class Asteroids extends Game {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	
	Asteroid spaceship;
	Asteroid asteroid1;
	Asteroid asteroid2;

	
	static int counter = 0;

	public Asteroids() {
		super("Asteroids!", SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setFocusable(true);
		this.requestFocus();
		
		Point point1 = new Point(50,50);
		Point point2 = new Point(50,75);
		Point point3 = new Point(75,50);
		Point point4 = new Point(100,100);
		Point point5 = new Point(75,100);
		
		
		Point p1 = new Point(30,90);
		Point p2 = new Point(90,90);
		Point p3 = new Point(90,30);
		Point p4 = new Point(30,30);
		
		Point p5 = new Point(30,90);
		Point p6 = new Point(45,90);
		Point p7 = new Point(90,30);
		Point p8 = new Point(20,30);

		Point middle = new Point(400,300);
		Point random = new Point(50,100);
		Point random2 = new Point(50,425);
			
		 
		Point[] pointArr = {point1, point2, point3, point1 };
		Point[] shape1 = {p1, p2, p3, p4, p1};
		Point[] shape2 = {p8, p7, p6, p5, p8};
		
		spaceship = new Asteroid(pointArr, middle, 0);
		asteroid1 = new Asteroid(shape1, random, 0);
		asteroid2 = new Asteroid(shape2, random2, 90);
	}

	public void paint(Graphics brush) {
		
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		counter++;
		brush.setColor(Color.white);
		brush.drawString("Counter is " + counter,10,10);
		
		spaceship.paint(brush, Color.PINK);
		asteroid1.paint(brush, Color.yellow);
		asteroid1.move();
		asteroid2.paint(brush, Color.cyan);
		asteroid2.move();
		
	}

	public static void main (String[] args) {
		Asteroids a = new Asteroids();
		a.repaint();
	}
}