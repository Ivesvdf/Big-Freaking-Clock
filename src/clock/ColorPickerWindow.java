package clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorPickerWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	public ColorPickerWindow(Color initialColor, final ColorSetHandler handler) {
		super();
		setTitle("Color Picker");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JColorChooser colorChooser = new JColorChooser(initialColor);
		colorChooser.setBorder(BorderFactory
				.createTitledBorder("Pick Foreground Color"));

		add(colorChooser, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.set(colorChooser.getColor());
				dispose();
			}
		});
		buttonPane.add(okButton);
		buttonPane.add(Box.createHorizontalStrut(10));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createHorizontalStrut(10));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		add(buttonPane, BorderLayout.PAGE_END);
		pack();
		setVisible(true);
	}
}
