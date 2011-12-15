import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Clock extends JFrame {

	private final class ClockComponent extends Component {
		private static final long serialVersionUID = 1L;
		private String text = "";

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

		public void updateTime() {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			text = sdf.format(cal.getTime());
			repaint();
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

			int lastOk = 4;

			ptSize = binaryFontSizeSearch(4, 1000, width, height, g2d);

			System.out.println("Setting ptsize to " + ptSize);

			g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,
					(int) (ptSize * 0.8)));
			Dimension dim = getTxtSize(testText, new Font(Font.SANS_SERIF,
					Font.PLAIN, (int) (ptSize * 0.8)), g2d);

			int textWidth = (int) dim.getWidth();
			int textHeight = (int) dim.getHeight();

			g2d.drawString(text, (width - textWidth) / 2, height
					- (height - textHeight) / 2);
			System.out.println("Height = " + height + " and textHeight = "
					+ textHeight);
		}
	}

	public Clock() {
		super("Big Fracking Clock");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		final ClockComponent clockComponent = new ClockComponent();

		clockComponent.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				clockComponent.repaint();
			}
		});

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clockComponent.updateTime();
			}
		});
		timer.start();

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
