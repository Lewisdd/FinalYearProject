import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NeuralNetwork {

	int inputN;
	int hiddenN;
	int outputN;
	double weights_ih[][];
	double weights_ho[][];
	double bias_h[][];
	double bias_o[][];
	double[][] inputs;
	double[][] inputsNew;
	double hiddens[][];
	double outputs[][];
	double mutate = 0.4;
	Genome genome;
	List<Genome> genomes;
	List<Genome> curGenomes;
	double curNum = 0;
	public boolean noProg = true;
	int mutatePower = 9;
	int numGen;
	int curGen;
	int genCount;

	public NeuralNetwork(int input, int hidden, int output, int numGen, int genCount) {
		this.inputN = input;
		this.hiddenN = hidden;
		this.outputN = output;
		this.numGen = numGen;
		this.genCount = genCount;

		initGenomes();
		curGenomes = genomes;

	}

	public double[] getOutputs(double[] inputs, int curNum) {

		Genome curGenome = null;

		curGenome = curGenomes.get(curNum);

		inputsNew = Matrix.fromArray(inputs);

		double[][] wih = curGenome.getWih();
		double[][] who = curGenome.getWho();
		double[][] bh = curGenome.getBh();
		double[][] bo = curGenome.getBo();

		hiddens = Matrix.multiplyMatrix(wih, inputsNew);
		hiddens = Matrix.addMatrix(hiddens, bh);
		hiddens = Matrix.sigMatrix(hiddens);
		
		outputs = Matrix.multiplyMatrix(who, hiddens);
		outputs = Matrix.addMatrix(outputs, bo);
		outputs = Matrix.sigMatrix(outputs);
		
		double[] output = (Matrix.toArray(outputs));
		return output;
	}

	public void newGenome(double fitness, int curNum) {

		Genome curGenome = curGenomes.get(curNum);

		curGenome.setFitness(fitness);
		if (fitness >= 5) {
			noProg = false;
		}

		if (curNum == numGen - 1) {

			curNum = 0;
			if (noProg) {
				List<Genome> newGen = new ArrayList<Genome>();
				Genome genNew;
				for (int i = 0; i < numGen; i++) {
					genNew = new Genome(inputN, hiddenN, outputN);
					newGen.add(genNew);
				}

				curGenomes = newGen;
				noProg = true;
			} else {

				Collections.sort(curGenomes, Collections.reverseOrder());
				System.out.println("Best 5 genomes:");
				for (int i = 0; i < 5; i++) {
					System.out.println("Fitness Level = " + curGenomes.get(i).getFit());
				}
				if (curGen == genCount - 2) {
					System.out.println("not evolving");
				} else {

					curGenomes = evolve(curGenomes);

				}

				noProg = true;
			}

		}

	}

	public List<Genome> evolve(List<Genome> gen) {

		for (int k = 0; k < numGen; k++) {

		}

		List<Genome> newGen = new ArrayList<Genome>();

		for (int i = 0; i < numGen / 2; i++) {
			newGen.add(gen.get(i));
		}

		for (int i = 0; i < numGen / 2; i++) {

			int parent1 = ThreadLocalRandom.current().nextInt(0, ((numGen / 2) - 1) + 1);
			int parent2 = ThreadLocalRandom.current().nextInt(0, ((numGen / 2) - 1) + 1);

			newGen.add(crossover(gen.get(parent1), gen.get(parent2)));

		}

		curGen++;

		return newGen;

	}

	public void initGenomes() {

		genomes = new ArrayList<Genome>();

		for (int i = 0; i < numGen; i++) {
			genome = new Genome(inputN, hiddenN, outputN);
			genomes.add(genome);
		}

		System.out.println(genomes.size());


	}

	public Genome crossover(Genome a, Genome b) {
		Genome c = new Genome(inputN, hiddenN, outputN);
		double randNum;
		for (int i = 0; i < hiddenN; i++) {
			for (int j = 0; j < inputN; j++) {
				randNum = randDouble(0, 1);
				if (randNum > 0.5) {
					(c.get(0))[i][j] = mutateG((a.get(0))[i][j]);
				} else {
					(c.get(0))[i][j] = mutateG((b.get(0))[i][j]);
				}
			}
		}
		for (int k = 0; k < outputN; k++) {
			for (int l = 0; l < hiddenN; l++) {
				randNum = randDouble(0, 1);
				if (randNum > 0.5) {
					(c.get(1))[k][l] = mutateG((a.get(1))[k][l]);
				} else {
					(c.get(1))[k][l] = mutateG((b.get(1))[k][l]);
				}
			}
		}
		return c;

	}

	public double mutateG(double weight) {
		double rand = randDouble(-1, 1);
		double r = Math.pow(rand, mutatePower);
		double newWeight = weight + r;
        double randNum = randDouble(0, 1);
        if (randNum < 0.2){
			return newWeight;
            } else {
            return weight;
            }
}
	public int randInt(int min, int max) {
		Random r = new Random();
		return min + (max - min) * r.nextInt();
	}

	public double randDouble(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

}
