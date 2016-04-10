package facerecog;

import java.util.ArrayList;

/**
 * Tools used in face recognition.
 * @author Najash Najm
 */
public class AverageFace
{		
	/**
	 * Calculate the average face from the given images of faces.
	 * @param faceSpace given images of faces
	 * @return the average face from the given images of faces
	 */
	public double[] getAverageFace(final ArrayList<FaceImage> faceSpace)
	{
		final double[] avgFace = new double[Tools.ELEMENTS];
		final int totalFaces = faceSpace.size();
		
		for (int i = 0; i < Tools.ELEMENTS; i++)
		{
			double sum = 0;
			
			for (FaceImage face: faceSpace)
			{
				sum += face.array[i];
			}
			
			avgFace[i] = sum/totalFaces;
		}
		
		return avgFace;
	}
	
	
}
