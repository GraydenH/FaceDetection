package facerecog;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jblas.DoubleMatrix;

public class Tools
{
	/**
	 * Total number of color info in a given face image. (256*256)
	 */
	public static final int ELEMENTS = 196608;
	
	/**
	 * Face image width and height.
	 */
	public static final int FACE_SIZE = 256;
	
	public static BufferedImage toImage(final double[] data)
	{
		final BufferedImage image = new BufferedImage(FACE_SIZE, FACE_SIZE, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, FACE_SIZE, FACE_SIZE, data);	
		
		return image;
	}
	
	public static BufferedImage toImage(final DoubleMatrix data)
	{
		final int rows = data.getRows();
		final int cols = data.getColumns();
		
		final BufferedImage image = new BufferedImage(rows, cols, BufferedImage.TYPE_INT_ARGB);
			
		for (int x = 0; x < rows; x++)
		{
			for (int y = 0; y < cols; y++)
			{
				image.setRGB(x, y, (int)data.get(x, y));
			}
		}
		
		return image;
	}
	
	public static double[] extractImage(final DoubleMatrix data, final int column)
	{
		double[] colData = new double[Tools.ELEMENTS];
		
		for (int y = 0; y < Tools.ELEMENTS; y++)
		{
			colData[y] = data.get(y, column);
		}
		
		return colData;
	}
	
	public static double[] extractImage(final double[][] data, final int column)
	{
		double[] colData = new double[Tools.ELEMENTS];
		
		for (int y = 0; y < Tools.ELEMENTS; y++)
		{
			colData[y] = data[y][column];
		}
		
		return colData;
	}
	
	public static double[] getImageArray(final BufferedImage img)
	{
		final double[] imgArray = new double[ELEMENTS];
		
		byte[] data = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		
		for (int i = 0; i < ELEMENTS; i++)
		{
			imgArray[i] = (double)(data[i] & 255); 
		}
		
		return imgArray;
	}

	public static BufferedImage eigenFacetoImage(double[] img)
	{
		double[] data = new double[Tools.ELEMENTS];
		
		for (int i = 0; i < Tools.ELEMENTS; i++)
		{
			data[i] = (img[i] * 10000) - 16740000;
		}
		
		return toImage(data);
	}

	public static void addFaceToLibrary(FaceImage prob)
	{
		try {
		    final File outputfile = new File("./images/library" + prob.getName());
		    ImageIO.write(prob, "jpg", outputfile);
		} catch (IOException e) { }
	}
}
