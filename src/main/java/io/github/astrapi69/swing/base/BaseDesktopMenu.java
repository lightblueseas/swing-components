/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.swing.base;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.help.CSH;
import javax.help.DefaultHelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.WindowPresentation;
import javax.swing.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import io.github.astrapi69.swing.actions.OpenBrowserToDonateAction;
import io.github.astrapi69.swing.actions.ShowInfoDialogAction;
import io.github.astrapi69.swing.actions.ShowLicenseFrameAction;
import io.github.astrapi69.swing.dialog.info.InfoDialog;
import io.github.astrapi69.swing.dialog.info.InfoPanel;
import io.github.astrapi69.swing.help.HelpFactory;
import io.github.astrapi69.swing.menu.MenuExtensions;
import io.github.astrapi69.swing.menu.MenuFactory;
import io.github.astrapi69.swing.plaf.actions.LookAndFeelGTKAction;
import io.github.astrapi69.swing.plaf.actions.LookAndFeelMetalAction;
import io.github.astrapi69.swing.plaf.actions.LookAndFeelMotifAction;
import io.github.astrapi69.swing.plaf.actions.LookAndFeelNimbusAction;
import io.github.astrapi69.swing.plaf.actions.LookAndFeelSystemAction;

/**
 * The class {@link BaseDesktopMenu} holds the base menu items for an application
 */
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
@Log
public class BaseDesktopMenu extends JMenu
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The application frame. */
	final Component applicationFrame;
	/** The default help broker */
	final DefaultHelpBroker helpBroker;
	/** The help window. */
	final Window helpWindow;
	/** The JMenuBar from the DesktopMenu. */
	final JMenuBar menubar;
	/** The edit menu. */
	JMenu editMenu;
	/** The file menu. */
	JMenu fileMenu;
	/** The help menu. */
	JMenu helpMenu;
	/** The look and feel menu. */
	JMenu lookAndFeelMenu;

	/**
	 * Instantiates a new {@link BaseDesktopMenu}
	 *
	 * @param applicationFrame
	 *            the application frame
	 */
	public BaseDesktopMenu(@NonNull Component applicationFrame)
	{
		this.applicationFrame = applicationFrame;
		helpBroker = newHelpBroker();
		helpWindow = newHelpWindow(helpBroker);
		menubar = newJMenuBar();
		fileMenu = newFileMenu();
		fileMenu = menubar.add(fileMenu);
		editMenu = newEditMenu(null);
		editMenu = menubar.add(editMenu);
		lookAndFeelMenu = newLookAndFeelMenu(null);
		lookAndFeelMenu = menubar.add(lookAndFeelMenu);
		helpMenu = newHelpMenu(null);
		helpMenu = menubar.add(helpMenu);
		onRefreshMenus(fileMenu, editMenu, lookAndFeelMenu, helpMenu);
	}

	/**
	 * Gets the help set.
	 *
	 * @return the help set
	 */
	public HelpSet getHelpSet()
	{
		HelpSet hs = null;
		final String filename = "simple-hs.xml";
		String directory = "help";
		final String path = directory + "/" + filename;
		try
		{
			hs = HelpFactory.newHelpSet(directory, filename);
		}
		catch (final HelpSetException e)
		{
			String title = e.getLocalizedMessage();
			String htmlMessage = "<html><body width='650'>" + "<h2>" + title + "</h2>" + "<p>"
				+ e.getMessage() + "\n" + path;
			JOptionPane.showMessageDialog(this.getParent(), htmlMessage, title,
				JOptionPane.ERROR_MESSAGE);
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return hs;
	}

	/**
	 * Creates the file menu.
	 *
	 * @param listener
	 *            the listener
	 *
	 * @return the j menu
	 */
	protected JMenu newEditMenu(final ActionListener listener)
	{
		final JMenu menu = new JMenu("Edit");
		menu.setMnemonic('E');

		return menu;
	}

	/**
	 * Factory method for create the file <code>JMenu</code>
	 *
	 * @return the j menu
	 */
	protected JMenu newFileMenu()
	{
		return MenuFactory.newJMenu("File", KeyEvent.VK_F);
	}

	protected DefaultHelpBroker newHelpBroker()
	{
		final HelpSet hs = getHelpSet();
		final DefaultHelpBroker helpBroker = (DefaultHelpBroker)hs.createHelpBroker();

		return helpBroker;
	}

	/**
	 * Creates the help menu.
	 *
	 * @param listener
	 *            the listener
	 * @return the j menu
	 */
	protected JMenu newHelpMenu(final ActionListener listener)
	{
		// Help menu
		final JMenu menuHelp = new JMenu(newLabelTextHelp());
		menuHelp.setMnemonic('H');

		// Help JMenuItems
		// Help content
		final JMenuItem mihHelpContent = MenuFactory.newJMenuItem(newLabelTextContent(), 'c', 'H');
		menuHelp.add(mihHelpContent);

		// 2. assign help to components
		CSH.setHelpIDString(mihHelpContent, newLabelTextOverview());
		// 3. handle events
		final CSH.DisplayHelpFromSource displayHelpFromSource = new CSH.DisplayHelpFromSource(
			helpBroker);
		mihHelpContent.addActionListener(displayHelpFromSource);
		// Donate
		final JMenuItem mihDonate = new JMenuItem(newLabelTextDonate());
		mihDonate.addActionListener(
			newOpenBrowserToDonateAction(newLabelTextDonate(), applicationFrame));
		menuHelp.add(mihDonate);
		// Licence
		final JMenuItem mihLicence = new JMenuItem(newLabelTextLicence());
		mihLicence.addActionListener(
			newShowLicenseFrameAction(newLabelTextLicence() + "Action", newLabelTextLicence()));
		menuHelp.add(mihLicence);
		// Info
		final JMenuItem mihInfo = new JMenuItem(newLabelTextInfo(), 'i'); // $NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(mihInfo, 'I');

		mihInfo.addActionListener(newShowInfoDialogAction(newLabelTextInfo(),
			(Frame)getApplicationFrame(), newLabelTextInfo()));
		menuHelp.add(mihInfo);

		return menuHelp;
	}

	protected Window newHelpWindow(final DefaultHelpBroker helpBroker)
	{
		// found bug with the javax.help
		// Exception in thread "main" java.lang.SecurityException: no manifiest
		// section for signature file entry
		// com/sun/java/help/impl/TagProperties.class
		// Solution is to remove the rsa files from the jar
		final WindowPresentation pres = helpBroker.getWindowPresentation();
		pres.createHelpWindow();
		Window helpWindow = pres.getHelpWindow();

		helpWindow.setLocationRelativeTo(null);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (final Exception e)
		{
			String title = e.getLocalizedMessage();
			String htmlMessage = "<html><body width='650'>" + "<h2>" + title + "</h2>" + "<p>"
				+ e.getMessage();
			JOptionPane.showMessageDialog(this.getParent(), htmlMessage, title,
				JOptionPane.ERROR_MESSAGE);
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		SwingUtilities.updateComponentTreeUI(helpWindow);
		return helpWindow;
	}

	/**
	 * Creates a new {@link JMenuBar}
	 *
	 * @return the new {@link JMenuBar}
	 */
	protected JMenuBar newJMenuBar()
	{
		return new JMenuBar();
	}

	protected String newLabelTextApplicationName()
	{
		return "";
	}

	protected String newLabelTextContent()
	{
		return "Content";
	}


	protected String newLabelTextCopyright()
	{
		return "";
	}


	protected String newLabelTextDonate()
	{
		return "Donate";
	}


	protected String newLabelTextHelp()
	{
		return "Help";
	}


	protected String newLabelTextInfo()
	{
		return "Info";
	}


	protected String newLabelTextLabelApplicationName()
	{
		return "";
	}


	protected String newLabelTextLabelCopyright()
	{
		return "";
	}

	protected String newLabelTextLabelVersion()
	{
		return "";
	}

	protected String newLabelTextLicence()
	{
		return "Licence";
	}

	protected String newLabelTextOverview()
	{
		return "Overview";
	}


	protected String newLabelTextVersion()
	{
		return "";
	}

	/**
	 * Creates the look and feel menu.
	 *
	 * @param listener
	 *            the listener
	 * @return the j menu
	 */
	protected JMenu newLookAndFeelMenu(final ActionListener listener)
	{

		final JMenu menuLookAndFeel = new JMenu("Look and Feel");
		menuLookAndFeel.setMnemonic('L');

		// Look and Feel JMenuItems
		// GTK
		JMenuItem jmiPlafGTK;
		jmiPlafGTK = new JMenuItem("GTK", 'g'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafGTK, 'G');
		jmiPlafGTK.addActionListener(new LookAndFeelGTKAction("GTK", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafGTK);
		// Metal default Metal theme
		JMenuItem jmiPlafMetal;
		jmiPlafMetal = new JMenuItem("Metal", 'm'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafMetal, 'M');
		jmiPlafMetal.addActionListener(new LookAndFeelMetalAction("Metal", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafMetal);
		// Metal Ocean theme
		JMenuItem jmiPlafOcean;
		jmiPlafOcean = new JMenuItem("Ocean", 'o'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafOcean, 'O');
		jmiPlafOcean.addActionListener(new LookAndFeelMetalAction("Ocean", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafOcean);
		// Motif
		JMenuItem jmiPlafMotiv;
		jmiPlafMotiv = new JMenuItem("Motif", 't'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafMotiv, 'T');
		jmiPlafMotiv.addActionListener(new LookAndFeelMotifAction("Motif", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafMotiv);
		// Nimbus
		JMenuItem jmiPlafNimbus;
		jmiPlafNimbus = new JMenuItem("Nimbus", 'n'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafNimbus, 'N');
		jmiPlafNimbus
			.addActionListener(new LookAndFeelNimbusAction("Nimbus", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafNimbus);
		// Windows
		JMenuItem jmiPlafSystem;
		jmiPlafSystem = new JMenuItem("System", 'd'); //$NON-NLS-1$
		MenuExtensions.setCtrlAccelerator(jmiPlafSystem, 'W');
		jmiPlafSystem
			.addActionListener(new LookAndFeelSystemAction("System", this.applicationFrame));
		menuLookAndFeel.add(jmiPlafSystem);

		return menuLookAndFeel;

	}

	protected OpenBrowserToDonateAction newOpenBrowserToDonateAction(final String name,
		final @NonNull Component component)
	{
		return new OpenBrowserToDonateAction(name, component);
	}

	@SuppressWarnings("serial")
	protected ShowInfoDialogAction newShowInfoDialogAction(final String name,
		final @NonNull Frame owner, final @NonNull String title)
	{
		return new ShowInfoDialogAction(name, owner, title)
		{
			@Override
			protected JDialog newJDialog(Frame frame, String s)
			{
				return BaseDesktopMenu.this.onNewInfoDialog(owner, title);
			}
		};
	}

	protected ShowLicenseFrameAction newShowLicenseFrameAction(final String name,
		final @NonNull String title)
	{
		return new ShowLicenseFrameAction(name, title)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String newLicenseText()
			{
				return onNewLicenseText();
			}
		};
	}

	protected String newTextWarning()
	{
		return "";
	}

	@SuppressWarnings("serial")
	protected InfoDialog onNewInfoDialog(Frame owner, String title)
	{
		return new InfoDialog(owner, title)
		{

			@Override
			protected InfoPanel newInfoPanel()
			{
				return new InfoPanel()
				{

					@Override
					protected String newLabelTextApplicationName()
					{
						return BaseDesktopMenu.this.newLabelTextApplicationName();
					}

					@Override
					protected String newLabelTextCopyright()
					{
						return BaseDesktopMenu.this.newLabelTextCopyright();
					}

					@Override
					protected String newLabelTextLabelApplicationName()
					{
						return BaseDesktopMenu.this.newLabelTextLabelApplicationName();
					}

					@Override
					protected String newLabelTextLabelCopyright()
					{
						return BaseDesktopMenu.this.newLabelTextLabelCopyright();
					}

					@Override
					protected String newLabelTextLabelVersion()
					{
						return BaseDesktopMenu.this.newLabelTextLabelVersion();
					}

					@Override
					protected String newLabelTextVersion()
					{
						return BaseDesktopMenu.this.newLabelTextVersion();
					}

					@Override
					protected String newTextWarning()
					{
						return BaseDesktopMenu.this.newTextWarning();
					}

				};
			}

			@Override
			protected String newLabelTextPlaceholder()
			{
				return "";
			}

		};
	}

	protected String onNewLicenseText()
	{
		return "Licence Text";
	}

	protected void onRefreshMenus(JMenu... menus)
	{
		for (JMenu menu : menus)
		{
			refreshMenu(menu);
		}
	}

	protected void refreshMenu(JMenu menu)
	{
		MenuElement[] subElements = menu.getSubElements();
		menu.setVisible(subElements.length != 0);
	}

}