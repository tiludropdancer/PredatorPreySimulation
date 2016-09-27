import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A linear graph of the simulation.
 */
public class SimulatorGraph extends JFrame {
	// Color used for objects that have no defined color.
	private static final Color UNKNOWN_COLOR = Color.gray;

	private GraphView graphView;

	// A map for storing colors for participants in the simulation
	private Map<Class, Color> colors;

    private SimulatorStats stats;
    private int maxValue;
    private int numValues;
	
	/**
	 * Create a view of the given width and height.
	 * 
	 * @param frame  The root frame which this dialog has popped up from.
	 * @param height The simulation's height.
	 * @param width  The simulation's width.
	 */
	public SimulatorGraph(Map<Class, Color> colors, int height, int width) 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.colors = colors;

		setTitle("Fox and Rabbit Simulation Graph");

		 setLocation(100, 50);

		graphView = new GraphView(height, width);

		Container contents = getContentPane();
		contents.add(graphView, BorderLayout.CENTER);
		pack();
	}
	
    public void close() { //written with the help of Andrey Radchenko
    	WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    	Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    	setVisible(false);
        dispose();
    }
    
	/**
	 * @return The color to be used for a given class of animal.
	 */
	private Color getColor(Class animalClass) {
		Color col = colors.get(animalClass);
		if (col == null) {
			// no color defined for this class
			return UNKNOWN_COLOR;
		} else {
			return col;
		}
	}

	public void draw(SimulatorStats stats) {
		this.stats = stats;
		maxValue = getMaxValue();
		numValues = stats.getSize();
		graphView.repaint();
	}

	private int getMaxValue() {
		int maxValue = 0;
		for (Class animalClass : colors.keySet()) {
			for (int count : stats.getCounts(animalClass)) {
				maxValue = Math.max(maxValue, count);
			}
		}
		return maxValue;
	}

	private class GraphView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;
		
        private int padding = 25;
        private int labelPadding = 35;
        private Color gridColor = new Color(200, 200, 200, 200);
        private int tickWidth = 5;
		private final Stroke GRAPH_STROKE = new BasicStroke(2f);
		private final int NUM_X_TICKS = 50;
		private final int NUM_Y_TICKS = 20;
		
		private int graphWidth, graphHeight;
        
		public GraphView(int height, int width) {
			graphWidth = width * GRID_VIEW_SCALING_FACTOR;
			graphHeight = height * GRID_VIEW_SCALING_FACTOR;
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(graphWidth, graphHeight);
		}
		
		private List<Point> getGraphPoints(Class animalClass, double xScale, double yScale) {
			List<Point> graphPoints = new ArrayList<Point>();
			int i = 0;
			for (int count : stats.getCounts(animalClass)) {
				int x = (int) (i * xScale + padding + labelPadding);
				int y = (int) ((maxValue - count) * yScale + padding);
				graphPoints.add(new Point(x, y));
				i++;
			}
			return graphPoints;
		}
		
		private void drawLine(Graphics2D g2, List<Point> graphPoints, Color lineColor) {
			Stroke oldStroke = g2.getStroke();
			g2.setColor(lineColor);
			g2.setStroke(GRAPH_STROKE);
			for (int i = 0; i < graphPoints.size() - 1; i++) {
				int x1 = graphPoints.get(i).x;
				int y1 = graphPoints.get(i).y;
				int x2 = graphPoints.get(i + 1).x;
				int y2 = graphPoints.get(i + 1).y;
				g2.drawLine(x1, y1, x2, y2);
			}
			g2.setStroke(oldStroke);
		}
		
		protected void paintComponent(Graphics g) { //written with the help of Andrey Radchenko
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (numValues - 1);
			double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxValue - 1);

			// draw tick marks and grid lines for y axis
	        for (int i = 0; i < NUM_Y_TICKS + 1; i++) {
	            int x0 = padding + labelPadding;
	            int x1 = tickWidth + x0;
	            int y0 = getHeight() - (i * (getHeight() - padding * 2 - labelPadding) / NUM_Y_TICKS + padding + labelPadding);
	            int y1 = y0;
	            if (numValues > 0) {
	                g2.setColor(gridColor);
	                g2.drawLine(x0 + 1 + tickWidth, y0, getWidth() - padding, y1);
	                g2.setColor(Color.black);
	                String yLabel = Integer.toString((int) (((maxValue * ((i * 1.0) / NUM_Y_TICKS)) * 100) / 100.0));
	                FontMetrics metrics = g2.getFontMetrics();
	                int labelWidth = metrics.stringWidth(yLabel);
	                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
	            }
	            g2.drawLine(x0, y0, x1, y1);
	        }
            int x = padding + labelPadding;
            int y = getHeight() - (((NUM_Y_TICKS + 1) * (getHeight() - padding * 2 - labelPadding)) / NUM_Y_TICKS + padding + labelPadding);
            String yLabel = "Counts";
            FontMetrics lblMetrics = g2.getFontMetrics();
            int lblWidth = lblMetrics.stringWidth(yLabel);
            g2.drawString(yLabel, x - lblWidth - 5, y + (lblMetrics.getHeight() / 2) - 3);
			
			// draw tick marks and grid lines for x axis
	        for (int i = 0; i < NUM_X_TICKS; i++) {
	            if (numValues > 1) {
	                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (NUM_X_TICKS - 1) + padding + labelPadding;
	                int x1 = x0;
	                int y0 = getHeight() - padding - labelPadding;
	                int y1 = y0 - tickWidth;
	                if ((i % ((int) ((NUM_X_TICKS / 20.0)) + 1)) == 0) {
	                    g2.setColor(gridColor);
	                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - tickWidth, x1, padding);
	                    g2.setColor(Color.black);
	                    String xLabel = Integer.toString((int)(i * numValues * 1.0 / NUM_X_TICKS));
	                    FontMetrics metrics = g2.getFontMetrics();
	                    int labelWidth = metrics.stringWidth(xLabel);
	                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
	                }
	                g2.drawLine(x0, y0, x1, y1);
	            }
	        }
	        x = getWidth() - padding;
            y = getHeight() - (( - (getHeight() - padding * 2 - labelPadding)) / NUM_Y_TICKS + padding + labelPadding);
            String xLabel = "Steps";
            lblMetrics = g2.getFontMetrics();
            lblWidth = lblMetrics.stringWidth(xLabel);
            g2.drawString(xLabel, x - lblWidth, y + lblMetrics.getHeight() + 3);
            
            x = padding + labelPadding;
            xLabel = "Legend:";
            lblMetrics = g2.getFontMetrics();
            lblWidth = lblMetrics.stringWidth(xLabel);
            x -= lblWidth + 5;
            g2.drawString(xLabel, x, y + lblMetrics.getHeight() + 3);
            x += lblWidth;
			for (Class animalClass : colors.keySet()) {
				String animalName = "  "+animalClass.getSimpleName();
				Color animalColor = colors.get(animalClass);
                g2.setColor(animalColor);
	            lblMetrics = g2.getFontMetrics();
	            lblWidth = lblMetrics.stringWidth(animalName);
	            g2.drawString(animalName, x, y + lblMetrics.getHeight() + 3);
	            x += lblWidth;
            }
            g2.setColor(Color.black);

            // x and y axes
	        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
	        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

	        
			for (Class animalClass : colors.keySet()) {
				List<Point> graphPoints = getGraphPoints(animalClass, xScale, yScale);
				drawLine(g2, graphPoints, getColor(animalClass));
			}
		}

	}
}
