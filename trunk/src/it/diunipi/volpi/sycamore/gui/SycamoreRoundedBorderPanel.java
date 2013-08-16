package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * A generic panel with shadow and rounded borders.
 * 
 * reference:
 * http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreRoundedBorderPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= -184882594049663948L;
	/** Stroke size. it is recommended to set it to 1 for better view */
	private int					strokeSize			= 1;
	/** Color of shadow */
	private Color				shadowColor			= Color.DARK_GRAY;
	/** Sets if it has an High Quality view */
	private boolean				highQuality			= true;
	/** Double values for Horizontal and Vertical radius of corner arcs */
	private Dimension			arcs				= new Dimension(20, 20);
	/** Distance between shadow border and opaque panel border */
	private int					shadowGap			= 3;
	/** The offset of shadow. */
	private int					shadowOffset		= 1;
	/** The transparency value of shadow. ( 0 - 255) */
	private int					shadowAlpha			= 50;
	private boolean				shadowVisible		= true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int shadowGap = this.shadowGap;
		int width = getWidth();
		int height = getHeight();

		Color shadowColorA = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
		Graphics2D graphics = (Graphics2D) g;

		// Sets antialiasing if HQ.
		if (highQuality)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		// Draws shadow borders if any.
		if (shadowVisible)
		{
			graphics.setColor(shadowColorA);
			graphics.fillRoundRect(shadowOffset, shadowOffset,// X position
					width - strokeSize - shadowOffset, // width
					height - strokeSize - shadowOffset, // height
					arcs.width, arcs.height);// arc Dimension
		}
		else
		{
			shadowGap = 1;
		}

		// Draws the rounded opaque panel with borders.
		graphics.setClip(new RoundRectangle2D.Float(0, 0, width - shadowGap, height - shadowGap, arcs.width, arcs.height));

		graphics.setColor(getBackground());
		graphics.fillRect(0, 0, width, height);
	}

	/**
	 * @return shadowVisible
	 */
	public boolean isShadowVisible()
	{
		return shadowVisible;
	}

	/**
	 * @param shadowVisible
	 */
	public void setShadowVisible(boolean shadowVisible)
	{
		this.shadowVisible = shadowVisible;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine.SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{

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
