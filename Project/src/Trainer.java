import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Trainer {

	public Target tar;

	// set up neural network
	private int numGen;
	private int genCount;
	private int proxPoints;
	private int eatPoints;
	private final int hiddenN = 5;
	private final int outputN = 4;
	private int inputN;

	private double inputs[];
	private double outputs[] = new double[4];
	public static int curNum = 0;
	public static int curGen = 1;
	private int SIZE;
	private String saveName;

	double fitList[];
	public List<AI> aiList;
	List<NeuralNetwork> nnList;
	int mNum1 = 0;
	int mNum2 = 0;
	int mNumT = 0;
	int aiCoordX;
	int aiCoordY;
	int tarCoordX;
	int tarCoordY;
	int steps = 1000;
	int recordCount = 0;
	int finalnum1 = 0;
	int finalnum2 = 0;
	int finalnumT = 0;
	int angleCheck;
	int distCheck;

	boolean dead1 = false;
	boolean dead2 = false;
	boolean done = false;

	double bestFitness = -1000;
	double curFitness;
	int used = 0;
	char directionTar = 's';
	List<List<int[]>> aiMoves;
	List<int[]> firstAI;
	List<int[]> secondAI;
	List<int[]> bestFirstAI;
	List<int[]> bestSecondAI;
	int[] coords;
	private int tarMoveSet[][];
	private int bestTarMoveSet[][];
	private int aiNum = 0;
	boolean aiRun = true;

	private int aiCount = 2;
	boolean running = true;

	public Trainer(int size, int genCount, int genNum, int proxPoints, int eatPoints, String saveName, int angleCheck,
			int distCheck) throws InterruptedException, IOException {
		this.SIZE = size;
		this.genCount = genCount;
		this.numGen = genNum;
		this.proxPoints = proxPoints;
		this.eatPoints = eatPoints;
		this.saveName = saveName;
		inputN = 5;
		if (angleCheck == 1) {
			inputN++;
		}
		if (distCheck == 1) {
			inputN++;
		}

		inputs = new double[inputN];

		aiMoves = new ArrayList<List<int[]>>();
		fitList = new double[aiCount];

		firstAI = new ArrayList<int[]>();
		secondAI = new ArrayList<int[]>();
		bestFirstAI = new ArrayList<int[]>();
		bestSecondAI = new ArrayList<int[]>();

		tarMoveSet = new int[1000][2];
		bestTarMoveSet = new int[1000][2];

		int randomNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
		int randomNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
		aiList = new ArrayList<AI>();

		nnList = new ArrayList<NeuralNetwork>();
		tar = new Target(randomNumX * 20, randomNumY * 20, SIZE);
		for (int i = 0; i < aiCount; i++) {
			nnList.add(new NeuralNetwork(inputN, hiddenN, outputN, numGen, genCount));
			int aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			int aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			while (aiNumX == randomNumX && aiNumY == randomNumY) {
				aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
				aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			}
			aiList.add(new AI(aiNumX * 20, aiNumY * 20, i));
			System.out.println("added ai");

		}
		while (running) {

			for (int i = 0; i < aiCount; i++) {

				AI curAI = aiList.get(i);
				NeuralNetwork curNN = nnList.get(i);

				if (curAI.getBounds().intersects(tar.getBounds()) && curAI.running == true) {
					refresh(curAI);
					steps--;
				}
				int beforeXD = Math.abs(tar.x - curAI.x);
				int beforeYD = Math.abs(tar.y - curAI.y);

				learn(curAI, curNN, i);

				int afterXD = Math.abs(tar.x - curAI.x);
				int afterYD = Math.abs(tar.y - curAI.y);
				steps--;
				if (beforeXD < afterXD || beforeYD < afterYD) {
					curAI.score2 = curAI.score2 - 2;
				} else {
					curAI.score2++;
				}

			}
			for (int i = 0; i < aiCount; i++) {

				AI curAI = aiList.get(i);
				NeuralNetwork curNN = nnList.get(i);
				if (curAI.getBounds().intersects(tar.getBounds()) && curAI.running == true) {
					refresh(curAI);
					steps--;
				}

				if (steps <= 0 || oob(curAI)) {

					gameOver(curAI, curNN);
					if(done == true) {
						break;
					}
				}
			}
			if(done == true) {
				break;
			}

			int win = -1;
			double distance = 1000;
			for (int i = 0; i < aiList.size(); i++) {
				if (distance(aiList.get(i), tar) < distance && aiList.get(i).running == true) {
					distance = distance(aiList.get(i), tar);
					win = i;
				}
			}
			if(done == true) {
				break;
			}
			proximity(aiList.get(win));
		}
	}

	private boolean oob(AI ai) {
		if (ai.x < 0 || ai.x >= SIZE || ai.y < 0 || ai.y >= SIZE) {
			return true;
		}
		return false;
	}

	private double tarDistance(AI ai) {
		int xdis = 0;
		int ydis = 0;

		AI ai1 = null;
		AI ai2 = null;
		double distance = 0;
		for (int i = 0; i < aiCount; i++) {
			AI tarAI = aiList.get(i);
			if (tarAI.num != ai.num) {
				ai1 = ai;
				ai2 = tarAI;
			}
		}

		xdis = Math.abs(ai1.x - ai2.x);
		ydis = Math.abs(ai1.y - ai2.y);

		distance = Math.sqrt(xdis ^ 2 + ydis ^ 2);

		distance = ((distance) / Math.sqrt(2 * ((SIZE * 20) ^ 2)));
		return distance;

	}

	private void proximity(AI ai) {

		int xdis = 0;
		int ydis = 0;
		int curX = tar.x;
		int curY = tar.y;
		if (curGen == -1) {
			saveTarMove();
		}

		xdis = ai.x - tar.x;
		ydis = ai.y - tar.y;
		int absx = Math.abs(xdis);
		int absy = Math.abs(ydis);
		if (Math.abs(xdis) <= 60 && Math.abs(ydis) <= 60) {

			if (absx <= absy) {
				if (xdis <= 0) {
					directionTar = 'r';
				} else {
					directionTar = 'l';
				}
			} else {
				if (ydis <= 0) {
					directionTar = 'd';
				} else {
					directionTar = 'u';
				}
			}
		} else {
			directionTar = 's';
		}

		tar.move(directionTar, xdis, ydis, ai);

		for (int i = 0; i < aiList.size(); i++) {
			if (tar.x == aiList.get(i).x && tar.y == aiList.get(i).y) {
				tar.x = curX;
				tar.y = curY;
			}
		}
	}

	private double distance(AI ai, Target tar) {
		int x = Math.abs(tar.x - ai.x);
		int y = Math.abs(tar.y - ai.y);
		return Math.sqrt(x ^ 2 + y ^ 2);
	}

	protected void learn(AI ai, NeuralNetwork nn, int num) throws InterruptedException {

		if (curGen == -1) {
			saveMove(ai, num);

		}

		// Get the inputs from the game and the output from the neural network
		ai.calc(tar.x, tar.y);
		inputs[0] = ai.a;
		inputs[1] = ai.b;
		inputs[2] = ai.l;
		inputs[3] = ai.r;
		inputs[4] = ai.angle;
		if (angleCheck == 1) {
			inputs[5] = getAngle(ai);
			if (distCheck == 1) {
				inputs[6] = tarDistance(ai);
			}
		} else {
			if (distCheck == 1) {
				inputs[5] = tarDistance(ai);
			}
		}

		outputs = nn.getOutputs(inputs, curNum);

		// Do an action according to the output value
		double max = -1;
		int ele = -1;
		for (int i = 0; i < outputs.length; i++) {
			if (max < outputs[i]) {
				max = outputs[i];
				ele = i;
			}
		}

		if (ele == 0) {
			ai.direction = 'u';
		}
		if (ele == 1) {
			ai.direction = 'd';
		}
		if (ele == 2) {
			ai.direction = 'l';
		}
		if (ele == 3) {
			ai.direction = 'r';
		}

		ai.move();

		ai.calc(tar.x, tar.y);

	}

	public double getAngle(AI ai) {
		// ai2 is target ai
		// ai1 is current ai
		AI ai1 = null;
		AI ai2 = null;
		double angle = 0;
		for (int i = 0; i < aiCount; i++) {
			AI tarAI = aiList.get(i);
			if (tarAI.num != ai.num) {
				ai1 = ai;
				ai2 = tarAI;
			}
		}

		int angleX = ai2.x - ai1.x;
		int angleY = ai2.y - ai1.y;
		// above
		if (angleX == 0 && angleY < 0) {
			angle = 1;
		}
		// topright
		if (angleX > 0 && angleY < 0) {
			angle = 0.75;
		}
		// right
		if (angleY == 0 && angleX > 0) {
			angle = 0.5;
		}
		// bottomright
		if (angleX > 0 && angleY > 0) {
			angle = 0.25;
		}
		// below
		if (angleX == 0 && angleY > 0) {
			angle = 0;
		}
		// bottomleft
		if (angleX < 0 && angleY > 0) {
			angle = -0.25;
		}
		// left
		if (angleY == 0 && angleX < 0) {
			angle = -0.5;
		}
		// topleft
		if (angleX < 0 && angleY < 0) {
			angle = -0.75;
		}
		return angle;

	}

	public void saveMove(AI ai, int num) {

		aiCoordX = ai.x;
		aiCoordY = ai.y;
		tarCoordX = tar.x;
		tarCoordY = tar.y;
		int curAI = ai.num;
		coords = new int[2];
		coords[0] = aiCoordX;
		coords[1] = aiCoordY;

		if (ai.running == true) {
			if (curAI == 0) {

				firstAI.add(coords);

				mNum1++;
			} else {

				secondAI.add(coords);

				mNum2++;
			}
		}
	}

	public void saveTarMove() {
		tarMoveSet[mNumT][0] = tar.x;
		tarMoveSet[mNumT][1] = tar.y;
		mNumT++;
	}

	public void refresh(AI ai) {
		int randomNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
		int randomNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));

		if (curGen == -1) {
			saveTarMove();
		}

		for (int i = 0; i < aiCount; i++) {
			AI curAI = aiList.get(i);

			if (curGen == -1) {
				saveMove(curAI, i);
			}

			if (curAI.num != ai.num) {
				if (Math.abs(curAI.x - ai.x) <= 60 && Math.abs(curAI.y - ai.y) <= 60) {
					curAI.score2 = curAI.score2 + proxPoints;
					ai.score2 = ai.score2 + proxPoints / 2;
				}
			}

		}

		for (int i = 0; i < aiCount; i++) {
			AI curAI = aiList.get(i);
			int aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			int aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			while (aiNumX == randomNumX && aiNumY == randomNumY) {
				aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
				aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			}
			curAI.x = aiNumX * 20;
			curAI.y = aiNumY * 20;
		}

		tar.x = randomNumX * 20;
		tar.y = randomNumY * 20;

		ai.score2 = ai.score2 + eatPoints;

	}

	public void gameOver(AI ai, NeuralNetwork nn) throws InterruptedException, IOException {

		ai.running = false;
		ai.dead = true;
		double fitness;
		fitness = ai.score2;
		fitList[ai.num] = fitness;
		for (int i = 0; i < aiList.size(); i++) {
			if ((aiList.get(i)).running == true) {
				aiRun = true;
				break;
			} else {
				aiRun = false;
			}
		}

		if (!aiRun) {
			if (curGen == -1) {
				saveTarMove();
				recordCount++;

				for (int i = 0; i < aiList.size(); i++) {
					int j = 0;

					j = j + aiList.get(i).score2;
					curFitness = j / 2;
				}

				System.out.println(curFitness);
				if (curFitness > bestFitness) {
					bestFitness = curFitness;
					bestFirstAI = firstAI;
					bestSecondAI = secondAI;
					bestTarMoveSet = tarMoveSet;
					used = recordCount;
					finalnum1 = mNum1;
					finalnum2 = mNum2;
					finalnumT = mNumT;
				}
				if (recordCount < 5) {
					mNum1 = 0;
					mNum2 = 0;
					mNumT = 0;
					tarMoveSet = new int[1000][2];
					firstAI = new ArrayList<int[]>();
					secondAI = new ArrayList<int[]>();
				} else {
					aiMoves.add(bestFirstAI);
					aiMoves.add(bestSecondAI);
					System.out.println("Best Genome displaying with fitness: " + bestFitness + " with genome: " + used);
					SaveLoad.write(aiMoves, bestTarMoveSet, finalnum1, finalnum2, SIZE, saveName, curFitness);
					SaveLoad.writeGen(saveName);
					(new Thread(new Displayer(aiMoves, bestTarMoveSet, finalnum1, finalnum2, SIZE))).start();
					curGen=1;
					running = false;
					done = true;
					return;
				}
			}

			for (int i = 0; i < aiCount; i++) {
				AI curAI = aiList.get(i);
				NeuralNetwork curNN = nnList.get(i);
				double curFit = fitList[i];
				curNN.newGenome(curFit, curNum);
			}

			curNum++;
			if (curNum == numGen) {

				System.out.println("THIS @@@@@@@ " + curGen + " @@@@@@@ THIS");
				curNum = 0;
				curGen++;
				if (curGen == genCount) {
					curGen = -1;
				}
			}

			int randomNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
			int randomNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));

			for (int i = 0; i < aiCount; i++) {
				AI curAI = aiList.get(i);

				int aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
				int aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
				while (aiNumX == randomNumX && aiNumY == randomNumY) {
					aiNumX = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
					aiNumY = (int) Math.round(randDouble(0, (SIZE / 20) - 1));
				}
				curAI.x = aiNumX * 20;
				curAI.y = aiNumY * 20;

				steps = 1000;
				curAI.score2 = 0;

				curAI.running = true;
			}

			tar.x = randomNumX * 20;
			tar.y = randomNumY * 20;

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
