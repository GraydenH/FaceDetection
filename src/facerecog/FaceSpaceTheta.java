package facerecog;

import java.util.ArrayList;

public class FaceSpaceTheta 
{	
	private double minTheta;
	
	private double maxTheta;
	
	public void computeMinMax(final ArrayList<FaceImage> faces,
			final ArrayList<double[]> eigenFaces, final double[] averageFace)
	{
		final EuclideanDistance distance = new EuclideanDistance();
		final int imageCount = faces.size();
		final EigenFace eigenFace = new EigenFace();
		final Weights weightTool = new Weights();
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (int i = 0; i < imageCount; i++)
		{
			FaceImage face = faces.get(i);
		
			double[] reconToFace = eigenFace.reconstruct(face.weights, eigenFaces, averageFace);			
			final double[] diff = eigenFace.computeDifference(reconToFace, averageFace);		
			final double[] weights = weightTool.getWeight(diff, eigenFaces);
			
			double dist = distance.getEuclideanDistance(face.weights, weights);
			 
			min = Math.min(min, dist);
			max = Math.max(max, dist);
		}
		
		minTheta = min;
		maxTheta = max;
	}

	public double getMinTheta()
	{
		return minTheta;
	}

	public double getMaxTheta()
	{
		return maxTheta - 50;
	}
}
