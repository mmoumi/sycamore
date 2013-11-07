/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.jme3.math.ColorRGBA;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class Trace2D extends MeasureImpl
{
	private enum COLOR_MODE
	{
		BLACK, ROBOT_COLOR, RANDOM;
	}
	
	private Hashtable<SycamoreRobot<Point2D>, Vector<Point2D>>	data	= null;
	private COLOR_MODE colorMode = COLOR_MODE.RANDOM;

	/**
	 * 
	 */
	public Trace2D()
	{
		this.data = new Hashtable<SycamoreRobot<Point2D>, Vector<Point2D>>();
	}
	
	/**
	 * @param colorMode the colorMode to set
	 */
	public void setColorMode(COLOR_MODE colorMode)
	{
		this.colorMode = colorMode;
	}
	
	/**
	 * @return the colorMode
	 */
	public COLOR_MODE getColorMode()
	{
		return colorMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStart()
	 */
	@Override
	public void onSimulationStart()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStep()
	 */
	@Override
	public void onSimulationStep()
	{
		Iterator<SycamoreRobot<Point2D>> iterator = engine.getRobots().iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<Point2D> robot = iterator.next();
			Point2D position = robot.getGlobalPosition();
			
			if (this.data.containsKey(robot))
			{
				Vector<Point2D> positions = this.data.get(robot);
				positions.add(position);
			}
			else
			{
				Vector<Point2D> positions = new Vector<Point2D>();
				positions.add(position);
				
				this.data.put(robot, positions);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationEnd()
	 */
	@Override
	public void onSimulationEnd()
	{
		BufferedImage image = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		
		g2d.setBackground(Color.LIGHT_GRAY);
		g2d.clearRect(0, 0, 2000, 2000);
		g2d.setTransform(AffineTransform.getTranslateInstance(1000, 1000));
		
		Set<SycamoreRobot<Point2D>> robots = data.keySet();
		for (SycamoreRobot<Point2D> robot : robots)
		{
			ColorRGBA color = null;
			if (colorMode == COLOR_MODE.BLACK)
			{
				color = ColorRGBA.Black;
			}
			else if (colorMode == COLOR_MODE.ROBOT_COLOR)
			{
				color = robot.getColor();
			}
			else
			{
				color = SycamoreUtil.getRandomColor();
			}
			
			g2d.setColor(SycamoreUtil.convertColor(color));
			
			int width = (robot.isHumanPilot()? 7 : 3);
			g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			
			Vector<Point2D> points = data.get(robot);
			int[] xPoints = new int[points.size()];
			int[] yPoints = new int[points.size()];
			
			for (int i = 0; i < points.size(); i++)
			{
				Point2D point = points.elementAt(i);
				xPoints[i] = (int) (point.x * 100);
				yPoints[i] = (int) (point.y * 100);
			}
			
			g2d.drawPolyline(xPoints, yPoints, points.size());
		}
		
		File outputfile = new File("/Users/Vale/Desktop/image.png");
		
		try
		{
			ImageIO.write(image, "png", outputfile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Draws the robot's trace";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Draws the robot's trace";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

}
