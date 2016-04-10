package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import facerecog.FaceImage;
import facerecog.FaceRecognition;

public class FaceLibrary extends JScrollPane
{
	private static final int PADDING = 5;
	
	private static final int FONT_SIZE = 14;
	
	private static final int Y_OFFSET = FONT_SIZE * 2 + PADDING;
	
	private final LibraryPane pane;
	
	private final FaceRecognition faceProcessor;
	
	public FaceLibrary(final FaceRecognition faceProcessor, final int paneWidth)
	{
		this.faceProcessor = faceProcessor;
		
		pane = new LibraryPane();
		pane.setPreferredSize(new Dimension(paneWidth, 1000));
		
		setViewportView(pane);	
		
		getVerticalScrollBar().setUnitIncrement(16);
	}
	
	private class LibraryPane extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			final ArrayList<FaceImage> faces = faceProcessor.faces;
			final int totalFaces = faces.size();
			
			if (totalFaces > 0)
			{
				g.setFont(new Font("Times New Roman", Font.BOLD, FONT_SIZE));
				final int faceSize = faces.get(0).getWidth() + PADDING;
				final int facesInARow = getWidth()/faceSize; if (facesInARow <= 0) return;	
				final int columns = (int) Math.ceil(totalFaces/facesInARow) + 1;
				final int paneHeight = (faceSize * columns) + (columns * Y_OFFSET);
				pane.setPreferredSize(new Dimension(getWidth(), paneHeight));
				
				int row = 0, col = 0;
				
				for(FaceImage face: faces)
				{
					drawFace(g, face, col, row++);
					
					if (row >= facesInARow)
					{
						row = 0;
						col++;
					}
				}				
			}
		}
	}
	
	private void drawFace(final Graphics g, FaceImage face,
			  			  final int i, final int j)
	{
		final int xOffset = PADDING * 2;	
		int yOffset = Y_OFFSET * i;	
		final int faceSize = face.getWidth() + PADDING;
		
		yOffset = (i == 0) ? xOffset:yOffset;
		
		final int x = xOffset + faceSize * j;
		final int y = yOffset + faceSize * i;			
		g.drawImage(face, x, y, null);
		
		final String name = face.getName();
		g.setColor(Color.RED);
		
		final java.awt.FontMetrics fontMetrics = g.getFontMetrics();
		final int textWidth = fontMetrics.stringWidth(name);
		
		final int nameX = x + (face.getWidth()/2) - (textWidth/2);
		final int nameY = y + face.getHeight() + PADDING + FONT_SIZE;
		g.drawString(name, nameX, nameY);
	}
}
