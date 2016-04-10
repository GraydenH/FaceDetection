package facerecog;


import java.util.SortedMap;

public class ProcessResult
{
	private static final double MIN_DISTANCE = 2000;
	
	private static final double MAX_DISTANCE = 4200;
	
	public final boolean isNotFace;
	public final boolean isNewFace;
	public final boolean isFace;
	public final boolean isLikeFace;
	
	public ProcessResult(final FaceImage probFace, final FaceRecognition faceProcessor, 
						 final SortedMap<Double, FaceImage> result)
	{
		final FaceSpaceTheta faceSpaceTheta = faceProcessor.getFaceSpaceTheta();
		final double theta = faceProcessor.getThetaDistance(probFace);
		final double minDistance = result.firstKey();
		
		final boolean withinFaceSpace = (theta >= faceSpaceTheta.getMinTheta() &&
		 		 theta <= faceSpaceTheta.getMaxTheta());
		
		System.out.println("\n"+ probFace.getName());
		System.out.println("Theta: " + theta);
		System.out.println("Dista: " + minDistance);
		
		if ((minDistance - MIN_DISTANCE) > MAX_DISTANCE)
		{
			isNotFace = true;
			isLikeFace = false;
			isFace = false;
			isNewFace = false;
		}
		else if (withinFaceSpace) //inside face space
		{
			// not close to any face in the library
			//so, new face
			if (minDistance > MIN_DISTANCE) 
			{
				isNewFace = true;
				isFace = false;
			}
			// close to a face in the library
			//so, a face from the library
			else
			{
				isFace = true;
				isNewFace = false;
			}
			
			isLikeFace = false;
			isNotFace = false;
		}
		else //outside face space
		{
			// not close to any face in the library
			//so, not a face
			if (minDistance > MIN_DISTANCE)
			{
				isNotFace = true;
				isLikeFace = false;
			}
			// close to a face in the library
			//so, looks like a face but not a face
			else
			{
				isLikeFace = true;
				isNotFace = false;
			}
			
			isFace = false;
			isNewFace = false;
		}
	}
}
