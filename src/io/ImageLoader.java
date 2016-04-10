package io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import facerecog.FaceImage;

/**
 * Handles loading images from the given folder.
 * @author Najash Najm
 */
public class ImageLoader
{
	/**
	 * Used in determining if a file is an image.
	 */
	private MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
	
	/**
	 * The file chooser used to select the image files.
	 */
	final JFileChooser fileChooser = new JFileChooser("./images/library");
	
	/**
	 * 
	 * The Constructor.
	 */
	public ImageLoader()
	{
		fileTypeMap.addMimeTypes("image png tif jpg jpeg bmp");
		
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter( 
			    "Image files", ImageIO.getReaderFileSuffixes()); //set image filter.
		
		fileChooser.setFileFilter(imageFilter);	
	}
	
	/**
	 * Pop-up the open image file dialog to
	 * prompt the use for a image file.
	 * 
	 * @param dialogTitle set the file chooser title
	 * 
	 * @return file
	 */
	public File showOpenFileDialog(final String dialogTitle) 
	{
		File filePath = null;
		
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setApproveButtonText(dialogTitle);
		
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			filePath = fileChooser.getSelectedFile();
		}
		
		return filePath;	
	}

	/**
	 * Get the image files in a given folder.
	 * @param folder Given folder
	 * @return all the images in a given folder
	 */
	public ArrayList<FaceImage> getImages(final String folder)
	{
		final File dir = new File(folder);

		final ArrayList<FaceImage> images = new ArrayList<FaceImage>();
		
		if (dir.exists() && dir.isDirectory())
		{
			File[] files = dir.listFiles(); //get all the files in the folder
			
			for (File file: files)
			{
				if (isImage(file)) //if it is an image add to the arraylist
				{
					images.add(getImage(file));
				}
			}
		}
		
		return images;
	}
	
	public FaceImage getImage(final File file)
	{
		BufferedImage img = null;
		try 
		{
			img = ImageIO.read(file);
		} catch (IOException e) { }
		
		return new FaceImage(img, file.getName());
	}
	
	/**
	 * Check if a given file is an image.
	 * @param file Given file
	 * @return true if it an image, false otherwise
	 */
	public boolean isImage(final File file)
	{
		String mimetype = fileTypeMap.getContentType(file);
		
        String type = mimetype.split("/")[0];
        
        if(type.equals("image"))
        {
            return true;     
        }
        
        return false;
	}
}
