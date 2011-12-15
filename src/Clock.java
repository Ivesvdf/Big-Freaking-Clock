import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Clock extends JFrame {

	public Clock() {
		super("Big Fracking Clock");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		final Component clockComponent = new Component() {
			private static final long serialVersionUID = 1L;

			public Dimension getTxtSize(String txt, Font f, Graphics2D g) {
				TextLayout layout = new TextLayout(txt, f,
						g.getFontRenderContext());
				Rectangle2D bounds = layout.getBounds();
				return new Dimension((int) bounds.getWidth(),
						(int) bounds.getHeight());
			}

			@Override
			public void paint(Graphics g) {
				Dimension size = getSize();
				int height = size.height;
				int width = size.width;

				Graphics2D g2d = (Graphics2D) g;

				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
				g2d.setBackground(Color.black);
				g2d.clearRect(0, 0, width, height);
				setBackground(Color.black);
				g2d.setColor(Color.white);

				boolean lastFit = true;
				int ptSize = 4;
				String testText = "88:88";

				while (lastFit) {
					Dimension dim = getTxtSize(testText, new Font(
							Font.SANS_SERIF, Font.PLAIN, ptSize), g2d);

					if (dim.getHeight() > height || dim.getWidth() > width) {
						lastFit = false;
					} else {
						ptSize++;
					}
				}

				g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,
						(int) (ptSize * 0.8)));
				Dimension dim = getTxtSize(testText, new Font(Font.SANS_SERIF,
						Font.PLAIN, (int) (ptSize * 0.8)), g2d);

				int textWidth = (int) dim.getWidth() + 1;
				int textHeight = (int) dim.getHeight() + 1;

				g2d.drawString("23:50", (width - textWidth) / 2, height
						- (height - textHeight) / 2);
				System.out.println("Height = " + height + " and textHeight = "
						+ textHeight);
			}
		};

		clockComponent.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				clockComponent.repaint();
			}
		});

		add(clockComponent, BorderLayout.CENTER);

		setSize(600, 400);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Clock clock = new Clock();
		clock.setVisible(true);
	}

}
