package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * The panel that lets the user modify the camera of the scene
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreCameraControlPanel extends SycamorePanel
{
	private enum CAMERA_TARGETS
	{
		ORIGIN("on origin"), BARICENTRUM("on baricentrum");

		private String	description	= null;

		/**
		 * Constructor.
		 * 
		 * @param command
		 * @param description
		 */
		CAMERA_TARGETS(String description)
		{
			this.description = description;
		}

		/**
		 * @return the description
		 */
		public String getDescription()
		{
			return description;
		}

		/**
		 * Returns the list of all the descriptions
		 * @return
		 */
		public static String[] getDescriptions()
		{
			CAMERA_TARGETS[] values = CAMERA_TARGETS.values();
			String[] ret = new String[values.length];

			for (int i = 0; i < values.length; i++)
			{
				ret[i] = values[i].getDescription();
			}

			return ret;
		}

		/**
		 * @param description
		 * @return
		 */
		public static CAMERA_TARGETS getByDescription(String description)
		{
			if (description.equals(ORIGIN.getDescription()))
			{
				return ORIGIN;
			}
			else if (description.equals(BARICENTRUM.getDescription()))
			{
				return BARICENTRUM;
			}
			else
				return null;
		}
	}

	private static final long		serialVersionUID		= -5768754358845874470L;
	private JComboBox				comboBox_cameraPosition	= null;
	private JLabel					label_camera			= null;
	private SycamoreEngine			appEngine				= null;
	private Vector<ActionListener>	listeners				= null;

	/**
	 * Default constructor
	 */
	public SycamoreCameraControlPanel()
	{
		this.listeners = new Vector<ActionListener>();
		initialize();
	}

	/**
	 * Init the GUI
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]
		{ 0.0, 1.0 };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_label_camera = new GridBagConstraints();
		gbc_label_camera.anchor = GridBagConstraints.EAST;
		gbc_label_camera.insets = new Insets(2, 2, 2, 2);
		gbc_label_camera.gridx = 0;
		gbc_label_camera.gridy = 0;
		add(getLabel_camera(), gbc_label_camera);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(2, 2, 2, 2);
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.fill = GridBagConstraints.VERTICAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(getComboBox_cameraPosition(), gbc_comboBox);
	}

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param l
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(ActionListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set
	 * <code>Action</code> for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param l
	 *            the listener to be removed
	 */
	public void removeActionListener(ActionListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Fires passed ActionEvent to all registered listeners, by calling <code>ActionPerformed</code>
	 * method on all of them.
	 * 
	 * @param e
	 */
	private void fireActionEvent(ActionEvent e)
	{
		for (ActionListener listener : this.listeners)
		{
			listener.actionPerformed(e);
		}
	}

	/**
	 * @return the label_camera
	 */
	private JLabel getLabel_camera()
	{
		if (label_camera == null)
		{
			label_camera = new JLabel("Camera:");
			label_camera.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/camera_32x32.png")));
		}
		return label_camera;
	}

	/**
	 * @return the comboBox
	 */
	private JComboBox getComboBox_cameraPosition()
	{
		if (comboBox_cameraPosition == null)
		{
			comboBox_cameraPosition = new JComboBox();
			comboBox_cameraPosition.setMaximumSize(new Dimension(52, 27));
			comboBox_cameraPosition.setModel(new DefaultComboBoxModel(CAMERA_TARGETS.getDescriptions()));
			comboBox_cameraPosition.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String value = (String) comboBox_cameraPosition.getSelectedItem();
					CAMERA_TARGETS target = CAMERA_TARGETS.getByDescription(value);

					if (target == CAMERA_TARGETS.ORIGIN)
					{
						fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.CAMERA_ON_ORIGIN.name()));
					}
					else if (target == CAMERA_TARGETS.BARICENTRUM)
					{
						fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.CAMERA_ON_BARICENTRUM.name()));
					}
				}
			});
		}
		return comboBox_cameraPosition;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		getComboBox_cameraPosition().setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine.SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		// Nothing to do
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// Nothing to do
	}
}
