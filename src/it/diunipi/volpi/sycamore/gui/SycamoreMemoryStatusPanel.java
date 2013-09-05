/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JProgressBar;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author Vale
 * 
 */
public class SycamoreMemoryStatusPanel extends JPanel
{
	private static final long	serialVersionUID	= 3706812556556016445L;
	private JLabel				label_memory;
	private JProgressBar		progressBar_indicator;
	private JButton				button_GC;

	/**
	 * Constructor.
	 */
	public SycamoreMemoryStatusPanel()
	{
		initialize();
	}

	/**
	 * Init GUI
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_label_memory = new GridBagConstraints();
		gbc_label_memory.insets = new Insets(0, 0, 5, 5);
		gbc_label_memory.gridx = 0;
		gbc_label_memory.gridy = 0;
		add(getLabel_memory(), gbc_label_memory);
		GridBagConstraints gbc_progressBar_indicator = new GridBagConstraints();
		gbc_progressBar_indicator.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar_indicator.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar_indicator.gridx = 0;
		gbc_progressBar_indicator.gridy = 1;
		add(getProgressBar_indicator(), gbc_progressBar_indicator);
		GridBagConstraints gbc_button_GC = new GridBagConstraints();
		gbc_button_GC.gridx = 1;
		gbc_button_GC.gridy = 1;
		add(getButton_GC(), gbc_button_GC);

		setupIndicator();
	}

	/**
	 * 
	 */
	private void setupIndicator()
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					Runtime runtime = Runtime.getRuntime();

					long mbTotal = runtime.totalMemory() / (1024 * 1024);
					long mbFree = runtime.freeMemory() / (1024 * 1024);
					long mbUsed = mbTotal - mbFree;

					getLabel_memory().setText("Total memory: " + mbTotal + " MB - Used memory: " + mbUsed + " MB - Free memory: " + mbFree + " MB");

					getProgressBar_indicator().setMaximum((int) mbTotal);
					getProgressBar_indicator().setValue((int) mbUsed);
					
					try
					{
						Thread.sleep(5000);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		thread.start();
	}

	/**
	 * @return
	 */
	private JLabel getLabel_memory()
	{
		if (label_memory == null)
		{
			label_memory = new JLabel("Total memory: 000 MB - Used memory: 000 MB - Free memory: 000 MB");
		}
		return label_memory;
	}

	/**
	 * @return
	 */
	private JProgressBar getProgressBar_indicator()
	{
		if (progressBar_indicator == null)
		{
			progressBar_indicator = new JProgressBar();
		}
		return progressBar_indicator;
	}

	/**
	 * @return
	 */
	private JButton getButton_GC()
	{
		if (button_GC == null)
		{
			button_GC = new JButton("GARBAGE COLLECTOR");
			button_GC.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.gc();
					setupIndicator();
				}
			});
		}
		return button_GC;
	}
}
