/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The preferences panel for the whole application
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePrefsPane extends JPanel
{
	private static final long	serialVersionUID					= -1683437960177568854L;
	private JPanel				panel_system						= null;
	private JPanel				panel_initialPosition				= null;
	private JTextPane			textPane_description				= null;
	private JLabel				label_defaultSpeed					= null;
	private JLabel				label_title							= null;
	private JLabel				label_min_x							= null;
	private JSpinner			spinner_min_x						= null;
	private JLabel				label_max_x							= null;
	private JSpinner			spinner_max_x						= null;
	private JLabel				label_min_y							= null;
	private JSpinner			spinner_min_y						= null;
	private JLabel				label_max_y							= null;
	private JSpinner			spinner_max_y						= null;
	private JSpinner			spinner_max_z						= null;
	private JSpinner			spinner_min_z						= null;
	private JLabel				label_min_z							= null;
	private JLabel				label_max_z							= null;
	private JSpinner			spinner_defaultSpeed				= null;
	private JLabel				label_fairnessCount					= null;
	private JSpinner			spinner_fairnessCount				= null;
	private JLabel				label_epsilon						= null;
	private JPanel				panel_components					= null;
	private JSpinner			spinner_epsilon						= null;
	private JLabel				label_fairnessCountDescription_1	= null;
	private JLabel				label_fairnessCountDescription_2	= null;
	private JLabel				label_epsilonDescription_1			= null;
	private JLabel				label_epsilonDescription_2			= null;

	/**
	 * Constructor.
	 */
	public SycamorePrefsPane()
	{
		initialize();
	}

	/**
	 * Init GUI;
	 */
	private void initialize()
	{
		setPreferredSize(new Dimension(500, 440));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel_system = new GridBagConstraints();
		gbc_panel_system.insets = new Insets(2, 2, 2, 2);
		gbc_panel_system.fill = GridBagConstraints.BOTH;
		gbc_panel_system.gridx = 0;
		gbc_panel_system.gridy = 0;
		add(getPanel_system(), gbc_panel_system);
		GridBagConstraints gbc_panel_initialPosition = new GridBagConstraints();
		gbc_panel_initialPosition.insets = new Insets(2, 2, 2, 2);
		gbc_panel_initialPosition.fill = GridBagConstraints.BOTH;
		gbc_panel_initialPosition.gridx = 0;
		gbc_panel_initialPosition.gridy = 1;
		add(getPanel_initialPosition(), gbc_panel_initialPosition);
	}

	/**
	 * @return panel_system
	 */
	private JPanel getPanel_system()
	{
		if (panel_system == null)
		{
			panel_system = new JPanel();
			panel_system.setBorder(BorderFactory.createTitledBorder("System preferences"));
			GridBagLayout gbl_panel_system = new GridBagLayout();
			gbl_panel_system.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_system.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_system.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_panel_system.rowWeights = new double[]
			{ 0.0, Double.MIN_VALUE };
			panel_system.setLayout(gbl_panel_system);
			GridBagConstraints gbc_panel_components = new GridBagConstraints();
			gbc_panel_components.gridx = 0;
			gbc_panel_components.gridy = 0;
			panel_system.add(getPanel_components(), gbc_panel_components);
		}
		return panel_system;
	}

	/**
	 * @return panel_initialPosition
	 */
	private JPanel getPanel_initialPosition()
	{
		if (panel_initialPosition == null)
		{
			panel_initialPosition = new JPanel();
			panel_initialPosition.setBorder(BorderFactory.createTitledBorder("Initial position preferences"));
			GridBagLayout gbl_panel_initialPosition = new GridBagLayout();
			gbl_panel_initialPosition.columnWidths = new int[]
			{ 0, 0, 0, 0 };
			gbl_panel_initialPosition.rowHeights = new int[]
			{ 0, 0, 0, 0, 0, 0 };
			gbl_panel_initialPosition.columnWeights = new double[]
			{ 1.0, 0.0, 1.0, 1.0 };
			gbl_panel_initialPosition.rowWeights = new double[]
			{ 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel_initialPosition.setLayout(gbl_panel_initialPosition);
			GridBagConstraints gbc_textPane_description = new GridBagConstraints();
			gbc_textPane_description.gridwidth = 4;
			gbc_textPane_description.insets = new Insets(2, 2, 2, 2);
			gbc_textPane_description.fill = GridBagConstraints.BOTH;
			gbc_textPane_description.gridx = 0;
			gbc_textPane_description.gridy = 0;
			panel_initialPosition.add(getTextPane_description(), gbc_textPane_description);
			GridBagConstraints gbc_label_title = new GridBagConstraints();
			gbc_label_title.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_title.insets = new Insets(2, 2, 2, 2);
			gbc_label_title.gridwidth = 4;
			gbc_label_title.gridx = 0;
			gbc_label_title.gridy = 1;
			panel_initialPosition.add(getLabel_title(), gbc_label_title);
			GridBagConstraints gbc_label_min_x = new GridBagConstraints();
			gbc_label_min_x.anchor = GridBagConstraints.EAST;
			gbc_label_min_x.insets = new Insets(2, 2, 2, 2);
			gbc_label_min_x.gridx = 0;
			gbc_label_min_x.gridy = 2;
			panel_initialPosition.add(getLabel_min_x(), gbc_label_min_x);
			GridBagConstraints gbc_spinner_min_x = new GridBagConstraints();
			gbc_spinner_min_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_min_x.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_min_x.gridx = 1;
			gbc_spinner_min_x.gridy = 2;
			panel_initialPosition.add(getSpinner_min_x(), gbc_spinner_min_x);
			GridBagConstraints gbc_label_max_x = new GridBagConstraints();
			gbc_label_max_x.anchor = GridBagConstraints.EAST;
			gbc_label_max_x.insets = new Insets(2, 2, 2, 2);
			gbc_label_max_x.gridx = 2;
			gbc_label_max_x.gridy = 2;
			panel_initialPosition.add(getLabel_max_x(), gbc_label_max_x);
			GridBagConstraints gbc_spinner_max_x = new GridBagConstraints();
			gbc_spinner_max_x.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_max_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_max_x.gridx = 3;
			gbc_spinner_max_x.gridy = 2;
			panel_initialPosition.add(getSpinner_max_x(), gbc_spinner_max_x);
			GridBagConstraints gbc_label_min_y = new GridBagConstraints();
			gbc_label_min_y.anchor = GridBagConstraints.EAST;
			gbc_label_min_y.insets = new Insets(2, 2, 2, 2);
			gbc_label_min_y.gridx = 0;
			gbc_label_min_y.gridy = 3;
			panel_initialPosition.add(getLabel_min_y(), gbc_label_min_y);
			GridBagConstraints gbc_spinner_min_y = new GridBagConstraints();
			gbc_spinner_min_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_min_y.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_min_y.gridx = 1;
			gbc_spinner_min_y.gridy = 3;
			panel_initialPosition.add(getSpinner_min_y(), gbc_spinner_min_y);
			GridBagConstraints gbc_label_max_y = new GridBagConstraints();
			gbc_label_max_y.anchor = GridBagConstraints.EAST;
			gbc_label_max_y.insets = new Insets(2, 2, 2, 2);
			gbc_label_max_y.gridx = 2;
			gbc_label_max_y.gridy = 3;
			panel_initialPosition.add(getLabel_max_y(), gbc_label_max_y);
			GridBagConstraints gbc_spinner_max_y = new GridBagConstraints();
			gbc_spinner_max_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_max_y.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_max_y.gridx = 3;
			gbc_spinner_max_y.gridy = 3;
			panel_initialPosition.add(getSpinner_max_y(), gbc_spinner_max_y);
			GridBagConstraints gbc_label_min_z = new GridBagConstraints();
			gbc_label_min_z.anchor = GridBagConstraints.EAST;
			gbc_label_min_z.insets = new Insets(2, 2, 2, 2);
			gbc_label_min_z.gridx = 0;
			gbc_label_min_z.gridy = 4;
			panel_initialPosition.add(getLabel_min_z(), gbc_label_min_z);
			GridBagConstraints gbc_spinner_min_z = new GridBagConstraints();
			gbc_spinner_min_z.anchor = GridBagConstraints.WEST;
			gbc_spinner_min_z.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_min_z.gridx = 1;
			gbc_spinner_min_z.gridy = 4;
			panel_initialPosition.add(getSpinner_min_z(), gbc_spinner_min_z);
			GridBagConstraints gbc_label_max_z = new GridBagConstraints();
			gbc_label_max_z.anchor = GridBagConstraints.EAST;
			gbc_label_max_z.insets = new Insets(2, 2, 2, 2);
			gbc_label_max_z.gridx = 2;
			gbc_label_max_z.gridy = 4;
			panel_initialPosition.add(getLabel_max_z(), gbc_label_max_z);
			GridBagConstraints gbc_spinner_max_z = new GridBagConstraints();
			gbc_spinner_max_z.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_max_z.anchor = GridBagConstraints.WEST;
			gbc_spinner_max_z.gridx = 3;
			gbc_spinner_max_z.gridy = 4;
			panel_initialPosition.add(getSpinner_max_z(), gbc_spinner_max_z);
		}
		return panel_initialPosition;
	}

	/**
	 * @return textPane_description
	 */
	private JTextPane getTextPane_description()
	{
		if (textPane_description == null)
		{
			textPane_description = new JTextPane();
			textPane_description.setFont(textPane_description.getFont().deriveFont(textPane_description.getFont().getSize() - 2f));
			textPane_description.setText("When no InitialCondition plugin is chosen, the System places the robots in random positions.");
			textPane_description.setOpaque(false);
		}
		return textPane_description;
	}

	/**
	 * @return label_defaultSpeed
	 */
	private JLabel getLabel_defaultSpeed()
	{
		if (label_defaultSpeed == null)
		{
			label_defaultSpeed = new JLabel("Default robot speed (units/sec):");
		}
		return label_defaultSpeed;
	}

	/**
	 * @return label_title
	 */
	private JLabel getLabel_title()
	{
		if (label_title == null)
		{
			label_title = new JLabel("Please, choose the bounds:");
			label_title.setFont(label_title.getFont().deriveFont(label_title.getFont().getSize() - 2f));
		}
		return label_title;
	}

	/**
	 * @return label_min_x
	 */
	private JLabel getLabel_min_x()
	{
		if (label_min_x == null)
		{
			label_min_x = new JLabel("Min X:");
		}
		return label_min_x;
	}

	/**
	 * @return spinner_min_x
	 */
	private JSpinner getSpinner_min_x()
	{
		if (spinner_min_x == null)
		{
			spinner_min_x = new JSpinner();
			spinner_min_x.setMaximumSize(new Dimension(80, 27));
			spinner_min_x.setMinimumSize(new Dimension(80, 27));
			spinner_min_x.setPreferredSize(new Dimension(80, 27));
			spinner_min_x.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_min_x.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X));
			spinner_min_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MIN_X, (Integer) spinner_min_x.getValue());
				}
			});
		}
		return spinner_min_x;
	}

	/**
	 * @return label_max_x
	 */
	private JLabel getLabel_max_x()
	{
		if (label_max_x == null)
		{
			label_max_x = new JLabel("Max X:");
		}
		return label_max_x;
	}

	/**
	 * @return spinner_max_x
	 */
	private JSpinner getSpinner_max_x()
	{
		if (spinner_max_x == null)
		{
			spinner_max_x = new JSpinner();
			spinner_max_x.setMaximumSize(new Dimension(80, 27));
			spinner_max_x.setMinimumSize(new Dimension(80, 27));
			spinner_max_x.setPreferredSize(new Dimension(80, 27));
			spinner_max_x.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_max_x.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X));
			spinner_max_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MAX_X, (Integer) spinner_max_x.getValue());
				}
			});
		}
		return spinner_max_x;
	}

	/**
	 * @return label_min_y
	 */
	private JLabel getLabel_min_y()
	{
		if (label_min_y == null)
		{
			label_min_y = new JLabel("Min Y:");
		}
		return label_min_y;
	}

	/**
	 * @return spinner_min_y
	 */
	private JSpinner getSpinner_min_y()
	{
		if (spinner_min_y == null)
		{
			spinner_min_y = new JSpinner();
			spinner_min_y.setMaximumSize(new Dimension(80, 27));
			spinner_min_y.setMinimumSize(new Dimension(80, 27));
			spinner_min_y.setPreferredSize(new Dimension(80, 27));
			spinner_min_y.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_min_y.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y));
			spinner_min_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y, (Integer) spinner_min_y.getValue());
				}
			});
		}
		return spinner_min_y;
	}

	/**
	 * @return label_max_y
	 */
	private JLabel getLabel_max_y()
	{
		if (label_max_y == null)
		{
			label_max_y = new JLabel("Max Y:");
		}
		return label_max_y;
	}

	/**
	 * @return spinner_max_y
	 */
	private JSpinner getSpinner_max_y()
	{
		if (spinner_max_y == null)
		{
			spinner_max_y = new JSpinner();
			spinner_max_y.setMaximumSize(new Dimension(80, 27));
			spinner_max_y.setMinimumSize(new Dimension(80, 27));
			spinner_max_y.setPreferredSize(new Dimension(80, 27));
			spinner_max_y.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_max_y.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y));
			spinner_max_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y, (Integer) spinner_max_y.getValue());
				}
			});
		}
		return spinner_max_y;
	}

	/**
	 * @return spinner_max_z
	 */
	private JSpinner getSpinner_max_z()
	{
		if (spinner_max_z == null)
		{
			spinner_max_z = new JSpinner();
			spinner_max_z.setMaximumSize(new Dimension(80, 27));
			spinner_max_z.setMinimumSize(new Dimension(80, 27));
			spinner_max_z.setPreferredSize(new Dimension(80, 27));
			spinner_max_z.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_max_z.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Z));
			spinner_max_z.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MAX_Z, (Integer) spinner_max_z.getValue());
				}
			});
		}
		return spinner_max_z;
	}

	/**
	 * @return spinner_min_z
	 */
	private JSpinner getSpinner_min_z()
	{
		if (spinner_min_z == null)
		{
			spinner_min_z = new JSpinner();
			spinner_min_z.setMaximumSize(new Dimension(80, 27));
			spinner_min_z.setMinimumSize(new Dimension(80, 27));
			spinner_min_z.setPreferredSize(new Dimension(80, 27));
			spinner_min_z.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_min_z.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Z));
			spinner_min_z.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.INITIAL_POSITION_MIN_Z, (Integer) spinner_min_z.getValue());
				}
			});

		}
		return spinner_min_z;
	}

	/**
	 * @return label_min_z
	 */
	private JLabel getLabel_min_z()
	{
		if (label_min_z == null)
		{
			label_min_z = new JLabel("Min Z:");
		}
		return label_min_z;
	}

	/**
	 * @return label_max_z
	 */
	private JLabel getLabel_max_z()
	{
		if (label_max_z == null)
		{
			label_max_z = new JLabel("Max Z:");
		}
		return label_max_z;
	}

	/**
	 * @return spinner_defaultSpeed
	 */
	private JSpinner getSpinner_defaultSpeed()
	{
		if (spinner_defaultSpeed == null)
		{
			spinner_defaultSpeed = new JSpinner();
			spinner_defaultSpeed.setPreferredSize(new Dimension(80, 27));
			spinner_defaultSpeed.setMinimumSize(new Dimension(80, 27));
			spinner_defaultSpeed.setMaximumSize(new Dimension(80, 27));
			spinner_defaultSpeed.setModel(new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.1));
			spinner_defaultSpeed.setValue(PropertyManager.getSharedInstance().getDoubleProperty(ApplicationProperties.DEFAULT_ROBOT_SPEED) * 10.0);
			spinner_defaultSpeed.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.DEFAULT_ROBOT_SPEED, (Float) spinner_defaultSpeed.getValue() / 10.0);
				}
			});
		}
		return spinner_defaultSpeed;
	}

	/**
	 * @return label_fairnessCount
	 */
	private JLabel getLabel_fairnessCount()
	{
		if (label_fairnessCount == null)
		{
			label_fairnessCount = new JLabel("Fairness count value:");
		}
		return label_fairnessCount;
	}

	/**
	 * @return spinner_fairnessCount
	 */
	private JSpinner getSpinner_fairnessCount()
	{
		if (spinner_fairnessCount == null)
		{
			spinner_fairnessCount = new JSpinner();
			spinner_fairnessCount.setPreferredSize(new Dimension(80, 27));
			spinner_fairnessCount.setMinimumSize(new Dimension(80, 27));
			spinner_fairnessCount.setMaximumSize(new Dimension(80, 27));
			spinner_fairnessCount.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			spinner_fairnessCount.setValue(PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.FAIRNESS_COUNT));
			spinner_fairnessCount.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.FAIRNESS_COUNT, (Integer) spinner_fairnessCount.getValue());
				}
			});
		}
		return spinner_fairnessCount;
	}

	/**
	 * @return label_epsilon
	 */
	private JLabel getLabel_epsilon()
	{
		if (label_epsilon == null)
		{
			label_epsilon = new JLabel("Epsilon value:");
		}
		return label_epsilon;
	}

	/**
	 * @return panel_components
	 */
	private JPanel getPanel_components()
	{
		if (panel_components == null)
		{
			panel_components = new JPanel();
			panel_components.setBorder(BorderFactory.createEmptyBorder());
			GridBagLayout gbl_panel_components = new GridBagLayout();
			gbl_panel_components.columnWidths = new int[]
			{ 177, 80, 0 };
			gbl_panel_components.rowHeights = new int[]
			{ 27, 27, 0, 0, 28, 0, 0, 0, 0 };
			gbl_panel_components.columnWeights = new double[]
			{ 0.0, 0.0, Double.MIN_VALUE };
			gbl_panel_components.rowWeights = new double[]
			{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel_components.setLayout(gbl_panel_components);
			GridBagConstraints gbc_label_defaultSpeed = new GridBagConstraints();
			gbc_label_defaultSpeed.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_defaultSpeed.insets = new Insets(2, 2, 2, 2);
			gbc_label_defaultSpeed.gridx = 0;
			gbc_label_defaultSpeed.gridy = 0;
			panel_components.add(getLabel_defaultSpeed(), gbc_label_defaultSpeed);
			GridBagConstraints gbc_spinner_defaultSpeed = new GridBagConstraints();
			gbc_spinner_defaultSpeed.anchor = GridBagConstraints.WEST;
			gbc_spinner_defaultSpeed.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_defaultSpeed.gridx = 1;
			gbc_spinner_defaultSpeed.gridy = 0;
			panel_components.add(getSpinner_defaultSpeed(), gbc_spinner_defaultSpeed);
			GridBagConstraints gbc_label_fairnessCount = new GridBagConstraints();
			gbc_label_fairnessCount.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_fairnessCount.insets = new Insets(2, 2, 0, 2);
			gbc_label_fairnessCount.gridx = 0;
			gbc_label_fairnessCount.gridy = 1;
			panel_components.add(getLabel_fairnessCount(), gbc_label_fairnessCount);
			GridBagConstraints gbc_spinner_fairnessCount = new GridBagConstraints();
			gbc_spinner_fairnessCount.anchor = GridBagConstraints.WEST;
			gbc_spinner_fairnessCount.insets = new Insets(2, 2, 0, 2);
			gbc_spinner_fairnessCount.gridx = 1;
			gbc_spinner_fairnessCount.gridy = 1;
			panel_components.add(getSpinner_fairnessCount(), gbc_spinner_fairnessCount);
			GridBagConstraints gbc_label_fairnessCountDescription_1 = new GridBagConstraints();
			gbc_label_fairnessCountDescription_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_fairnessCountDescription_1.gridwidth = 2;
			gbc_label_fairnessCountDescription_1.insets = new Insets(0, 2, 1, 2);
			gbc_label_fairnessCountDescription_1.gridx = 0;
			gbc_label_fairnessCountDescription_1.gridy = 2;
			panel_components.add(getLabel_fairnessCountDescription_1(), gbc_label_fairnessCountDescription_1);
			GridBagConstraints gbc_label_fairnessCountDescription_2 = new GridBagConstraints();
			gbc_label_fairnessCountDescription_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_fairnessCountDescription_2.gridwidth = 2;
			gbc_label_fairnessCountDescription_2.insets = new Insets(1, 2, 2, 2);
			gbc_label_fairnessCountDescription_2.gridx = 0;
			gbc_label_fairnessCountDescription_2.gridy = 3;
			panel_components.add(getLabel_fairnessCountDescription_2(), gbc_label_fairnessCountDescription_2);
			GridBagConstraints gbc_label_epsilon = new GridBagConstraints();
			gbc_label_epsilon.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_epsilon.insets = new Insets(5, 2, 0, 2);
			gbc_label_epsilon.gridx = 0;
			gbc_label_epsilon.gridy = 4;
			panel_components.add(getLabel_epsilon(), gbc_label_epsilon);
			GridBagConstraints gbc_spinner_epsilon = new GridBagConstraints();
			gbc_spinner_epsilon.insets = new Insets(5, 2, 0, 2);
			gbc_spinner_epsilon.anchor = GridBagConstraints.WEST;
			gbc_spinner_epsilon.gridx = 1;
			gbc_spinner_epsilon.gridy = 4;
			panel_components.add(getSpinner_epsilon(), gbc_spinner_epsilon);
			GridBagConstraints gbc_label_epsilonDescription_1 = new GridBagConstraints();
			gbc_label_epsilonDescription_1.insets = new Insets(0, 2, 1, 2);
			gbc_label_epsilonDescription_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_epsilonDescription_1.gridwidth = 2;
			gbc_label_epsilonDescription_1.gridx = 0;
			gbc_label_epsilonDescription_1.gridy = 5;
			panel_components.add(getLabel_epsilonDescription_1(), gbc_label_epsilonDescription_1);
			GridBagConstraints gbc_label_epsilonDescription_2 = new GridBagConstraints();
			gbc_label_epsilonDescription_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_epsilonDescription_2.gridwidth = 2;
			gbc_label_epsilonDescription_2.insets = new Insets(1, 2, 2, 2);
			gbc_label_epsilonDescription_2.gridx = 0;
			gbc_label_epsilonDescription_2.gridy = 6;
			panel_components.add(getLabel_epsilonDescription_2(), gbc_label_epsilonDescription_2);
		}
		return panel_components;
	}

	/**
	 * @return spinner_epsilon
	 */
	private JSpinner getSpinner_epsilon()
	{
		if (spinner_epsilon == null)
		{
			spinner_epsilon = new JSpinner();
			spinner_epsilon.setPreferredSize(new Dimension(120, 27));
			spinner_epsilon.setMinimumSize(new Dimension(120, 27));
			spinner_epsilon.setMaximumSize(new Dimension(120, 27));
			
			// define double values
			spinner_epsilon.setModel(new SpinnerListModel(new Object[]
			{ new Double(0.1), new Double(0.01), new Double(0.001), new Double(0.0001), new Double(0.00001), new Double(0.000001), new Double(0.0000001), new Double(0.00000001),
					new Double(0.000000001), new Double(0.0000000001), new Double(0.00000000001) }));
			spinner_epsilon.getEditor().setEnabled(false);
			spinner_epsilon.setValue(PropertyManager.getSharedInstance().getDoubleProperty(ApplicationProperties.EPSILON));
			spinner_epsilon.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PropertyManager.getSharedInstance().putProperty(ApplicationProperties.EPSILON, (Double) spinner_epsilon.getValue());
				}
			});
		}
		return spinner_epsilon;
	}

	/**
	 * @return label_fairnessCountDEscription_1
	 */
	private JLabel getLabel_fairnessCountDescription_1()
	{
		if (label_fairnessCountDescription_1 == null)
		{
			label_fairnessCountDescription_1 = new JLabel("The fairness count is the maximum number of steps where");
			label_fairnessCountDescription_1.setFont(label_fairnessCountDescription_1.getFont().deriveFont(label_fairnessCountDescription_1.getFont().getSize() - 2f));
		}
		return label_fairnessCountDescription_1;
	}

	/**
	 * @return label_fairnessCountDescription_2
	 */
	private JLabel getLabel_fairnessCountDescription_2()
	{
		if (label_fairnessCountDescription_2 == null)
		{
			label_fairnessCountDescription_2 = new JLabel("a robot can stay still without being moved by the fair scheduler.");
			label_fairnessCountDescription_2.setFont(label_fairnessCountDescription_2.getFont().deriveFont(label_fairnessCountDescription_2.getFont().getSize() - 2f));
		}
		return label_fairnessCountDescription_2;
	}

	/**
	 * @return label_epsilonDescription_1
	 */
	private JLabel getLabel_epsilonDescription_1()
	{
		if (label_epsilonDescription_1 == null)
		{
			label_epsilonDescription_1 = new JLabel("Epsilon is the minimum size of a robot step.");
			label_epsilonDescription_1.setFont(label_epsilonDescription_1.getFont().deriveFont(label_epsilonDescription_1.getFont().getSize() - 2f));
		}
		return label_epsilonDescription_1;
	}

	/**
	 * @return label_epsilonDescription_2
	 */
	private JLabel getLabel_epsilonDescription_2()
	{
		if (label_epsilonDescription_2 == null)
		{
			label_epsilonDescription_2 = new JLabel("A step smaller than epsilon is considered to be zero.");
			label_epsilonDescription_2.setFont(label_epsilonDescription_2.getFont().deriveFont(label_epsilonDescription_2.getFont().getSize() - 2f));
		}
		return label_epsilonDescription_2;
	}
}
