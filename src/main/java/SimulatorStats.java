import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class collects and provides the sequences of statistical 
 * data on the state of a field during simulation.
 */
public class SimulatorStats {
	private int size;
	// Animal classes which participate in simulation.
	private final Set<Class> animalClasses;
	// This map contains the sequences of animal counts during simulation.
	private final Map<Class, List<Integer>> counts;
	
	public SimulatorStats(Set<Class> animalClasses)
	{
		size = 0;
		this.animalClasses = animalClasses;
    	counts = new HashMap<Class, List<Integer>>();
    	for (Class animalClass : animalClasses)
    	{
    		counts.put(animalClass, new LinkedList<Integer>());
    	}
	}
	
	/**
	 * Add the field stats.
	 * @param fieldStats The statistics on the field.
	 */
	void addStats(FieldStats stats) 
	{
		size++;
		for (Class animalClass : animalClasses) 
		{
			List<Integer> animalCounts = counts.get(animalClass);
			animalCounts.add(stats.getCount(animalClass));
		}
	}
	
	/**
	 * @return The length of sequences collected during simulation.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Get the sequence of counts for the animal.
	 * @param animalClass The class of the animal.
	 * @return the sequence of animal counts during simulation.
	 */
	public List<Integer> getCounts(Class animalClass) 
	{
		return counts.get(animalClass);
	}

}
