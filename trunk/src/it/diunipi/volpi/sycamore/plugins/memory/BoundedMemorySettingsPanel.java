/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Vale
 * 
 */
public class BoundedMemorySettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= -7346984457971790162L;

	private JLabel				label_memorySize	= null;
	private JPanel				panel_size			= null;
	private JSpinner			spinner_memorySize;

	/**
	 * Default constructor
	 */
	public BoundedMemorySettingsPanel()
	{
		initialize();
	}

	/**
	 * Init Gui
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel_size = new GridBagConstraints();
		gbc_panel_size.insets = new Insets(2, 2, 2, 2);
		gbc_panel_size.fill = GridBagConstraints.BOTH;
		gbc_panel_size.gridx = 0;
		gbc_panel_size.gridy = 0;
		add(getPanel_size(), gbc_panel_size);
	}

	/**
	 * @return
	 */
	private JLabel getLabel_memorySize()
	{
		if (label_memorySize == null)
		{
			label_memorySize = new JLabel("Memory size (number of steps in the past):");
			label_memorySize.setFont(label_memorySize.getFont().deriveFont(label_memorySize.getFont().getStyle() | Font.BOLD));
		}
		return label_memorySize;
	}
	

	/**
	 * @return
	 */
	private JSpinner getSpinner_memorySize()
	{
		if (spinner_memorySize == null)
		{
			spinner_memorySize = new JSpinner();
			spinner_memorySize.setMaximumSize(new Dimension(100, 27));
			spinner_memorySize.setMinimumSize(new Dimension(100, 27));
			spinner_memorySize.setPreferredSize(new Dimension(100, 27));
			spinner_memorySize.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
			spinner_memorySize.setValue(BoundedMemory.getMemorySize());
			spinner_memorySize.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					BoundedMemory.setMemorySize((Integer) spinner_memorySize.getValue());
				}
			});
		}
		return spinner_memorySize;
	}

	/**
	 * @return
	 */
	private JPanel getPanel_size()
	{
		if (panel_size == null)
		{
			panel_size = new JPanel();
			panel_size.setBorder(BorderFactory.createTitledBorder("Memory size settings"));

			GridBagLayout gbl_panel_size = new GridBagLayout();
			gbl_panel_size.columnWidths = new int[]
			{ 98, 0, 0 };
			gbl_panel_size.rowHeights = new int[]
			{ 16, 0 };
			gbl_panel_size.columnWeights = new double[]
			{ 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_size.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_size.setLayout(gbl_panel_size);
			GridBagConstraints gbc_label_memorySize = new GridBagConstraints();
			gbc_label_memorySize.anchor = GridBagConstraints.EAST;
			gbc_label_memorySize.insets = new Insets(10, 2, 10, 2);
			gbc_label_memorySize.gridx = 0;
			gbc_label_memorySize.gridy = 0;
			panel_size.add(getLabel_memorySize(), gbc_label_memorySize);
			GridBagConstraints gbc_spinner_memorySize = new GridBagConstraints();
			gbc_spinner_memorySize.insets = new Insets(10, 2, 10, 2);
			gbc_spinner_memorySize.anchor = GridBagConstraints.WEST;
			gbc_spinner_memorySize.gridx = 1;
			gbc_spinner_memorySize.gridy = 0;
			panel_size.add(getSpinner_memorySize(), gbc_spinner_memorySize);
		}
		return panel_size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine
	 * .SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		// Nothing to do
	}
}
