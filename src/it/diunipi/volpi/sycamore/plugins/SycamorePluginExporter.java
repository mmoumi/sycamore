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
 * @author Vale
 * 
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
	 * @param sources
	 * @param destDirectory
	 * @param mode
	 */
	public void exportPlugins(final File[] sources, final File destDirectory, EXPORT_MODE mode)
	{
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

					try
					{
						SycamoreUtil.copyFile(source, dest);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					double value = (double) i / (double) sources.length;
					progressBarWindow.getProgressBar().setValue((int) (value * 100));
				}

				progressBarWindow.getProgressBar().setValue(100);
				progressBarWindow.setVisible(false);

				JOptionPane.showOptionDialog(null, "Plugins successfully exported.", "Exporting OK", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
		};
		new Thread(task).start();
	}
}
