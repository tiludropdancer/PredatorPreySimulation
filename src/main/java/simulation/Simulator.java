package simulation;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael K������lling
 * @version 2011.07.31
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a coyote will be created in any given grid position.
    private static final double COYOTE_CREATION_PROBABILITY = 0.03;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // The probability that a hunter will be created in any given grid position.
    private static final double HUNTER_CREATION_PROBABILITY = 0.02;

    // List of animals in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    private SimulatorGraph graph;
    private List<Dictionary> dict;
    // The mapping of animal colors
    private Map<Class, Color> colors;
    // The series of animal counts during simulation.
    private SimulatorStats statistics;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator(Control control)
    {
    	// Take the dictionary of animal kinds from control object
    	dict = control.getCollection();
    	
    	actors = new ArrayList<Actor>();
        field = new Field(DEFAULT_DEPTH, DEFAULT_WIDTH);

        // Set up the animal colors
        colors = new HashMap<Class, Color>();
        colors.put(Rabbit.class, Color.orange);
        colors.put(Fox.class, Color.blue);
        colors.put(Coyote.class, Color.green);
        colors.put(Hunter.class, Color.red);
        
        // Create a view of the state of each location in the field.
        view = new SimulatorView(colors, DEFAULT_DEPTH, DEFAULT_WIDTH);

        // Create a graph view.
        graph = new SimulatorGraph(colors, DEFAULT_DEPTH, DEFAULT_WIDTH);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(final int numSteps)
    {
    	//written with the help of Andrey Radchenko
    	// We have to run simulation in the Swing background thread to yield to its 
    	// event dispatcher thread and let it repaint the field as simulation progresses. 
    	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			protected Void doInBackground() throws Exception { //written with the help of Andrey Radchenko
		        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
		            simulateOneStep();
		        }
	            return null;
			}

			protected void done() {
		        try {
		            SwingUtilities.invokeLater(new Runnable() { //written with the help of Andrey Radchenko
		                public void run() {
		                    view.close();
		    				graph.setVisible(true);
		    				graph.draw(statistics);
		                }
		            });
		        } catch (Exception e) {
		        }
			}
			
    	};
    	worker.execute();
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn animals.
        List<Actor> newActors = new ArrayList<Actor>();        
        // Let all rabbits act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
        	Actor actor = it.next();
        	actor.act(newActors);
            if(! actor.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born animals to the main lists.
        actors.addAll(newActors);

        // Here we schedule repainting of the field to the Swing event dispatcher thread.
        try {
            SwingUtilities.invokeAndWait(new Runnable() { //written with the help of Andrey Radchenko
                public void run() {
                    FieldStats stats = view.showStatus(step, field);
                    statistics.addStats(stats);
                }
            });
        } catch (Exception e) {
        }
        
    }
        
    /**
     * Close finished simulation and discard its resources.
     */
    public void close()
    {
        view.close();
        graph.close();
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        actors.clear();
        statistics = new SimulatorStats(colors.keySet());
        populate();
        
        // Show the starting state in the view.
        FieldStats stats = view.showStatus(step, field);
        statistics.addStats(stats);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
            	if(rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                	Dictionary h = dict.get(3);
                    Location location = new Location(row, col);
                    Hunter hunter = new Hunter(h.getMaxAge(), h.getHuntAge(), true, field, location);
                    actors.add(hunter);
                }
            	else if(rand.nextDouble() <= COYOTE_CREATION_PROBABILITY) {
            		Dictionary c = dict.get(2);
                    Location location = new Location(row, col);
                    Coyote coyote = new Coyote(c.getMaxAge(), c.getBreedAge(), c.getMaxLitter(), true, field, location);
                    actors.add(coyote);
                }
            	else if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
            		Dictionary f = dict.get(1);
                    Location location = new Location(row, col);
                    Fox fox = new Fox(f.getMaxAge(), f.getBreedAge(), f.getMaxLitter(), true, field, location);
                    actors.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                	Dictionary r = dict.get(0);
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(r.getMaxAge(), r.getBreedAge(), r.getMaxLitter(), true, field, location);
                    actors.add(rabbit);
                }
                // else leave the location empty.
            }
        }
    }

}
