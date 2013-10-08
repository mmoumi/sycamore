/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

/**
 * A window that contains a progress bar and a textual description. It is drawed over the
 * application frame to represent operations performing.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class ProgressBarWindow extends JWindow
{
	private static final long	serialVersionUID	= 4353210352410688729L;
	private JLabel				label_title			= null;
	private JProgressBar		progressBar			= null;

	/**
	 * Constructor.
	 */
	public ProgressBarWindow()
	{
		// Set the window's bounds, centering the window
		int width = 400;
		int height = 70;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		initialize();
	}

	/**
	 * Init GUI
	 */
	private void initialize()
	{
		getContentPane().setPreferredSize(new Dimension(400, 70));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_label_title = new GridBagConstraints();
		gbc_label_title.anchor = GridBagConstraints.SOUTH;
		gbc_label_title.insets = new Insets(5, 5, 2, 5);
		gbc_label_title.gridx = 0;
		gbc_label_title.gridy = 0;
		getContentPane().add(getLabel_title(), gbc_label_title);

		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.anchor = GridBagConstraints.NORTH;
		gbc_progressBar.insets = new Insets(2, 5, 5, 5);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 1;
		getContentPane().add(getProgressBar(), gbc_progressBar);

		getContentPane().repaint();
	}

	/**
	 * @return label_title
	 */
	public JLabel getLabel_title()
	{
		if (label_title == null)
		{
			label_title = new JLabel("Exporting plugins...");
		}
		return label_title;
	}

	/**
	 * @return progressBar
	 */
	public JProgressBar getProgressBar()
	{
		if (progressBar == null)
		{
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(false);
		}
		return progressBar;
	}
}
