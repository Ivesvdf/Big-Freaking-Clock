package clock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

final class DisplayComponent extends JComponent {
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