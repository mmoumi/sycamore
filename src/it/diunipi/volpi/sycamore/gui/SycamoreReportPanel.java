package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;

import java.awt.GridBagLayout;
import org.jdesktop.swingx.JXTable;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * The panel that lets the user manage reports, by viewing them, deleting them on creating
 * statistics on them.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreReportPanel extends SycamoreTitledRoundedBorderPanel
{
	private static final long	serialVersionUID	= -927843615388435222L;
	private JXTable				table_reports		= null;
	private JButton				button_statistics	= null;
	private SycamoreEngine		appEngine			= null;

	/**
	 * Default constructor.
	 */
	public SycamoreReportPanel()
	{
		setPreferredSize(new Dimension(250, 120));
		setMinimumSize(new Dimension(200, 55));
		initialize();
	}

	/**
	 * Init the GUI
	 */
	private void initialize()
	{
		getLabel_title().setText("Reports");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 0.0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0 };
		getPanel_contentContainer().setLayout(gridBagLayout);

		GridBagConstraints gbc_table_reports = new GridBagConstraints();
		gbc_table_reports.insets = new Insets(5, 5, 5, 5);
		gbc_table_reports.fill = GridBagConstraints.BOTH;
		gbc_table_reports.gridx = 0;
		gbc_table_reports.gridy = 0;
		getPanel_contentContainer().add(getTable_reports(), gbc_table_reports);

		GridBagConstraints gbc_button_statistics = new GridBagConstraints();
		gbc_button_statistics.insets = new Insets(5, 5, 10, 7);
		gbc_button_statistics.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_statistics.gridx = 0;
		gbc_button_statistics.gridy = 1;
		getPanel_contentContainer().add(getButton_statistics(), gbc_button_statistics);
	}

	/**
	 * @return the table_reports
	 */
	private JXTable getTable_reports()
	{
		if (table_reports == null)
		{
			table_reports = new JXTable();
		}
		return table_reports;
	}

	/**
	 * @return the button_statistics
	 */
	private JButton getButton_statistics()
	{
		if (button_statistics == null)
		{
			button_statistics = new JButton("Statistics");
		}
		return button_statistics;
	}

	/**
	 * Returns the current app engine
	 * 
	 * @return
	 */
	public SycamoreEngine getAppEngine()
	{
		return appEngine;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreRoundedBorderPanel#setAppEngine(it.diunipi.volpi.sycamore.engine.SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		getButton_statistics().setEnabled(enabled);
		getTable_reports().setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreRoundedBorderPanel#updateGui()
	 */
	@Override
	public void updateGui()
	{

	}

}
