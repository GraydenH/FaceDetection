package facerecog;

import java.util.ArrayList;

public class Weights
{
	public double[] getWeight(final double[] diffImg, final ArrayList<double[]> eigenFaces)
	{
		int len = diffImg.length;
		int totalFaces = eigenFaces.size();
		
		double[] weights = new double[totalFaces];
		
		for(int i = 0 ; i < totalFaces; i++) 
		{
			double[] eigenFace = eigenFaces.get(i);
			for(int j = 0 ; j < len ; j++)
			{
				weights[i] += diffImg[j] * eigenFace[j];
			}
		}
		return weights;
	}
}
