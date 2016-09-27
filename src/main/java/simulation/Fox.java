import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael K��lling
 * @version 2011.07.31
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).

    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.45;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).

    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(int max_age, int breed_age, int max_litter, boolean randomAge, Field field, Location location)
    {
        super(max_age, breed_age, max_litter, randomAge, field, location);
        if(randomAge) {
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Actor> newFoxes)
    {
    	try {
	        incrementAge();
	        incrementHunger();
	        if(isAlive()) {
	            giveBirth(newFoxes);            
	            // Move towards a source of food if found.
	            Location newLocation = findFood();
	            if(newLocation == null) { 
	                // No food found - try to move to a free location.
	                newLocation = getField().freeAdjacentLocation(getLocation());
	            }
	            // See if it was possible to move.
	            if(newLocation != null) {
	                setLocation(newLocation);
	            }
	            else {
	                // Overcrowding.
	                setDead();
	            }
	        }
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newFoxes)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(getMaxAge(), getBreedAge(), getMaxLitter(), false, field, loc);
            newFoxes.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
    	int births = 0;
        Field field = getField();
        List<Location> male = field.getMaleAdjacentLocations(this.getClass(), getLocation());
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY && !male.isEmpty()) {
            births = rand.nextInt(getMaxLitter()) + 1;
        }
        return births;
    }

}
