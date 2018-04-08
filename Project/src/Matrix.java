import static java.lang.Math.exp;

import java.util.Random;

public class Matrix {
	
	static double[][] multiplyMatrix(double[][] a, double[][] b) {
		int aRows = a.length;
		int aCols = a[0].length;
		int bCols = b[0].length;
		
		double[][] c = new double[aRows][bCols];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bCols; j++) {
				c[i][j] = 0.0;
			}
		}
		
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bCols; j++) {
				for (int k = 0; k< aCols; k++) {
				c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}
	
	static double[][] addMatrix(double[][] a, double[][] n){
		for (int i = 0; i<a.length; i++) {
			for (int j = 0; j<a[0].length; j++) {
				a[i][j] += n[i][j];
			}
		}
		return a;
	}
	
	static double[][] sigMatrix(double[][] a){
		for (int i = 0; i<a.length; i++) {
			for (int j = 0; j<a[0].length; j++) {
				double val = a[i][j];
				val = sigmoid(val);
				a[i][j] = val;
			}
		}
		return a;
		
	}
	    private static double sigmoid(double x) {
	        return 1 / (1 + exp(-x));
	    }
	    
		static double[][] randomise(double[][] a){

			for(int i = 0; i< a.length; i++) {
				for(int j = 0; j< a[0].length;j++) {
					double random = randDouble(-1,1);
					a[i][j] = random;

				}
			}
			return a;
		}
		
		static double[][] setOne(double[][] a){
			for(int i = 0; i< a.length; i++) {
				for(int j = 0; j< a[0].length;j++) {
					a[i][j] = 1;
				}
			}
			return a;
		}
		
		static double[][] setZero(double[][] a){
			for(int i = 0; i< a.length; i++) {
				for(int j = 0; j< a[0].length;j++) {
					a[i][j] = 0;
				}
			}
			return a;
		}
		
		
		static double[][] fromArray(double[] a){
		double[][] b = new double[a.length][1];
		for (int i = 0; i<a.length; i++) {
			b[i][0] = a[i];
		}
		return b;
		}

		
		static double[] toArray(double[][] a){
			double[] b = new double[a.length*a[0].length];
			for(int i = 0; i< a.length; i++) {
				double[] row = a[i];
				for(int j = 0; j< row.length;j++) {
					double n = a[i][j];
					b[i*row.length+j] = n;
				}
			
		}
			return b;
}
	    private static double randDouble(double min, double max) {
	        Random r = new Random();
	        return min + (max - min) * r.nextDouble();
	    }
}
