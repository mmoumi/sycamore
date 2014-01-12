/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Vale
 * 
 */
public class RobotsDisposedOnPlygonSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= 3539382668957282091L;
	private JPanel				panel_settings		= null;
	private JPanel				panel_contents		= null;
	private JLabel				label_sides			= null;
	private JSpinner			spinner_sides		= null;
	private JLabel				label_radius		= null;
	private JSpinner			spinner_radius		= null;
	private JLabel				label_centerX		= null;
	private JLabel				label_centerY		= null;
	private JSpinner			spinner_centerX		= null;
	private JSpinner			spinner_centerY		= null;

	/**
	 * Constructor
	 */
	public RobotsDisposedOnPlygonSettingsPanel()
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
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.fill = GridBagConstraints.BOTH;
		gbc_panel_settings.gridx = 0;
		gbc_panel_settings.gridy = 0;
		add(getPanel_settings(), gbc_panel_settings);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// Nothing to do
	}

	/**
	 * @return
	 */
	private JPanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new JPanel();
			panel_settings.setBorder(new TitledBorder(null, "Select settings for polygon to draw:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_panel_settings = new GridBagLayout();
			gbl_panel_settings.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_settings.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_settings.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_panel_settings.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_settings.setLayout(gbl_panel_settings);
			GridBagConstraints gbc_panel_contents = new GridBagConstraints();
			gbc_panel_contents.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_contents.gridx = 0;
			gbc_panel_contents.gridy = 0;
			panel_settings.add(getPanel_contents(), gbc_panel_contents);
		}
		return panel_settings;
	}

	/**
	 * @return
	 */
	private JPanel getPanel_contents()
	{
		if (panel_contents == null)
		{
			panel_contents = new JPanel();
			GridBagLayout gbl_panel_contents = new GridBagLayout();
			gbl_panel_contents.columnWidths = new int[]
			{ 0, 0, 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_sides = new GridBagConstraints();
			gbc_label_sides.anchor = GridBagConstraints.EAST;
			gbc_label_sides.insets = new Insets(0, 0, 5, 5);
			gbc_label_sides.gridx = 0;
			gbc_label_sides.gridy = 0;
			panel_contents.add(getLabel_sides(), gbc_label_sides);
			GridBagConstraints gbc_spinner_sides = new GridBagConstraints();
			gbc_spinner_sides.anchor = GridBagConstraints.WEST;
			gbc_spinner_sides.insets = new Insets(0, 0, 5, 5);
			gbc_spinner_sides.gridx = 1;
			gbc_spinner_sides.gridy = 0;
			panel_contents.add(getSpinner_sides(), gbc_spinner_sides);
			GridBagConstraints gbc_label_centerX = new GridBagConstraints();
			gbc_label_centerX.anchor = GridBagConstraints.EAST;
			gbc_label_centerX.insets = new Insets(0, 0, 5, 5);
			gbc_label_centerX.gridx = 2;
			gbc_label_centerX.gridy = 0;
			panel_contents.add(getLabel_centerX(), gbc_label_centerX);
			GridBagConstraints gbc_spinner_centerX = new GridBagConstraints();
			gbc_spinner_centerX.anchor = GridBagConstraints.WEST;
			gbc_spinner_centerX.insets = new Insets(0, 0, 5, 0);
			gbc_spinner_centerX.gridx = 3;
			gbc_spinner_centerX.gridy = 0;
			panel_contents.add(getSpinner_centerX(), gbc_spinner_centerX);
			GridBagConstraints gbc_label_radius = new GridBagConstraints();
			gbc_label_radius.anchor = GridBagConstraints.EAST;
			gbc_label_radius.insets = new Insets(0, 0, 0, 5);
			gbc_label_radius.gridx = 0;
			gbc_label_radius.gridy = 1;
			panel_contents.add(getLabel_radius(), gbc_label_radius);
			GridBagConstraints gbc_spinner_radius = new GridBagConstraints();
			gbc_spinner_radius.anchor = GridBagConstraints.WEST;
			gbc_spinner_radius.insets = new Insets(0, 0, 0, 5);
			gbc_spinner_radius.gridx = 1;
			gbc_spinner_radius.gridy = 1;
			panel_contents.add(getSpinner_radius(), gbc_spinner_radius);
			GridBagConstraints gbc_label_centerY = new GridBagConstraints();
			gbc_label_centerY.anchor = GridBagConstraints.EAST;
			gbc_label_centerY.insets = new Insets(0, 0, 0, 5);
			gbc_label_centerY.gridx = 2;
			gbc_label_centerY.gridy = 1;
			panel_contents.add(getLabel_centerY(), gbc_label_centerY);
			GridBagConstraints gbc_spinner_centerY = new GridBagConstraints();
			gbc_spinner_centerY.anchor = GridBagConstraints.WEST;
			gbc_spinner_centerY.gridx = 3;
			gbc_spinner_centerY.gridy = 1;
			panel_contents.add(getSpinner_centerY(), gbc_spinner_centerY);
		}
		return panel_contents;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_sides()
	{
		if (label_sides == null)
		{
			label_sides = new JLabel("Number of sides:");
		}
		return label_sides;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_sides()
	{
		if (spinner_sides == null)
		{
			spinner_sides = new JSpinner();
			spinner_sides.setMaximumSize(new Dimension(80, 27));
			spinner_sides.setMinimumSize(new Dimension(80, 27));
			spinner_sides.setPreferredSize(new Dimension(80, 27));
			spinner_sides.setModel(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));
			spinner_sides.setValue(RobotsDisposedOnPolygon.getSides());
			spinner_sides.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					RobotsDisposedOnPolygon.setSides((Integer) spinner_sides.getValue());
				}
			});
		}
		return spinner_sides;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_radius()
	{
		if (label_radius == null)
		{
			label_radius = new JLabel("Radius:");
		}
		return label_radius;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_radius()
	{
		if (spinner_radius == null)
		{
			spinner_radius = new JSpinner();
			spinner_radius.setMaximumSize(new Dimension(80, 27));
			spinner_radius.setMinimumSize(new Dimension(80, 27));
			spinner_radius.setPreferredSize(new Dimension(80, 27));
			spinner_radius.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
			spinner_radius.setValue(RobotsDisposedOnPolygon.getRadius());
			spinner_radius.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					Integer value = (Integer) spinner_radius.getValue();
					RobotsDisposedOnPolygon.setRadius(value);
				}
			});
		}
		return spinner_radius;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_centerX()
	{
		if (label_centerX == null)
		{
			label_centerX = new JLabel("Center x:");
		}
		return label_centerX;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_centerY()
	{
		if (label_centerY == null)
		{
			label_centerY = new JLabel("Center y:");
		}
		return label_centerY;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_centerX()
	{
		if (spinner_centerX == null)
		{
			spinner_centerX = new JSpinner();
			spinner_centerX.setMaximumSize(new Dimension(80, 27));
			spinner_centerX.setMinimumSize(new Dimension(80, 27));
			spinner_centerX.setPreferredSize(new Dimension(80, 27));
			spinner_centerX.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_centerX.setValue(RobotsDisposedOnPolygon.getXCenter());
			spinner_centerX.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					RobotsDisposedOnPolygon.setXCenter((Float) spinner_centerX.getValue());
				}
			});
		}
		return spinner_centerX;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_centerY()
	{
		if (spinner_centerY == null)
		{
			spinner_centerY = new JSpinner();
			spinner_centerY.setMaximumSize(new Dimension(80, 27));
			spinner_centerY.setMinimumSize(new Dimension(80, 27));
			spinner_centerY.setPreferredSize(new Dimension(80, 27));
			spinner_centerY.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_centerY.setValue(RobotsDisposedOnPolygon.getYCenter());
			spinner_centerY.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					RobotsDisposedOnPolygon.setYCenter((Float) spinner_centerY.getValue());
				}
			});
		}
		return spinner_centerY;
	}
}
