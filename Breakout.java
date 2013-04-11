/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.MediaTools;
import acm.util.RandomGenerator;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 404;

	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;

	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;

	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
			* BRICK_SEP)
			/ NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/* Method: run() */
	/** Runs the Breakout program. */

	public void run() {
		createBricks();
		createPaddle();
		setBall();
		addMouseListeners();
		setUpSpeed();
		label = new GLabel ("click to play");
		waitForClick();
		remove(label);
		while (ball.getY() < HEIGHT && countBricks != 0) {
			moveBall();
			pause(7);
			checkForCollision();
		}
	}
	

	/*
	 * create Bricks with different colors
	 */

	private void createBricks() {

		color[0] = Color.RED;
		color[1] = Color.RED;
		color[2] = Color.ORANGE;
		color[3] = Color.ORANGE;
		color[4] = Color.YELLOW;
		color[5] = Color.YELLOW;
		color[6] = Color.GREEN;
		color[7] = Color.GREEN;
		color[8] = Color.CYAN;
		color[9] = Color.CYAN;

		int x, y = BRICK_Y_OFFSET;

		for (int i = 0; i < NBRICK_ROWS; ++i) {
			x = BRICK_SEP;
			for (int j = 0; j < NBRICK_ROWS; ++j) {
				bricks = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				bricks.setFilled(true);
				bricks.setColor(color[i]);
				add(bricks);
				x += BRICK_WIDTH + BRICK_SEP;
			}
			y += BRICK_HEIGHT + BRICK_SEP;
		}
	}

	private void createPaddle() {

		paddle = new GRect(0, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT);
	//	paddle1 = new GRect(0, 10, PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle);
	//	add(paddle1);
	}

	// @Override
	// public void mousePressed(MouseEvent arg0) {
	// // TODO Auto-generated method stub
	// super.mousePressed(arg0);
	// lastX = paddle.getX();
	// }

	@Override
	public void mouseMoved(MouseEvent arg0) {
		super.mouseMoved(arg0);
		lastX = arg0.getX();
		if (arg0.getX() < WIDTH - PADDLE_WIDTH) {
			paddle.setLocation(lastX, getHeight() - PADDLE_Y_OFFSET);
//			if (arg0.getX() < WIDTH - PADDLE_WIDTH) {
//				paddle1.setLocation(lastX, 10);
//			}
		}
	}

	public void setBall() {
		ball = new GOval(getWidth() / 2 - BALL_RADIUS, getHeight() / 2
				- BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		add(ball);
	}

	void setUpSpeed() {
		vy = +1.0;
		vx = rgen.nextDouble(+1.0, +3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}

	void moveBall() {

		ball.move(vx, vy);
		if (ball.getX() < 0) {
			vx = -vx;
		}
		if (ball.getX() > WIDTH - 2 * BALL_RADIUS) {
			vx = -vx;
		}
		if (ball.getY() < 0) {
			vy = -vy;
		}
	}

	private GObject getCollidingObject() {

		if ((getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null)) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		}

		else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2
				* BALL_RADIUS) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2
					* BALL_RADIUS);

		} else if (getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null) {
			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);

		} else if ((getElementAt(ball.getX(), ball.getY()) != null)) {
			return getElementAt(ball.getX(), ball.getY());

		} else {
			return null;
		}
	}

	void checkForCollision() {
		GObject collider = getCollidingObject();
		if (collider == paddle && collider != null) {
			vy = -(vy);
			color1 = rgen.nextColor();
			ball.setColor(color1);
			paddle.setFilled(true);
			paddle.setColor(color1);
			
		} else if (collider != null) {
			vy = -vy;
			remove(collider);
			bounceClip.play();
			countBricks --;
	//		color1=bricks.getFillColor();
	//		paddle.setFilled(true);
	//		paddle.setColor(color1);
			
		}
	}

	GRect bricks, paddle, paddle1;

	GOval ball;

	Color color[] = new Color[NBRICK_ROWS];

	GPoint point;

	GObject object;

	private RandomGenerator rgen = RandomGenerator.getInstance();

	private double vx, vy, lastX;

	private int countBricks = 100;
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	
	Color color1;
	
	private GLabel label;
	
	private JButton but;
	
	public static void main (String [] args) {
		new Breakout().start(args);
	}
	
}
