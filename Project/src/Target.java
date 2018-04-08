import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Target extends Thread{

	int size;
	
	
	protected int x, y;
	
	protected char direction = 's';

	int count = 0;
	public int time = 0;
	
	public Target(int start_x, int start_y, int size) {
		x = start_x;
		y = start_y;
		this.size = size;

	}
	
	public void move(char dir, int xdis, int ydis, AI ai) {
		if(count != 5) {
			count++;
		int xnorm = xdis;
		int ynorm = ydis;
		xdis = Math.abs(xnorm);
		ydis = Math.abs(ynorm);
		if(dir == 'r') {

			if(x+20 < size){
				x=x+20;
			} else { 
				if(xnorm <= 0 && ynorm <= 0) {
					move('d', xnorm,ynorm, ai);
				} else {
				if(xdis<ydis) {
					move('l', xnorm,ynorm,ai);
				} else {
				move('u',xnorm,ynorm, ai); }
				
			}
		}
		}
		if(dir == 'l') {

			if(x-20 >= 0) {
				x=x-20;
			} else { 
				if(xnorm <= 0 && ynorm >= 0) {
					move('d', xnorm,ynorm, ai);
				} else {
					if(xnorm >=0 && ynorm <= 0) {
						move('d', xnorm,ynorm,ai);
					} else {
				if(xdis<ydis) {
					move('r',xnorm,ynorm,ai);
				} else {
				move('u',xnorm,ynorm, ai);}
			}
				}
			
		}
		}
		if(dir == 'd' ) {

			if(y+20 < size) {
				y=y+20;
			} else { 
				if(xnorm ==0 && ynorm <=0) {
					move('l', xnorm, ynorm, ai);
				} else {
				if(xdis<ydis) {
					move('r', xnorm,ynorm,ai);
				} else {
					move('u',xnorm,ynorm, ai); }
				}
			}
		
		}
		if(dir == 'u') {
			
			if(y-20 >= 0) {
				y=y-20;
			} else { 
				if(xdis<ydis) {
					move('l',xnorm,ynorm, ai);
				} else {
					move('d',xnorm,ynorm, ai);
				} 
			}
		}
		count = 0;
		} else {
			count = 0;
		}
	}
	
		protected Rectangle getBounds() {
			return new Rectangle(x,y,20,20);
		}
		
		protected void paint(Graphics2D g) {
			g.fillRect(x,y,20,20);
		}
	}
