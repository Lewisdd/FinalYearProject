import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome implements Comparable<Genome> {

	int inputN;
	int hiddenN;
	int outputN;
	public Double fits;
	List<double[][]> genome;
	List<List<double[][]>> genomes;
	double weights_ih[][];
	double weights_ho[][];
	double bias_h[][];
	double bias_o[][];
	public double fitness[][];
	
	int mutatePower = 9;
	
	
	public Genome(int inputN, int hiddenN, int outputN) {
		this.inputN = inputN;
		this.hiddenN = hiddenN;
		this.outputN = outputN;
			genome = new ArrayList<double[][]>();
			
			weights_ih = new double[hiddenN][inputN];
			weights_ho = new double[outputN][hiddenN];
			weights_ih = Matrix.randomise(weights_ih);
			weights_ho = Matrix.randomise(weights_ho);
			
			bias_h = new double[hiddenN][1];
			bias_o = new double[outputN][1];
			fitness = new double[1][1];
			
			bias_h = Matrix.setOne(bias_h);
			bias_o = Matrix.setOne(bias_o);

			
			genome.add(weights_ih);
			genome.add(weights_ho);
			genome.add(bias_h);
			genome.add(bias_o);
			
			
	}
	
	public void replace(Genome o) {
		for(int i = 0; i<inputN;i++) {
			for(int j = 0; j<hiddenN; j++) {
				(this.get(0))[i][j] = (o.get(0))[i][j];
			}
		}
		for(int k = 0; k<outputN;k++) {
			for(int l = 0; l<hiddenN; l++) {
				(this.get(1))[k][l] = (o.get(1))[k][l];
			}
		}
		this.setFitness(o.getFit());
	}

	
	

	public void setFitness(double fit) {
		fits = fit;
	}
	public double[][] get(int j) {
		return genome.get(j);
	}
	public int size() {
		return genome.size();
	}
	
	public double[][] getWih(){
		return this.weights_ih;
	}
	public double[][] getWho(){
		return this.weights_ho;
	}
	public double[][] getBh(){
		return this.bias_h;
	}
	public double[][] getBo(){
		return this.bias_o;
	}
	
    public double randDouble(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
    public double randInt(int min, int max) {
        Random r = new Random();
        return min + (max - min) * r.nextInt();
    }
    
    public double getFit() {
    	return this.fits;
    }

	public int compareTo(Genome o) {
		return fits.compareTo(o.fits);
	}
}
