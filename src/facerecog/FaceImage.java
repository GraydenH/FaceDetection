package facerecog;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

public class FaceImage extends BufferedImage
{
	private String name;

	public double[] array;
	
	public double[] differenceArray;
	
	public double[] weights;
		
	private double distance;
	
	public FaceImage(final BufferedImage img, final String name)
	{
		super(img.getWidth(), img.getHeight(), TYPE_3BYTE_BGR);
		
		getGraphics().drawImage(img, 0, 0, null);
				
		this.name = name;
		this.array = toArray();
	}

	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public double[] toArray()
	{
		byte[] data = ((DataBufferByte)getRaster().getDataBuffer()).getData();
		final int len = data.length;
		array = new double[len];
		
		for (int i = 0; i < len; i++)
		{
			array[i] = (double)(data[i] & 255); 
		}
		
		return array;
	}

	public void computeDifference(double[] avgFaceData) 
	{
		differenceArray = new double[Tools.ELEMENTS];
		
		for(int i = 0 ; i < Tools.ELEMENTS ; i++) 
		{
			final double difference = array[i] - avgFaceData[i];
			differenceArray[i] = difference;			
		}
	}

	public void computeWeights(ArrayList<double[]> eigenFaces)
	{
		final Weights weightTool = new Weights();
		
		weights = weightTool.getWeight(differenceArray, eigenFaces);
	}
	
	public double getDistance() 
	{
		return distance;
	}

	public void setDistance(final double distance)
	{
		this.distance = distance;
	}
	
	
}
