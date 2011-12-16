import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Clock extends JFrame {

	private Color foreground;
	private Color background;
	private final DisplayComponent clockComponent = new DisplayComponent();
	private JFrame fullscreenFrame;

	public Color getDisplayForeground() {
		return foreground;
	}

	public void setDisplayForeground(Color foreground) {
		this.foreground = foreground;
		clockComponent.setDisplayForeground(foreground);
	}

	public Color getDisplayBackground() {
		return background;
	}

	public void setDisplayBackground(Color background) {
		this.background = background;
		clockComponent.setDisplayBackground(background);
	}

	private final class DisplayComponent extends JComponent {
		private static final long serialVersionUID = 1L;
		private String text;
		private int xpos;
		private int ypos;
		private int ptSize;
		private Font currentFont = null;
		private int textWidth;
		private int textHeight;
		private Color foreground = Color.white;
		private Color background = Color.black;

		public Color getDisplayForeground() {
			return foreground;
		}

		public void setDisplayForeground(Color foreground) {
			this.foreground = foreground;
			repaint();
		}

		public Color getDisplayBackground() {
			return background;
		}

		public void setDisplayBackground(Color background) {
			this.background = background;
			repaint();
		}

		public DisplayComponent() {
			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					onResize();
				}
			});
		}

		public Dimension getTxtSize(String txt, Font f, Graphics2D g) {
			TextLayout layout = new TextLayout(txt, f, g.getFontRenderContext());
			Rectangle2D bounds = layout.getBounds();
			return new Dimension((int) bounds.getWidth(),
					(int) bounds.getHeight());
		}

		int binaryFontSizeSearch(int low, int high, int maxWidth,
				int maxHeight, Graphics2D g2d) {
			if (high < low) {
				return low; // not found
			}

			int mid = (low + high) / 2;

			Dimension dim = getTxtSize("88:88", new Font(Font.SANS_SERIF,
					Font.PLAIN, mid), g2d);

			if (dim.getHeight() > maxHeight || dim.getWidth() > maxWidth) {
				return binaryFontSizeSearch(low, mid - 1, maxWidth, maxHeight,
						g2d);
			} else if (dim.getHeight() < maxHeight && dim.getWidth() < maxWidth) {
				return binaryFontSizeSearch(mid + 1, high, maxWidth, maxHeight,
						g2d);
			} else {
				return mid;
			}
		}

		public void setText(String text) {
			this.text = text;
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			if (currentFont == null) {
				reposition((Graphics2D) g);
			}

			Dimension size = getSize();
			int height = size.height;
			int width = size.width;
			Graphics2D g2d = (Graphics2D) g;

			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g2d.setBackground(background);
			g2d.clearRect(0, 0, width, height);
			g2d.setColor(foreground);

			g2d.setFont(currentFont);

			if (text != null) {
				TextLayout layout = new TextLayout(text, currentFont,
						g2d.getFontRenderContext());
				layout.draw(g2d, xpos - (int) layout.getBounds().getMinX(),
						ypos);
				// g2d.drawString(text, xpos, ypos);
			}
			System.out.println("Painting");

			// g2d.drawRect(xpos, ypos - textHeight, textWidth, textHeight);
		}

		public void reposition(Graphics2D g2d) {
			Dimension size = getSize();
			int height = size.height;
			int width = size.width;

			ptSize = binaryFontSizeSearch(4, 1000, width, height, g2d);

			Dimension dim = getTxtSize(text, new Font(Font.SANS_SERIF,
					Font.PLAIN, (int) (ptSize * 0.8)), g2d);

			currentFont = new Font(Font.SANS_SERIF, Font.PLAIN,
					(int) (ptSize * 0.8));

			textWidth = (int) dim.getWidth();
			textHeight = (int) dim.getHeight();

			xpos = (width - textWidth) / 2;
			ypos = height - (height - textHeight) / 2;

			System.out.println("Total Width = " + width + ", Total Height = "
					+ height);
			System.out.println("Text Width = " + textWidth + ", Text Height = "
					+ textHeight);
			System.out.println("Repositioning to x=" + xpos + ",y=" + ypos
					+ ",size=" + ptSize + "pt");
			System.out.println("Left=" + xpos + ", Right="
					+ (width - textWidth - xpos) + ", Top = " + (height - ypos)
					+ ", Bottom = " + (height - textHeight - (height - ypos)));

		}

		private void onResize() {
			reposition((Graphics2D) getGraphics());
			repaint();
		}
	}

	public Clock() {
		super("Big Fracking Clock");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		final JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem configureItem = new JMenuItem("Configure");
		configureItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationWindow configWindow = new ConfigurationWindow(
						Clock.this);
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

		setJMenuBar(menuBar);
		menuBar.setVisible(false);

		final Timer menuTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuBar.setVisible(false);
			}
		});

		clockComponent.addMouseMotionListener(new MouseMotionListener() {
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
					clockComponent.setText(newText);
					prevText = newText;
				}
			}
		};

		timeSetter.actionPerformed(null);
		Timer timer = new Timer(1000, timeSetter);
		timer.start();

		add(clockComponent, BorderLayout.CENTER);

		clockComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("control L"), "fullscreen");
		clockComponent.getActionMap().put("fullscreen", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				goFullscreen();
			}
		});

		clockComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "nofullscreen");
		clockComponent.getActionMap().put("nofullscreen", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				backFromFullscreen();
			}
		});

		clockComponent.addMouseListener(new MouseAdapter() {
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

			remove(clockComponent);
			add(clockComponent, BorderLayout.CENTER);
			invalidate();
			validate();
			repaint();
		}

	}

	private void goFullscreen() {
		GraphicsDevice currentDevice = getGraphicsConfiguration().getDevice();
		Rectangle bounds = getGraphicsConfiguration().getBounds();

		if (currentDevice.isFullScreenSupported()) {
			fullscreenFrame = new JFrame();

			// make frame undecorated and not resizeable
			fullscreenFrame.setUndecorated(true);
			fullscreenFrame.setResizable(false);

			fullscreenFrame.setBounds(bounds);

			fullscreenFrame.setLayout(new BorderLayout());
			fullscreenFrame.add(clockComponent, BorderLayout.CENTER);

			// go into full screen mode
			// currentDevice.setFullScreenWindow(fullscreenFrame);
			fullscreenFrame.setVisible(true);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Clock clock = new Clock();
		clock.setVisible(true);
	}

}
