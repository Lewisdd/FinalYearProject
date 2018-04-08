import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Displayer extends JPanel implements Runnable{
	
	Rectangle ai = new Rectangle();
	Rectangle tar = new Rectangle();
	LinkedList<Line> lines = new LinkedList<Line>();
	List<Rectangle> ais;
	int size;
	List<List<int[]>> aiMoves;
	int[][] tarMoves;
	int aiNum1;
	int aiNum2;
	boolean dead1 = false;
	boolean dead2 = false;
	boolean animate1 = false;
	boolean animate2 = false;
	public Displayer(List<List<int[]>> aiMoves2, int[][] tarMoves, int aiNum1, int aiNum2, int size) {
		
		this.aiMoves = aiMoves2;
		this.tarMoves = tarMoves;
		this.aiNum1 = aiNum1;
		this.aiNum2 = aiNum2;
		this.size = size;
		


			for (int j = 0; j < size/20; j++) {
				lines.add(new Line(0,j*20,size,j*20,Color.LIGHT_GRAY));
				lines.add(new Line(j*20,0,j*20,size,Color.LIGHT_GRAY));
			}
			
			lines.add(new Line(0,0,size,0,Color.black));
			lines.add(new Line(0,0,0,size,Color.black));
			lines.add(new Line(size,0,size,size,Color.black));
			lines.add(new Line(0,size,size,size,Color.black));



		
		ais = new ArrayList<Rectangle>();
		System.out.println(aiMoves2.size());
		for(int i = 0; i<2; i++) {
			ais.add(new Rectangle());
			System.out.println("added rect");
		}
		
		System.out.println(aiMoves.size());
		JFrame j = new JFrame();
		j.setBackground(Color.blue);
		j.setSize(size+50,size+50);
		j.add(this);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);	
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		j.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i = 0; i<2; i++) {
			if(i == 0) {
				g.setColor(Color.RED);
			}
			if(i == 1) {
				g.setColor(Color.RED.darker());
			}
			if(i==0 && dead1) {
				g.setColor(Color.BLACK);
			}
			if(i==0 && animate1) {
				g.setColor(Color.yellow);
			}
			if(i==1 && dead2) {
				g.setColor(Color.black);
			}
			if(i==1 && animate2) {
				g.setColor(Color.yellow);
			}

			g.fillRect(ais.get(i).x,ais.get(i).y,20,20);
		}
		g.setColor(Color.BLUE);
		if(animate1||animate2) {
		g.setColor(Color.yellow);
		}
		g.fillRect(tar.x,tar.y,20,20);
		
		for (Line line : lines) {
	        g.setColor(line.color);
	        g.drawLine(line.x1, line.y1, line.x2, line.y2);
		}
	}
	public void run() {
		while(true) {
			int xCoordAi;
			int yCoordAi;
			int xCoordTar;
			int yCoordTar;
			int num = 0;


			
			for(int j = 0; j<2000;j++) {
				
				repaint();

				if(j<aiNum1) {
				xCoordAi = aiMoves.get(0).get(j)[0];
				yCoordAi = aiMoves.get(0).get(j)[1];


				ais.get(0).setLocation(xCoordAi,yCoordAi);
				} else { dead1 = true;}
				if(j<aiNum2) {
					xCoordAi = aiMoves.get(1).get(j)[0];
					yCoordAi = aiMoves.get(1).get(j)[1];


					ais.get(1).setLocation(xCoordAi,yCoordAi);
			} else { dead2 = true;}




			xCoordTar = tarMoves[j][0];
			yCoordTar = tarMoves[j][1];
			tar.setLocation(xCoordTar,yCoordTar);
			
			if(ais.get(0).x == tar.x && ais.get(0).y == tar.y && dead1 == false) {
				animate1 = true;
			}
			if(ais.get(1).x == tar.x && ais.get(1).y == tar.y && dead2 == false) {
				animate2 = true;
			}

			

			if(animate1 && num==0||animate2 && num==0) {
					repaint();
					System.out.println("second");
					try {
						Thread.sleep(1200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}			
			
			repaint();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			animate1 = false;
			animate2 = false;
			num = 0;
			
			if(dead1 && dead2) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			}
			
			dead1 = false;
			dead2 = false;
			animate1 = false;
			animate2 = false;
		}
	}
	
	private static class Line{
	    final int x1; 
	    final int y1;
	    final int x2;
	    final int y2;   
	    final Color color;

	    public Line(int x1, int y1, int x2, int y2, Color color) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
	        this.color = color;
	    }               
	}

}







