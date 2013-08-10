package it.diunipi.volpi.sycamore.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 * A iOS-like swith toggle.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @see https://weblogs.java.net/blog/campbell/archive/2006/07/java_2d_tricker.html
 */
public class SwitchToggle extends JToggleButton implements ActionListener, Runnable, MouseMotionListener, MouseListener, HierarchyListener
{
	private static final long	serialVersionUID	= -1934864426879149371L;
	private Image				bgImage				= new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/bg.png")).getImage();

	int							buttonX				= 0;
	int							deltaX				= 0;
	boolean						selected			= true;
	boolean						drag				= true;

	/**
	 * Default constructor.
	 */
	public SwitchToggle()
	{
		super();

		buttonX = -1 * (this.getWidth() - this.getHeight());
		this.addActionListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addHierarchyListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractButton#isSelected()
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractButton#setSelected(boolean)
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		new Thread(this).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		int width = getWidth();
		int height = getHeight();
		int arc = getHeight();
		int fullWidth = width + (width - height);

		// Create a translucent intermediate image in which we can perform
		// the soft clipping
		GraphicsConfiguration gc = ((Graphics2D) g).getDeviceConfiguration();
		BufferedImage img = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		Graphics2D g2 = img.createGraphics();

		// Clear the image so all pixels have zero alpha
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, width, height);

		// Render our clip shape into the image. Note that we enable
		// antialiasing to achieve the soft clipping effect. Try
		// commenting out the line that enables antialiasing, and
		// you will see that you end up with the usual hard clipping.
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(0, 0, width, height, arc, arc);

		// Here's the trick... We use SrcAtop, which effectively uses the
		// alpha value as a coverage value for each pixel stored in the
		// destination. For the areas outside our clip shape, the destination
		// alpha will be zero, so nothing is rendered in those areas. For
		// the areas inside our clip shape, the destination alpha will be fully
		// opaque, so the full color is rendered. At the edges, the original
		// antialiasing is carried over to give us the desired soft clipping
		// effect.
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(bgImage, buttonX, 0, fullWidth, this.getHeight(), null, null);
		g2.dispose();

		// eventually disable
		if (!this.isEnabled())
		{
			Image disabled = GrayFilter.createDisabledImage(img);

			// Copy our intermediate image to the screen
			g.drawImage(disabled, 0, 0, null);
		}
		else
		{
			// Copy our intermediate image to the screen
			g.drawImage(img, 0, 0, null);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!drag)
		{
			selected = !selected;
			new Thread(this).start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		if (this.isSelected())
		{
			for (; buttonX < 0; buttonX++)
			{
				this.repaint();
				try
				{
					Thread.sleep(4);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

		}
		else
		{
			int minX = -1 * (this.getWidth() - this.getHeight());
			for (; buttonX > minX; buttonX--)
			{
				this.repaint();
				try
				{
					Thread.sleep(4);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent evt)
	{
		drag = true;
		if (deltaX == -1)
		{
			deltaX = evt.getX() - buttonX;
		}

		buttonX = evt.getX() - deltaX;

		int min = -1 * (this.getWidth() - this.getHeight());
		int max = 0;

		if (buttonX < min)
		{
			buttonX = min;
		}
		if (buttonX > max)
		{
			buttonX = max;
		}

		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		deltaX = -1;
		if (drag)
		{
			int min = -1 * (this.getWidth() - this.getHeight());
			if (buttonX < min / 2)
			{
				this.setSelected(false);
			}
			else
			{
				this.setSelected(true);
			}
		}
		drag = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.HierarchyListener#hierarchyChanged(java.awt.event.HierarchyEvent)
	 */
	@Override
	public void hierarchyChanged(HierarchyEvent arg0)
	{
		new Thread(this).start();
	}
}
