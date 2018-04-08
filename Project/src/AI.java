import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.nio.charset.spi.CharsetProvider;


public class AI{

	protected static final int SIZE = 20;
	
	protected int score = 1000;
	protected int score2 = 0;
	protected int score3 = 0;
	public int aiMoveSet[][];
	protected boolean dead;
	
	protected int a,b,r,l;
	protected double angle;
	protected int x, y;
	protected int num;
	
	protected char directionX = 's';
	protected char directionY = 's';
	protected char direction = 's';
	
	public boolean running;
	public AI(int start_x, int start_y, int num) {
		x = start_x;
		y = start_y;
		this.num = num;
		running = true;
	}
	
	public void calc(int tarX, int tarY) {
		calcA(tarX, tarY);
		calcB(tarX, tarY);
		calcR(tarX, tarY);
		calcL(tarX, tarY);
		calcAngle(tarX, tarY);
		
	}
	private void calcA(int tarX, int tarY) {
		if(y-20 >= 0) {
			if(y-20 == tarY && x==tarX) {
				a=1;
			}
			else {
				a=0;
			}
		}
		else {
			a=-1;
		}
	}
	private void calcB(int tarX, int tarY) {
		if(y+20 < SIZE) {
			if(y+20 == tarY && x==tarX) {
				b=1;
			}
			else {
				b=0;
			}
		}
		else {
			b=-1;
		}
	}
	private void calcL(int tarX, int tarY) {
		if(x-20 >= 0) {
			if(x-20 == tarX && y==tarY) {
				l=1;
			}
			else {
				l=0;
			}
		}
		else {
			l=-1;
		}
	}
	private void calcR(int tarX, int tarY) {
		if(x+20 < SIZE) {
			if(x+20 == tarX && y==tarY) {
				r=1;
			}
			else {
				r=0;
			}
		}
		else {
			r=-1;
		}
	}
	private void calcAngle(int tarX, int tarY) {
		int angleX = tarX-x;
		int angleY = tarY-y;
		//above
		if(angleX == 0 && angleY < 0) {
			angle = 1;
		}
		//topright
		if(angleX > 0 && angleY<0 ) {
			angle = 0.75;
		}
		//right
		if(angleY == 0 && angleX > 0) {
			angle = 0.5;
		}
		//bottomright
		if(angleX > 0 && angleY>0 ) {
			angle = 0.25;
		}
		//below
		if(angleX == 0 && angleY > 0) {
			angle = 0;
		}	
		//bottomleft
		if(angleX <0 && angleY>0 ) {
			angle = -0.25;
		}
		//left
		if(angleY == 0 && angleX < 0) {
			angle = -0.5;
		}
		//topleft
		if(angleX < 0 && angleY<0 ) {
			angle = -0.75;
		}
		angle = angle * 20;

	}
	
	
	public void move() throws InterruptedException {

		if(direction == 'l') {
			x=x-20;
		}
		if(direction == 'r') {
			x=x+20;
		}

		if(direction == 'd' ) {
			y=y+20;
		}
		if(direction == 'u') {
			y=y-20;
		}
		


	}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		
		protected Rectangle getBounds() {
			return new Rectangle(x,y,SIZE,SIZE);
		}
		
		protected void paint(Graphics2D g) {
			g.fillRect(x,y,SIZE,SIZE);
		}
	}
