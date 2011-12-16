package clock;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class ConfigurationWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	private void chooseLookAndFeel() {
		try {
			boolean found = false;
			for (final LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					found = true;
					break;
				}
			}
			if (!found) {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			}
		} catch (final Exception e) {
		}
	}

	ConfigurationWindow(final ClockWindow clock) {
		super(clock);
		chooseLookAndFeel();
		setTitle("Properties");
		setSize(600, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(3, 3));

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 0;
		c.insets = new Insets(10, 10, 10, 10);
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

		c.gridy = 1;
		JButton setBackground = new JButton("Set Background Color");
		add(setBackground, c);
	}
}
