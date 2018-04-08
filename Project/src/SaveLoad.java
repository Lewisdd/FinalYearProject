import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad{
	int num1;
	int num2;
	
	public static void write(List<List<int[]>> aiMoves2, int[][] tarMoves, int num1, int num2,int size, String name, double fitness) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(name+".txt", "UTF-8");
		List<int[]> ai1 = aiMoves2.get(0);
		List<int[]> ai2 = aiMoves2.get(1);
		int[] ai11;
		int[] ai22;
		
		String ai1move = "";
		String ai2move = "";
		String tarmove = "";
		for(int i = 0; i<ai1.size() ; i++) {
			 ai11 = ai1.get(i);
			ai1move += ai11[0] + "," + ai11[1] + " ";
		}
		
		for(int i = 0; i<ai2.size() ; i++) {
			 ai22 = ai2.get(i);
			ai2move += ai22[0] + "," + ai22[1] + " ";
		}
		
		for(int i = 0; i<tarMoves.length;i++) {
			tarmove += tarMoves[i][0] + "," + tarMoves[i][1] + " ";
		}
		writer.println(ai1move);
		writer.println(ai2move);
		writer.println(tarmove);
		writer.println(num1);
		writer.println(num2);
		writer.println(size);
		writer.println(fitness);
		writer.close();
	}
	
	public static void writeGen(String genName) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter("genNames.txt", true));
			try {
				output.newLine();
				output.write(genName);
			}catch (IOException e) {
				//exception handling left as an exercise for the reader
			} 
		output.close();
		}
	
	
	public static List<String> readGens() throws IOException {
		String line = null;
		List<String> genomes = new ArrayList<String>();
		
		try {
			FileReader fileReader = new FileReader("genNames.txt");
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 0;
			while((line = bufferedReader.readLine()) != null) {
				

				genomes.add(line);
				i++;
			}
		}       
		catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + 
                    "genNames" + "'");                
            }
		return genomes;
	}
			
			
			
	public static void read(String fileName) {
		String line = null;
		int num1 = 0;
		int num2 = 0;
		int size = 0;
		fileName = fileName+".txt";
		List<int[]> ai1 = new ArrayList<int[]>();
		List<int[]> ai2 = new ArrayList<int[]>();
		List<List<int[]>> moves = new ArrayList<List<int[]>>();
		int[][] tarmoves = new int[1000][2];
		int k = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				String[] tokens = line.split(" ");
				if(k == 0) {
				System.out.println("here " + tokens.length);
				for(int i = 0; i < tokens.length; i++) {
					int[] coords = new int[2];
					String[] nums = tokens[i].split(",");
					coords[0] = Integer.parseInt(nums[0]);
					coords[1] = Integer.parseInt(nums[1]);
					
					System.out.println(nums[0]);
					System.out.println(nums[1]);
					ai1.add(coords);
				}
				
				
				}
				if(k==1) {
					for(int i = 0; i < tokens.length; i++) {
						int[] coords = new int[2];
						String[] nums = tokens[i].split(",");
						coords[0] = Integer.parseInt(nums[0]);
						coords[1] = Integer.parseInt(nums[1]);
						
						System.out.println(nums[0]);
						System.out.println(nums[1]);
						ai2.add(coords);
					}
					
					
				}
				if(k==2) {
					for(int i = 0; i < tokens.length; i++) {
						String[] nums = tokens[i].split(",");
						tarmoves[i][0] = Integer.parseInt(nums[0]);
						tarmoves[i][1] = Integer.parseInt(nums[1]);
						
						System.out.println(nums[0]);
						System.out.println(nums[1]);
					}
					
				}
				if(k==3) {
					num1 = Integer.parseInt(tokens[0]);
				}
				if(k==4) {
					num2 = Integer.parseInt(tokens[0]);
				}
				if(k==5){
					size = Integer.parseInt(tokens[0]);
				}
				k++;
			}
			bufferedReader.close();
			moves.add(ai1);
			moves.add(ai2);
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex1) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		new Thread(new Displayer(moves, tarmoves, num1, num2,size)).start();
	}
}