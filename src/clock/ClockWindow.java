package clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

public class ClockWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private Color foreground;
	private Color background;
	private boolean wasAlwaysOnTop = false;

	private final DisplayComponent displayComponent = new DisplayComponent();
	private JFrame fullscreenFrame;
	private final JCheckBoxMenuItem alwaysOnTopItem = new JCheckBoxMenuItem(
			"Always on Top", false);

	public Color getDisplayForeground() {
		return foreground;
	}

	public void setDisplayForeground(Color foreground) {
		this.foreground = foreground;
		displayComponent.setDisplayForeground(foreground);
	}

	public Color getDisplayBackground() {
		return background;
	}

	public void setDisplayBackground(Color background) {
		this.background = background;
		displayComponent.setDisplayBackground(background);
	}

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

	public ClockWindow() {
		super("Big Fracking Clock");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		chooseLookAndFeel();

		setLayout(new BorderLayout());

		final JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem configureItem = new JMenuItem("Configure");
		configureItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationWindow configWindow = new ConfigurationWindow(
						ClockWindow.this);
				configWindow.setVisible(true);
			}
		});
		fileMenu.add(configureItem);

		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		JMenuItem fullscreenItem = new JMenuItem("Go to Full Screen");
		fullscreenItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goFullscreen();
			}
		});
		viewMenu.add(fullscreenItem);

		alwaysOnTopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAlwaysOnTop(alwaysOnTopItem.getState());
			}
		});
		viewMenu.add(alwaysOnTopItem);

		setJMenuBar(menuBar);
		menuBar.setVisible(false);

		final Timer menuTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuBar.setVisible(false);
			}
		});

		displayComponent.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				menuTimer.restart();
				menuBar.setVisible(true);
			}
		});

		ActionListener timeSetter = new ActionListener() {
			private String prevText = "";

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String newText = sdf.format(cal.getTime());

				if (!newText.equals(prevText)) {
					displayComponent.setText(newText);
					prevText = newText;
				}
			}
		};

		timeSetter.actionPerformed(null);
		Timer timer = new Timer(1000, timeSetter);
		timer.start();

		add(displayComponent, BorderLayout.CENTER);

		displayComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("control L"), "fullscreen");
		displayComponent.getActionMap().put("fullscreen", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				goFullscreen();
			}
		});

		displayComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "nofullscreen");
		displayComponent.getActionMap().put("nofullscreen",
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(final ActionEvent e) {
						backFromFullscreen();
					}
				});

		displayComponent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) { // Double-click
					if (fullscreenFrame == null) {
						goFullscreen();
					} else {
						backFromFullscreen();
					}
				}

			}
		});

		setSize(600, 400);
	}

	private void backFromFullscreen() {
		if (fullscreenFrame != null) {
			fullscreenFrame.dispose();
			fullscreenFrame = null;

			remove(displayComponent);
			add(displayComponent, BorderLayout.CENTER);
			invalidate();
			validate();
			repaint();
		}

		if (wasAlwaysOnTop) {
			alwaysOnTopItem.setState(true);
			setAlwaysOnTop(true);
		}

	}

	private void goFullscreen() {
		GraphicsDevice currentDevice = getGraphicsConfiguration().getDevice();
		wasAlwaysOnTop = alwaysOnTopItem.getState();
		alwaysOnTopItem.setState(false);
		setAlwaysOnTop(false);

		if (currentDevice.isFullScreenSupported()) {
			fullscreenFrame = new JFrame();

			// make frame undecorated and not resizeable
			fullscreenFrame.setUndecorated(true);
			fullscreenFrame.setResizable(false);

			Rectangle bounds = getGraphicsConfiguration().getBounds();
			fullscreenFrame.setBounds(bounds);

			fullscreenFrame.setLayout(new BorderLayout());
			fullscreenFrame.add(displayComponent, BorderLayout.CENTER);

			// go into full screen mode
			// currentDevice.setFullScreenWindow(fullscreenFrame);
			fullscreenFrame.setVisible(true);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClockWindow clock = new ClockWindow();
		clock.setVisible(true);
	}

}
