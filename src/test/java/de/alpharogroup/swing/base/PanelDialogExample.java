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
package de.alpharogroup.swing.base;

import de.alpharogroup.layout.CloseWindow;
import de.alpharogroup.layout.ScreenSizeExtensions;
import de.alpharogroup.model.BaseModel;
import de.alpharogroup.model.api.Model;
import de.alpharogroup.swing.components.factories.JComponentFactory;
import de.alpharogroup.swing.panels.login.pw.ChangePasswordModelBean;
import de.alpharogroup.swing.panels.login.pw.NewPasswordPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelDialogExample extends PanelDialog<ChangePasswordModelBean>
{

	private static final long serialVersionUID = 1L;

	public static void main(final String[] a)
	{
		final Frame frame = new Frame("BaseDialog");
		int screenID = ScreenSizeExtensions.getScreenID(frame);
		Dimension screenDimension = ScreenSizeExtensions.getScreenDimension(frame);
		System.out.println(screenID);
		System.out.println(screenDimension);
		frame.addWindowListener(new CloseWindow());
		final PanelDialogExample dialog = new PanelDialogExample(frame, "Password title", true,
			BaseModel.<ChangePasswordModelBean> of());

		dialog.setSize(500, 250);

		dialog.setVisible(true);
	}

	/** The button close. */
	private JButton buttonClose;

	public PanelDialogExample(final Frame owner, final String title, final boolean modal,
                              final Model<ChangePasswordModelBean> model)
	{
		super(owner, title, modal, model);
	}

	private void onClose(final ActionEvent e)
	{
		this.setVisible(false);
		System.exit(0);
	}

	@Override
	protected void onInitializeComponents()
	{
		super.onInitializeComponents();
		buttonClose = newButtonClose();
	}

	protected JButton newButtonClose(){
		JButton button = JComponentFactory.newJButton("Close");
		button.addActionListener(e -> onClose(e));
		return button;
	}

	@Override
	protected JPanel newContent(Model<ChangePasswordModelBean> model) {
		return new NewPasswordPanel(BaseModel.<ChangePasswordModelBean> of());
	}

	@Override
	protected JPanel newButtons(Model<ChangePasswordModelBean> model) {
		JPanel buttons = super.newButtons(model);
		return buttons;
	}

	@Override
	protected void onInitializeLayout()
	{
		super.onInitializeLayout();
		getButtons()
		.add(buttonClose, BorderLayout.EAST);
		final int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		final int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation((x / 3), (y / 3));
		setSize((x / 3), (y / 3));
	}


}