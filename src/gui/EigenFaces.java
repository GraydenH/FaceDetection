package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import facerecog.FaceRecognition;

public class EigenFaces extends JScrollPane
{
	private static final int PADDING = 5;
	
	private final LibraryPane pane;
	
	private final FaceRecognition faceProcessor;
	
	public EigenFaces(final FaceRecognition faceProcessor)
	{
		this.faceProcessor = faceProcessor;
		
		pane = new LibraryPane();
		pane.setPreferredSize(new Dimension(1000, 1000));
		
		setViewportView(pane);	
		
		getVerticalScrollBar().setUnitIncrement(16);
	}
	
	private class LibraryPane extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			final ArrayList<BufferedImage> eigenFaces = faceProcessor.eigenFacesImg;
			final int totalFaces = eigenFaces.size();
			
			if (totalFaces > 0)
			{
				BufferedImage face1 = eigenFaces.get(0);
				final int faceSize = face1.getWidth() + PADDING;
				final int facesInARow = getWidth()/faceSize;
				final int columns = (int) Math.ceil(totalFaces/facesInARow) + 1;
				pane.setPreferredSize(new Dimension(getWidth(), faceSize * columns));
				
				int row = 0, col = 0;
				
				for(BufferedImage face: eigenFaces)
				{			
					g.drawImage(face, faceSize * row++, faceSize * col, null);
					
					if (row >= facesInARow)
					{
						row = 0;
						col++;
					}
				}	
			}
		}
	}
}
