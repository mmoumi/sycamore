/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins;

import it.diunipi.volpi.sycamore.gui.ProgressBarWindow;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * This class exports installed plugins into jar files saved in a directory of the file system
 * chosen by the callee.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePluginExporter
{
	/**
	 * @author Valerio Volpi - vale.v@me.com
	 * 
	 */
	public static enum EXPORT_MODE
	{
		FILE_BY_FILE, ZIPPED;
	}

	/**
	 * Export the sources plugins into destination
	 * 
	 * @param sources
	 * @param destDirectory
	 * @param mode
	 */
	public void exportPlugins(final File[] sources, final File destDirectory, EXPORT_MODE mode)
	{
		// show progress bar
		final ProgressBarWindow progressBarWindow = new ProgressBarWindow();
		progressBarWindow.setVisible(true);
		progressBarWindow.getProgressBar().setValue(0);

		Runnable task = new Runnable()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < sources.length; i++)
				{
					File source = sources[i];
					File dest = new File(destDirectory.getAbsolutePath() + System.getProperty("file.separator") + source.getName());

					// copy the file
					try
					{
						SycamoreUtil.copyFile(source, dest);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					// update progess bar
					double value = (double) i / (double) sources.length;
					progressBarWindow.getProgressBar().setValue((int) (value * 100));
				}

				// hide progress bar
				progressBarWindow.getProgressBar().setValue(100);
				progressBarWindow.setVisible(false);

				// show confirmation message
				JOptionPane.showOptionDialog(null, "Plugins successfully exported.", "Exporting OK", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
		};
		new Thread(task).start();
	}
}
