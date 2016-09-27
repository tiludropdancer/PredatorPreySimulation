package simulation;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael K������lling
 * @version 2011.07.31
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    /**
     * Create a view of the given width and height.
     * @param frame The root frame which this dialog has popped up from. 
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(Map<Class, Color> colors, int height, int width)
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        stats = new FieldStats();
        this.colors = colors;

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    public void close() {
    	WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    	Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    	setVisible(false);
        dispose();
    }
    
    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     * @return The simulation statistics up to this point.
     */
    public FieldStats showStatus(int step, Field field)
    {
        if(!isVisible()) {
            setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
        return stats;
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }

    private class GraphView extends JPanel {
    	private final int MAX_SCORE;
    	private final int PREF_W;
    	private final int PREF_H;
    	private final int BORDER_GAP = 30;
    	private final Color GRAPH_COLOR = Color.green;
    	private final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    	private final Stroke GRAPH_STROKE = new BasicStroke(3f);
    	private final int GRAPH_POINT_WIDTH = 12;
    	private final int Y_HATCH_CNT = 10;
    	private List<FieldStats> storage;

	   public GraphView(List<FieldStats> storage, int height, int width) {
	      this.storage = storage;
	      MAX_SCORE = getMaxCount();
	      PREF_H = height;
	      PREF_W = width;
	   }
	   
	   private int getMaxCount()
	   {
		   int maxCount = 0;
		   for (FieldStats stats : storage) {
			   maxCount = Math.max(maxCount, stats.getCount(Rabbit.class));
		   }
		   return maxCount;
	   }
	   
	   protected void paintComponent(Graphics g) {
		      super.paintComponent(g);
		      Graphics2D g2 = (Graphics2D)g;
		      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		      double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (storage.size() - 1);
		      double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

		      List<Point> graphPoints = new ArrayList<Point>();
		      for (int i = 0; i < storage.size(); i++) {
		         int x1 = (int) (i * xScale + BORDER_GAP);
		         int y1 = (int) ((MAX_SCORE - storage.get(i).getCount(Rabbit.class)) * yScale + BORDER_GAP);
		         graphPoints.add(new Point(x1, y1));
		      }

		      // create x and y axes 
		      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		      // create hatch marks for y axis. 
		      for (int i = 0; i < Y_HATCH_CNT; i++) {
		         int x0 = BORDER_GAP;
		         int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
		         int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
		         int y1 = y0;
		         g2.drawLine(x0, y0, x1, y1);
		      }

		      // and for x axis
		      for (int i = 0; i < storage.size() - 1; i++) {
		         int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (storage.size() - 1) + BORDER_GAP;
		         int x1 = x0;
		         int y0 = getHeight() - BORDER_GAP;
		         int y1 = y0 - GRAPH_POINT_WIDTH;
		         g2.drawLine(x0, y0, x1, y1);
		      }

		      Stroke oldStroke = g2.getStroke();
		      g2.setColor(GRAPH_COLOR);
		      g2.setStroke(GRAPH_STROKE);
		      for (int i = 0; i < graphPoints.size() - 1; i++) {
		         int x1 = graphPoints.get(i).x;
		         int y1 = graphPoints.get(i).y;
		         int x2 = graphPoints.get(i + 1).x;
		         int y2 = graphPoints.get(i + 1).y;
		         g2.drawLine(x1, y1, x2, y2);         
		      }

		      g2.setStroke(oldStroke);      
		      g2.setColor(GRAPH_POINT_COLOR);
		      for (int i = 0; i < graphPoints.size(); i++) {
		         int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
		         int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
		         int ovalW = GRAPH_POINT_WIDTH;
		         int ovalH = GRAPH_POINT_WIDTH;
		         g2.fillOval(x, y, ovalW, ovalH);
		      }
		   }

		   @Override
		   public Dimension getPreferredSize() {
		      return new Dimension(PREF_W, PREF_H);
		   }
    }
}
