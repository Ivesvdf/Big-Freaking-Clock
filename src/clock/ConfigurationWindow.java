package clock;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ConfigurationWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	ConfigurationWindow(final ClockWindow clock) {
		super(clock);
		setTitle("Properties");
		setSize(600, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 0.2;
		c.insets = new Insets(10, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;

		JButton setForeground = new JButton("Set Foreground Color");
		setForeground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ColorPickerWindow(getForeground(), new ColorSetHandler() {
					@Override
					public void set(Color newColor) {
						clock.setDisplayForeground(newColor);
					}
				});
			}
		});
		add(setForeground, c);

		c.gridx = 1;
		add(new JLabel(""), c);
		c.gridx = 2;
		add(new JLabel(""), c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10, 10, 10, 10);

		JButton setBackground = new JButton("Set Background Color");
		setBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ColorPickerWindow(getBackground(), new ColorSetHandler() {
					@Override
					public void set(Color newColor) {
						clock.setDisplayBackground(newColor);
					}
				});
			}
		});
		add(setBackground, c);

		pack();
	}
}
