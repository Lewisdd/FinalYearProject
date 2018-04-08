import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;


public class Tests {
	@Test
	public void setZeroOfMatrixShouldReturnZero() {
	Matrix matrix = new Matrix();
	double[][] test = new double[2][1];
	
	double[][] multi = new double[][]{
		  {0},
		  {0}
			};
	 Assert.assertEquals(multi, Matrix.setZero(test));
	 
}
	
	@Test
	public void setOneOfMatrixShouldReturnOne() {
	Matrix matrix = new Matrix();
	double[][] test = new double[2][1];
	
	double[][] multi = new double[][]{
		  {1},
		  {1}
			};
	 Assert.assertEquals(multi, Matrix.setOne(test));
	 
	 
}
	@Test
	public void multiplyMatrixShouldReturnMultiply() {
	Matrix matrix = new Matrix();
	double[][] multi1 = new double[][]{
		  {2,2},
			};
	
	double[][] multi2 = new double[][]{
		  {3},
		  {3}
			};
			
	double[][] multi3 = new double[][]{
		 {12},
			};

	 Assert.assertEquals(multi3, Matrix.multiplyMatrix(multi1,multi2));
	 
	}
	
	@Test
	public void AddMatrixShouldReturnAdd() {
	Matrix matrix = new Matrix();
	double[][] multi1 = new double[][]{
		  {2,2},
			};
	
	double[][] multi2 = new double[][]{
		  {3,3}
			};
			
	double[][] multi3 = new double[][]{
		 {5,5}
			};

	 Assert.assertEquals(multi3, Matrix.addMatrix(multi2,multi1));
	 
}
}