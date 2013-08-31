/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.app.sycamore.SycamoreApp;
import it.diunipi.volpi.sycamore.engine.SycamorePluginManager;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.jme3.renderer.Caps;
import com.jme3.system.JmeSystem;

/**
 * @author Vale
 * 
 */
public class SycamoreInfoPanel extends JPanel
{
	private static final long			serialVersionUID	= 4200554499440035361L;
	private JLabel						label_icon;
	private JLabel						label_title;
	private JTextPane					textPane_info;
	private JLabel						label_version;
	private SycamoreMemoryStatusPanel	sycamoreMemoryStatusPanel_memoryStatus;
	private JScrollPane					scrollPane_info;

	// Styles vector
	private Vector<MutableAttributeSet>	infoStyles			= new Vector<MutableAttributeSet>();
	// Vector indexes
	static final int					TEXT_HEADER			= 0;
	static final int					TEXT_HIGHLIGHT		= 1;
	static final int					TEXT_NORMAL			= 2;

	/**
	 * Constructor
	 */
	public SycamoreInfoPanel()
	{
		initialize();
	}

	/**
	 * Init GUI
	 */
	private void initialize()
	{

		setPreferredSize(new Dimension(1024, 640));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_label_icon = new GridBagConstraints();
		gbc_label_icon.insets = new Insets(5, 5, 5, 5);
		gbc_label_icon.gridx = 1;
		gbc_label_icon.gridy = 0;
		add(getLabel_icon(), gbc_label_icon);
		GridBagConstraints gbc_label_title = new GridBagConstraints();
		gbc_label_title.insets = new Insets(5, 2, 5, 5);
		gbc_label_title.gridx = 2;
		gbc_label_title.gridy = 0;
		add(getLabel_title(), gbc_label_title);
		GridBagConstraints gbc_label_version = new GridBagConstraints();
		gbc_label_version.gridwidth = 4;
		gbc_label_version.insets = new Insets(0, 0, 5, 5);
		gbc_label_version.gridx = 0;
		gbc_label_version.gridy = 1;
		add(getLabel_version(), gbc_label_version);
		GridBagConstraints gbc_sycamoreMemoryStatusPanel_memoryStatus = new GridBagConstraints();
		GridBagConstraints gbc_scrollPane_info = new GridBagConstraints();
		gbc_scrollPane_info.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_info.gridwidth = 4;
		gbc_scrollPane_info.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_info.gridx = 0;
		gbc_scrollPane_info.gridy = 2;
		add(getScrollPane_info(), gbc_scrollPane_info);
		gbc_sycamoreMemoryStatusPanel_memoryStatus.gridwidth = 4;
		gbc_sycamoreMemoryStatusPanel_memoryStatus.insets = new Insets(5, 5, 0, 5);
		gbc_sycamoreMemoryStatusPanel_memoryStatus.fill = GridBagConstraints.BOTH;
		gbc_sycamoreMemoryStatusPanel_memoryStatus.gridx = 0;
		gbc_sycamoreMemoryStatusPanel_memoryStatus.gridy = 3;
		add(getSycamoreMemoryStatusPanel_memoryStatus(), gbc_sycamoreMemoryStatusPanel_memoryStatus);

		createInfoStyles();
		displayInfo();
	}

	/**
	 * This method was created in VisualAge.
	 */
	private void createInfoStyles()
	{
		MutableAttributeSet attr = null;
		Color headcol = Color.RED;
		Color highlightcol = new Color(0, 0, 180);
		Color normalcol = Color.DARK_GRAY;
		String fontname = "Courier New";//$NON-NLS-1$
		int fontsize = 13;

		attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, fontname);
		StyleConstants.setFontSize(attr, fontsize + 3);
		StyleConstants.setBold(attr, true);
		StyleConstants.setForeground(attr, headcol);
		infoStyles.insertElementAt(attr, TEXT_HEADER);

		attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, fontname);
		StyleConstants.setFontSize(attr, fontsize);
		StyleConstants.setBold(attr, false);
		StyleConstants.setForeground(attr, highlightcol);
		infoStyles.insertElementAt(attr, TEXT_HIGHLIGHT);

		attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, fontname);
		StyleConstants.setFontSize(attr, fontsize);
		StyleConstants.setForeground(attr, normalcol);
		infoStyles.insertElementAt(attr, TEXT_NORMAL);
	}

	/**
	 * @return
	 */
	private JLabel getLabel_icon()
	{
		if (label_icon == null)
		{
			label_icon = new JLabel();
			label_icon.setPreferredSize(new Dimension(64, 64));
			label_icon.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/sycamore_64x64.png")));
		}
		return label_icon;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_title()
	{
		if (label_title == null)
		{
			label_title = new JLabel();
			label_title.setPreferredSize(new Dimension(200, 64));
			label_title.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/title.png")));
		}
		return label_title;
	}

	/**
	 * @return
	 */
	private JTextPane getTextPane_info()
	{
		if (textPane_info == null)
		{
			textPane_info = new JTextPane();
			textPane_info.setFont(new Font("Courier New", textPane_info.getFont().getStyle(), textPane_info.getFont().getSize()));
		}
		return textPane_info;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_version()
	{
		if (label_version == null)
		{
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, getLocale());
			label_version = new JLabel("Version " + SycamoreApp.getVersion() + " (build " + SycamoreApp.getBuildNumber() + ") - " + dateFormat.format(SycamoreApp.getBuildDate()));
			label_version.setFont(label_version.getFont().deriveFont(label_version.getFont().getStyle() | Font.BOLD, label_version.getFont().getSize() + 2f));
		}
		return label_version;
	}

	/**
	 * @return
	 */
	private SycamoreMemoryStatusPanel getSycamoreMemoryStatusPanel_memoryStatus()
	{
		if (sycamoreMemoryStatusPanel_memoryStatus == null)
		{
			sycamoreMemoryStatusPanel_memoryStatus = new SycamoreMemoryStatusPanel();
		}
		return sycamoreMemoryStatusPanel_memoryStatus;
	}

	/**
	 * Display infos
	 */
	private void displayInfo()
	{
		// create text styles
		AttributeSet headerStyle = infoStyles.elementAt(TEXT_HEADER);
		AttributeSet highlightStyle = infoStyles.elementAt(TEXT_HIGHLIGHT);
		AttributeSet normalStyle = infoStyles.elementAt(TEXT_NORMAL);
		Properties properties = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
		Collection<Caps> capabilities = SycamoreSystem.getRenderManager().getRenderer().getCaps();

		// Sycamore infos
		String shortDescription = "Sycamore is a 2D-3D simulation environment for autonomous mobile robots algorithms. With Sycamore it is possible to test an algorithm as well as to check its behavior under several different situations. Sycamore is a completely modular system, where you can write your own plugin and integrated in an easy way. Sycamore is completely open source and with the available SDK it is possible for everybody to write its own plugin and check with it the behavior of the system.";
		String newLine = "\n";
		String tab = "\t";

		// titles
		String sycamoreTitle = "Informations about Sycamore:";
		String title_Version = "Sycamore version: ";
		String title_BuildNum = "Build number: ";
		String title_BuildDate = "Build date: ";
		String title_Plugins = "Number of plugins installed: ";
		String title_Workspace = "Workspace location: ";
		String title_Properties = "Property file location: ";
		String title_Developer = "Developers: ";
		String title_Contacts = "Email contacts: ";
		String title_Webpage = "Webpage: ";

		// txt
		String txt_Version = SycamoreApp.getVersion();
		String txt_BuildNum = SycamoreApp.getBuildNumber();

		DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, getLocale());
		String txt_BuildDate = format.format(SycamoreApp.getBuildDate());

		String txt_Plugins = SycamorePluginManager.getSharedInstance().getLoadedPlugins().size() + "";

		String txt_Workspace = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.name());
		String txt_Properties = PropertyManager.getSharedInstance().getPropertyFilePath();
		String txt_Developer = "Valerio Volpi";
		String txt_Contacts = "valecapannoli@gmail.com, vale.v@me.com, prencipe@di.unipi.it";
		String txt_Webpage = "http://code.google.com/p/sycamore";

		// System infos
		// titles
		String systemTitle = "System informations:";
		String title_OS = "Operative system: ";
		String title_OS_version = "OS Version: ";
		String title_arch = "Architecture: ";
		String title_proc_num = "Number of processors: ";
		String title_memory_tot = "Total memory: ";
		String title_memory_free = "Free memory: ";
		String title_user = "User: ";
		String title_java_version = "Java version: ";
		String title_java_name = "Java VM name: ";
		String title_java_classPath = "Java ClassPath: ";
		String title_java_vendor = "Java Vendor: ";

		// graphics
		String gfxSystemTitle = "Graphics subsystem informations: ";
		String title_jme_version = "JME version: ";
		String title_lwjgl_version = "LWJGL version: ";
		String title_opengl_version = "OpenGL version: ";
		String title_gfx_memory = "Graphics memory available: ";
		String title_gfx_vendor = "Graphics card vendor: ";
		String title_renderer = "Renderer: ";
		String title_gfx_capabilities = "Graphics card capabilities: ";

		// txt
		String txt_OS = properties.getProperty("os.name");
		String txt_OS_version = properties.getProperty("os.version");
		String txt_arch = properties.getProperty("os.arch");
		String txt_proc_num = runtime.availableProcessors() + "";
		String txt_memory_tot = runtime.totalMemory() / (1024 * 1024) + " MB";
		String txt_memory_free = runtime.freeMemory() / (1024 * 1024) + " MB";
		String txt_user = properties.getProperty("user.name");
		String txt_java_version = properties.getProperty("java.version");
		String txt_java_name = properties.getProperty("java.vm.name");
		String txt_java_classPath = properties.getProperty("java.classpath");
		String txt_java_vendor = properties.getProperty("java.vendor");

		final HashMap<String, String> caps = SycamoreSystem.getSystemCaps();
		
		// get 3D caps
		String txt_jme_version = JmeSystem.getFullName();
		String txt_lwjgl_version = caps.get("txt_lwjgl_version");
		String txt_opengl_version = caps.get("txt_opengl_version");
		String txt_gfx_memory = graphicsDevice.getAvailableAcceleratedMemory() / (1024 * 1024) + " MB";
		String txt_gfx_vendor = caps.get("txt_gfx_vendor");
		String txt_renderer = caps.get("txt_renderer");

		
		String txt_gfx_capabilities = capabilities.toString();

		// Credits
		try
		{
			// write sycamore infos
			Document doc = getTextPane_info().getDocument();

			doc.insertString(doc.getLength(), shortDescription + newLine, highlightStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);
			doc.insertString(doc.getLength(), sycamoreTitle + newLine, headerStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Version + tab + tab + txt_Version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_BuildNum + tab + tab + tab + txt_BuildNum + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_BuildDate + tab + tab + tab + txt_BuildDate + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Plugins + tab + txt_Plugins + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Workspace + tab + tab + txt_Workspace + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Properties + tab + tab + txt_Properties + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Developer + tab + tab + tab + txt_Developer + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Contacts + tab + tab + tab + txt_Contacts + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_Webpage + tab + tab + tab + txt_Webpage + newLine, normalStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);

			// write system info
			doc.insertString(doc.getLength(), systemTitle + newLine, headerStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);
			doc.insertString(doc.getLength(), title_OS + tab + tab + txt_OS + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_OS_version + tab + tab + tab + txt_OS_version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_arch + tab + tab + tab + txt_arch + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_proc_num + tab + tab + txt_proc_num + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_memory_tot + tab + tab + tab + txt_memory_tot + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_memory_free + tab + tab + tab + txt_memory_free + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_user + tab + tab + tab + tab + txt_user + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_java_version + tab + tab + tab + txt_java_version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_java_name + tab + tab + tab + txt_java_name + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_java_classPath + tab + tab + tab + txt_java_classPath + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_java_vendor + tab + tab + tab + txt_java_vendor + newLine, normalStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);

			// write graphics subsystem system info
			doc.insertString(doc.getLength(), gfxSystemTitle + newLine, headerStyle);
			doc.insertString(doc.getLength(), newLine, normalStyle);
			doc.insertString(doc.getLength(), title_jme_version + tab + tab + tab + txt_jme_version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_lwjgl_version + tab + tab + tab + txt_lwjgl_version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_opengl_version + tab + tab + tab + txt_opengl_version + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_gfx_memory + tab + txt_gfx_memory + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_gfx_vendor + tab + tab + txt_gfx_vendor + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_renderer + tab + tab + tab + txt_renderer + newLine, normalStyle);
			doc.insertString(doc.getLength(), title_gfx_capabilities + newLine + txt_gfx_capabilities + newLine, normalStyle);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// return back
		getTextPane_info().setCaretPosition(0);
	}

	/**
	 * @return
	 */
	private JScrollPane getScrollPane_info()
	{
		if (scrollPane_info == null)
		{
			scrollPane_info = new JScrollPane();
			scrollPane_info.setViewportView(getTextPane_info());
		}
		return scrollPane_info;
	}
}
