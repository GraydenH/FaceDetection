package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import facerecog.FaceRecognition;
import facerecog.Tools;

public class AverageFace extends JScrollPane
{
	private final FaceRecognition faceProcessor;
	
	private final LibraryPane pane;
	
	public AverageFace(final FaceRecognition faceProcessor)
	{
		this.faceProcessor = faceProcessor;

		pane = new LibraryPane();
		pane.setPreferredSize(new Dimension(256, 256));
		
		setViewportView(pane);	
		
		getVerticalScrollBar().setUnitIncrement(16);
	}
	
	private class LibraryPane extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			final BufferedImage averageFace = Tools.toImage(faceProcessor.avgFaceData);
			
			if (averageFace != null)
			{
				g.drawImage(averageFace, 0, 0, null);
			}
		}
	}
}
