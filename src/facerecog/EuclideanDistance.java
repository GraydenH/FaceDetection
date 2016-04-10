package facerecog;

public class EuclideanDistance 
{
	public double getEuclideanDistance(double[] compare, double[] to)
	{
		double distance = 0;
		
		for(int i = 0 ; i < to.length ; i++) 
		{
			double diff = to[i] - compare[i];
			
			distance += diff * diff;
		}
		
		return Math.sqrt(distance / to.length);
	}
	
	public double[] getDifference(final double[] faceData, double[] avgFaceData) 
	{
		double[] differenceArray = new double[Tools.ELEMENTS];
		
		for(int i = 0 ; i < Tools.ELEMENTS ; i++) 
		{
			final double difference = faceData[i] - avgFaceData[i];
			differenceArray[i] = difference;		
		}
		
		return differenceArray;
	}
}
