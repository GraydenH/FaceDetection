package gui.menu;

import facerecog.FaceImage;
import facerecog.FaceRecognition;
import gui.ResultsPane;
import io.ImageLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

/**
 * The main frame menu bar.
 *
 * @author Najash Najimudeen
 */
public class MainMenu extends JMenuBar
{
	private final FaceRecognition faceProcessor;
	
	private final JTabbedPane tabbedPane;
	
	private ImageLoader imageLoader = new ImageLoader();
	
	/**
	 * The constructor.
	 */
	public MainMenu(final FaceRecognition faceProcessor, final JTabbedPane tabbedPane) 
	{
		if (faceProcessor == null)
		{
			throw new NullPointerException("Image panel cannot be null.");
		}
		
		this.faceProcessor = faceProcessor;
		this.tabbedPane = tabbedPane;
		
		createFileMenu();
	}

	/**
	 * File menu.
	 */
	private void createFileMenu()
	{
		final JMenu fileMenu = new JMenu("File", false);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		addMenuItemOpen(fileMenu);
		
		add(fileMenu);
	}

	/**
	 * Add the "Select Image" menu item.
	 */
	private void addMenuItemOpen(final JMenu fileMenu) 
	{
		final JMenuItem itemOpen = new JMenuItem("Select Image");
		itemOpen.setMnemonic(KeyEvent.VK_O);
		
		itemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				handleImageSelection();
			}
		});
		
		fileMenu.add(itemOpen);
	}

	protected void handleImageSelection()
	{
		final File file = imageLoader.showOpenFileDialog("Select an image to recognize");
		
		if (file != null)
		{
			final BufferedImage faceImg = imageLoader.getImage(file);
			final FaceImage probFace = new FaceImage(faceImg, file.getName());
			final ResultsPane resultsPane = new ResultsPane(probFace, faceProcessor);
			
			int lastTab = tabbedPane.getTabCount() - 1;
			final String title = tabbedPane.getTitleAt(lastTab);
			
			if (title.equals(ResultsPane.TITLE))
			{
				tabbedPane.remove(lastTab);
			}
			
			tabbedPane.add(ResultsPane.TITLE, resultsPane);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		}
	}
	
}
