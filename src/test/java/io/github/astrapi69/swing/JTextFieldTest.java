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
package io.github.astrapi69.swing;

import java.awt.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import io.github.astrapi69.model.PropertyModel;
import io.github.astrapi69.window.adapter.CloseWindow;

public class JTextFieldTest
{
	public static void main(String[] args)
	{
		// Bind legacy JTextField with a property model
		JTextField textField = new JTextField("fff", 20);

		final PropertyModel<String> propertyModel = PropertyModel.<String> of(textField, "text");
		final Frame frame = new Frame("JCheckBoxDecoratorTest");
		JButton buttonCheck = new JButton("check it");
		buttonCheck.addActionListener(e -> {
			String selected = propertyModel.getObject();
			selected = textField.getText();
			System.out.println(selected);
		});
		frame.addWindowListener(new CloseWindow());

		frame.setLayout(new MigLayout());
		frame.add(buttonCheck);
		frame.add(textField);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}

}