package facerecog;

import io.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jblas.DoubleMatrix;
import org.jblas.Eigen;

public class FaceRecognition
{
	public ArrayList<FaceImage> faces;
	public double[] avgFaceData;
	public final ArrayList<double[]> eigenFaces = new ArrayList<double[]>();
	
	private final ImageLoader loader = new ImageLoader();;
	private final FaceSpaceTheta faceSpaceTheta = new FaceSpaceTheta();
	private final EigenFace eigenFace = new EigenFace();
	private final EuclideanDistance distance = new EuclideanDistance();
	private final Weights weightTool = new Weights();
	public ArrayList<BufferedImage> eigenFacesImg = new ArrayList<BufferedImage>();
	
	public FaceRecognition()
	{
		initializeAll();
	}
	
	public void initializeAll()
	{
		final CovarianceMatrix covariance = new CovarianceMatrix();		
		final AverageFace average = new AverageFace();		
		
		faces = loader.getImages("./images/library");
		
		avgFaceData = average.getAverageFace(faces);
		
		for (FaceImage face: faces)
		{
			face.computeDifference(avgFaceData);
		}
		
		final DoubleMatrix covariMatrix = covariance.getCovarianceMatrix(faces);	
		final DoubleMatrix[] eigen = Eigen.symmetricEigenvectors(covariMatrix);
		
		eigenFaces.clear();
		eigenFaces.addAll(eigenFace.getEigenFaces(faces, eigen[0], eigen[1]));
		
		for (FaceImage face: faces)
		{
			face.computeWeights(eigenFaces);
		}
		
		faceSpaceTheta.computeMinMax(faces, eigenFaces, avgFaceData);
		
		eigenFacesImg.clear();
		for(double[] faceData: eigenFaces)
		{
			final BufferedImage face = Tools.eigenFacetoImage(faceData);	
			this.eigenFacesImg.add(face);
		}
		
		System.out.println("Min Theta: " + faceSpaceTheta.getMinTheta());
		System.out.println("Max Theta: " + faceSpaceTheta.getMaxTheta());
	}
	
	public FaceSpaceTheta getFaceSpaceTheta()
	{
		return faceSpaceTheta;
	}
	
	public SortedMap<Double, FaceImage> compare(final FaceImage probFace) 
	{
		final SortedMap<Double, FaceImage> result = new TreeMap<Double, FaceImage>();
		
		final double[] prob = Tools.getImageArray(probFace);	
		final double[] diff = eigenFace.computeDifference(prob, avgFaceData);		
		final double[] weights = weightTool.getWeight(diff, eigenFaces);
		
		for (FaceImage face: faces)
		{
			double dist = distance.getEuclideanDistance(weights, face.weights);
			result.put(dist, face);			
		}

		return result;
	}
	
	public double getThetaDistance(final FaceImage probFace)
	{
		final double[] prob = Tools.getImageArray(probFace);
		
		final double[] diffProb = eigenFace.computeDifference(prob, avgFaceData);		
		final double[] weightsProb = weightTool.getWeight(diffProb, eigenFaces);
		
		final double[] reconstructed = eigenFace.reconstructFromFace(prob, eigenFaces, avgFaceData);				
		final double[] diff = eigenFace.computeDifference(reconstructed, avgFaceData);		
		final double[] weights = weightTool.getWeight(diff, eigenFaces);
		
		return distance.getEuclideanDistance(weightsProb, weights);
	}
}
