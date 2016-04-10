package facerecog;

import java.util.ArrayList;

import org.jblas.DoubleMatrix;

public class CovarianceMatrix 
{		
	/**
	 * 
	 * @param differenceSpace
	 * @return
	 */
	public DoubleMatrix getCovarianceMatrix(final ArrayList<FaceImage> faces) 
	{
		final int totalSamples = faces.size();
		final DoubleMatrix matrix = new DoubleMatrix(totalSamples, totalSamples);
		
		for(int x = 0; x < totalSamples; x++)
		{
			for(int y = 0; y < totalSamples; y++) 
			{
				int sum = 0;
				for(int z = 0; z < Tools.ELEMENTS; z++) 
				{
					sum += faces.get(x).differenceArray[z] * faces.get(y).differenceArray[z];
				}
				
				matrix.put(x, y, sum);
			}
		}

		return matrix;
	}
}
