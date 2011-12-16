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
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorPicker extends JDialog {
	final private ColorSetHandler handler;

	public ColorPicker(Color initialColor, final ColorSetHandler handler) {
		super();
		setTitle("Color Picker");

		this.handler = handler;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JColorChooser colorChooser = new JColorChooser(initialColor);
		colorChooser.setBorder(BorderFactory
				.createTitledBorder("Pick Foreground Color"));

		ColorSelectionModel model = colorChooser.getSelectionModel();
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				Color newForegroundColor = colorChooser.getColor();

				// label.setForeground(newForegroundColor);
			}
		};
		model.addChangeListener(changeListener);
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
