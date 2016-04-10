package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import facerecog.FaceImage;
import facerecog.FaceRecognition;
import facerecog.ProcessResult;
import facerecog.Tools;

public class ResultsPane extends JScrollPane
{	
	public static final String TITLE = "Results";
	
	private final ProcessResult processResult;
	
	private static final int PADDING = 5;
	
	private static final int TOP_MARGIN = 45;
	
	private static final int FONT_SIZE = 14;
	
	private static final int Y_OFFSET = FONT_SIZE * 2 + PADDING;
	
	private final SortedMap<Double, FaceImage> result;
	
	private final LibraryPane pane;
	
	private final FaceImage prob;
	
	private final FaceRecognition faceProcessor;
	
	private boolean newFaceAdded;
	
	public ResultsPane(final FaceImage prob, final FaceRecognition faceProcessor)
	{
		this.faceProcessor = faceProcessor;
		result = faceProcessor.compare(prob);
		
		processResult = new ProcessResult(prob, faceProcessor, result);
		
		this.prob = prob;
				
		pane = new LibraryPane();
		
		setViewportView(pane);	
		
		getVerticalScrollBar().setUnitIncrement(16);
	}
	
	private class LibraryPane extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			if (prob != null)
			{
				drawProbFace(g);
				
				if (processResult.isNotFace)
				{
					labelProb(g, "Not a face!");
				}
				else if (processResult.isFace)
				{
					labelProb(g, "A face from the library!");
				}
				else if (processResult.isNewFace)
				{
					labelProb(g, "A new face!");
					
					if (!newFaceAdded)
					{
						showResult(g);
						newFaceAdded = true;
						
						final int option = JOptionPane.showConfirmDialog(this, "Add to the library?", "A new face!", 
								JOptionPane.YES_NO_OPTION);
						
						if (option == JOptionPane.YES_OPTION)
						{
							Tools.addFaceToLibrary(prob);
							faceProcessor.initializeAll();
						}
					}
				}
				else if (processResult.isLikeFace)
				{
					labelProb(g, "Looks like a face, but not a face!");
				}
				
				showResult(g);
			}
			
		}

		private void showResult(Graphics g)
		{
			final int totalFaces = result.size();
			
			if (totalFaces > 0)
			{
				g.setFont(new Font("Times New Roman", Font.BOLD, FONT_SIZE));
				final int faceSize = prob.getWidth() + PADDING;
				final int facesInARow = getWidth()/faceSize; if (facesInARow <= 0) return;				
				final int columns = (int) Math.ceil(totalFaces/facesInARow) + 1;
				final int paneHeight = (faceSize * columns) + (columns * Y_OFFSET) + prob.getHeight() + TOP_MARGIN;				
				pane.setPreferredSize(new Dimension(getParent().getWidth(), paneHeight));
				
				int row = 0, col = 0;
				
				for(Entry<Double, FaceImage> entry : result.entrySet())
				{
					drawFace(g, entry, col, row++);
					
					if (row >= facesInARow)
					{
						row = 0;
						col++;
					}
				}
			}
		}

		private void drawFace(final Graphics g, final Entry<Double, FaceImage> entry,
							  final int i, final int j)
		{
			final FaceImage face = entry.getValue();
			
			final int xOffset = PADDING*2;	
			int yOffset = Y_OFFSET * i + prob.getHeight() + TOP_MARGIN;	
			final int faceSize = face.getWidth() + PADDING;
			
			final int x = xOffset + faceSize * j;
			final int y = yOffset + faceSize * i;			
			g.drawImage(face, x, y, null);
			
			final String label = face.getName() + " | " + entry.getKey();			
			g.setColor(Color.RED);
			
			final java.awt.FontMetrics fontMetrics = g.getFontMetrics();
			final int textWidth = fontMetrics.stringWidth(label);
			
			final int nameX = x + (face.getWidth()/2) - (textWidth/2);
			final int nameY = y + face.getHeight() + PADDING + FONT_SIZE;
			g.drawString(label, nameX, nameY);
		}

		private void drawProbFace(Graphics g)
		{
			final int width = prob.getWidth();
			final int centerX = (getWidth()/2) - (width/2);
		
			g.setColor(Color.BLACK);
			g.drawImage(prob, centerX, PADDING, null);
			
			g.drawRect(centerX, PADDING, prob.getWidth(), prob.getHeight());
			
			final String name = prob.getName();			
			g.setColor(Color.RED);
			
			final java.awt.FontMetrics fontMetrics = g.getFontMetrics();
			final int textWidth = fontMetrics.stringWidth(name);
			
			final int nameX = centerX + (width/2) - (textWidth/2);
			final int nameY = PADDING + prob.getHeight() + FONT_SIZE;
			g.drawString(name, nameX, nameY);
		}


		private void labelProb(Graphics g, final String text)
		{
			g.setColor(Color.RED);
			
			final java.awt.FontMetrics fontMetrics = g.getFontMetrics();
			
			final int textWidth = fontMetrics.stringWidth(text);
			final int centerX = (getWidth()/2) - (textWidth/2);
			final int y = prob.getHeight() + FONT_SIZE*2 + PADDING*2;
			g.drawString(text, centerX, y);
		}
	}
}
