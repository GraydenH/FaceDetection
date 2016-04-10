package facerecog;

import java.util.ArrayList;

import org.jblas.DoubleMatrix;

public class EigenFace
{
	private static final int EIGEN_DIFF_MIN = 80000000;
	
	public ArrayList<double[]> getEigenFaces(final ArrayList<FaceImage> faces, 
			final DoubleMatrix eigenVectors, final DoubleMatrix eigenValues) 
	{
		final int imageCount = eigenVectors.getRows();
		final ArrayList<double[]> eigenFaces = new ArrayList<double[]>();
		
		int curEigenVal = 0;
		int eigenValX = 0, eigenValY = 0;
		
		for(int i = 0 ; i < imageCount ; i++)
		{
			final double[] eigenFace = new double[Tools.ELEMENTS];
			curEigenVal = (int) eigenValues.get(eigenValX++, eigenValY++);
			
			if (curEigenVal < EIGEN_DIFF_MIN)
			{
				continue;
			}
			
			double sumSquare = 0;
			for(int j = 0 ; j < Tools.ELEMENTS ; j++)
			{
				for(int k = 0 ; k < imageCount ; k++) 
				{
					eigenFace[j] += faces.get(k).differenceArray[j] * eigenVectors.get(k, i);
				}
				
				sumSquare += (eigenFace[j] * eigenFace[j]); 
			}
			
			double norm = Math.sqrt(sumSquare);
			
			for(int j = 0 ; j < Tools.ELEMENTS ; j++) 
			{
				eigenFace[j] /= norm;
			}
			
			eigenFaces.add(eigenFace);
		}

		return eigenFaces;
	}
	
	public double[] reconstructFromFace(final double[] face, 
			ArrayList<double[]> eigenFaces, double[] avgFaceData) 
	{		
		final Weights weightTool = new Weights();
		final double[] diff = computeDifference(face, avgFaceData);
		
		final double[] weights = weightTool.getWeight(diff, eigenFaces);
		
		return reconstruct(weights, eigenFaces, avgFaceData);
	}
	
	public double[] computeDifference(final double[] face, double[] avgFaceData) 
	{
		double[] differenceArray = new double[face.length];
		
		for(int i = 0 ; i < face.length ; i++) 
		{
			final double difference = face[i] - avgFaceData[i];
			differenceArray[i] = difference;		
		}
		
		return differenceArray;
	}
	
	public double[] reconstruct(double[] weight, ArrayList<double[]> eigenFaces, double[] avgFaceData) 
	{
		double[] img = avgFaceData.clone();
		int imageCount = eigenFaces.size();

		for (int i = 0; i < imageCount; i++)
		{
			double[] face = eigenFaces.get(i);
			
			for (int j = 0; j < Tools.ELEMENTS; j++)
			{
				img[j] += (weight[i] * face[j]);
			}
		}
		
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		
		for(int i = 0 ; i < img.length ; i++)
		{
			min = Math.min(min, img[i]);
			max = Math.max(max, img[i]);
		}

		double[] normalizedReconstructedPixels = new double[Tools.ELEMENTS];
		
		for(int i = 0 ; i < img.length ; i++) 
		{
			normalizedReconstructedPixels[i] = (255.0 * (img[i] - min)) / (max - min);
		}

		return normalizedReconstructedPixels;
	}
}
