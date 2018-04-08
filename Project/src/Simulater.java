import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Simulater extends JFrame{
	
	private static int ai[][];
	static int[] coords;
	static List<int[]> ai1;
	static List<int[]> ai2;
	static List<List<int[]>> moves;
	private static int tar[][];
    private JLabel sizeLabel;
    private JLabel genCountLabel;
    private JLabel genNumLabel;
    private JLabel proxPointsLabel;
    private JLabel eatPointsLabel;
    private JLabel genPicker;
    private JLabel seperator;
    private JLabel name;
    private JLabel dist;
    private JLabel angle;
    private JButton nnButton;
    private JButton learnButton;
    private JTextField textSize;
    private JTextField textGenCount;
    private JTextField textGenNum;
    private JTextField textProxPoints;
    private JTextField textEatPoints;
    private JTextField textName;
    private JComboBox genList;
    private JCheckBox angleCheck;
    private JCheckBox distCheck;
    int size;
    int genCount;
    int genNum;
    int proxPoints;
    int eatPoints;
    int angleNum = 0;
    int distNum = 0;
    int numGen;
    String saveName;
    String fileName;
    String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
    
    public Simulater() throws IOException {
    	File f = new File("genNames.txt");
    	if(!f.exists()) { 
    	PrintWriter writer = new PrintWriter("genNames.txt", "UTF-8");
    	writer.print("Choose a Genome");
    	writer.close();
    	}
    	
    	textSize = new JTextField();
    	textGenCount = new JTextField();
    	textGenNum = new JTextField();
    	textProxPoints = new JTextField();
    	textEatPoints = new JTextField();
    	textName = new JTextField();
    	
    	angleCheck = new JCheckBox();
    	distCheck = new JCheckBox();
    	
    	sizeLabel = new JLabel("Size of grid x by x:      ");
    	genCountLabel = new JLabel("  Number of Generations:");
    	genNumLabel = new JLabel("  Number of Genomes:");
    	proxPointsLabel = new JLabel("  Points for Proximity:");
    	eatPointsLabel = new JLabel("  Points for Eating:");
    	genPicker = new JLabel("Choose a genome:");
    	seperator = new JLabel("___________________");
    	name = new JLabel("Choose a save name");
    	angle = new JLabel("Check AI Angle");
    	dist = new JLabel("Check AI Distance");

    	
    	setLayout(new FlowLayout());
    	add(sizeLabel);
    	add(textSize);
    	textSize.setPreferredSize( new Dimension( 80, 24 ) );
    	add(genCountLabel);
       	add(textGenCount);
    	textGenCount.setPreferredSize( new Dimension( 80, 24 ) );
    	add(genNumLabel);
       	add(textGenNum);
    	textGenNum.setPreferredSize( new Dimension( 80, 24 ) );
    	add(proxPointsLabel);
       	add(textProxPoints);
    	textProxPoints.setPreferredSize( new Dimension( 80, 24 ) );
    	add(eatPointsLabel);
       	add(textEatPoints);
    	textEatPoints.setPreferredSize( new Dimension( 80, 24 ) );
    	add(name);
    	add(textName);
    	textName.setPreferredSize( new Dimension( 80, 24));
    	add(angle);
    	add(angleCheck);
    	add(dist);
    	add(distCheck);
        learnButton = new JButton("Train new Network");
        add(learnButton);
    	
        add(seperator);
    	List<String> genNames = SaveLoad.readGens();
    	String[] genNamesArray = new String[genNames.size()];
    	for(int i = 0; i < genNames.size();i++) {
    		genNamesArray[i] = genNames.get(i);
    	}
    	add(genPicker);
    	genList = new JComboBox(genNamesArray);
    	genList.setSelectedIndex(0);
    	genList.setPreferredSize(new Dimension( 150, 24 ));
    	add(genList);

    	fileName = genNamesArray[0];
        nnButton = new JButton("Load Neural Network");
        add(nnButton);
        

    	event1 e1 = new event1();
    	genList.addActionListener(e1);
        event e = new event();
        nnButton.addActionListener(e);
        
        event2 ev = new event2();
        learnButton.addActionListener(ev);
    
    }

    public class event implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(fileName.equals("Choose a Genome")) {
				JOptionPane.showMessageDialog(null, "Please choose a Genome", "Error",
                        JOptionPane.ERROR_MESSAGE);
			}else {
			SaveLoad.read(fileName);
			} 
		}
    }
    public class event2 implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			try {
				if(angleCheck.isSelected()) {
					angleNum = 1;
				}
				if(distCheck.isSelected()) {
					distNum = 1;
				}
				try {
				 size = Integer.parseInt(textSize.getText())*20;
				 genCount = Integer.parseInt(textGenCount.getText());
				 numGen = Integer.parseInt(textGenNum.getText());
				 proxPoints = Integer.parseInt(textProxPoints.getText());
				 eatPoints = Integer.parseInt(textEatPoints.getText());
				 saveName = textName.getText();
				 

				//	 int add = (i+1)*100;
				//	 String newSave = saveName+i;
					 System.out.println("hit");
					 new Trainer(size, genCount, numGen, proxPoints, eatPoints, saveName, angleNum, distNum);
				 
				
				}catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Please input correct values", "Error",
	                        JOptionPane.ERROR_MESSAGE);
				}
			} catch(Exception e) {

			}
		}
    }

    public class event1 implements ActionListener{
		public void actionPerformed(ActionEvent e1) {
			String selectedIndex = (String) genList.getSelectedItem();
			fileName = selectedIndex;
			System.out.println(fileName);
		}
    }
    public static void main(String[] args) throws InterruptedException, IOException {

        
        Simulater frame = new Simulater();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(180,600);
        frame.setVisible(true);
        frame.setTitle("Neural Network Evolution");

        



    }
    

}
    
  
