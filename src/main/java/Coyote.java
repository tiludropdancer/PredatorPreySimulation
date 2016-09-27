import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Coyotes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael K��lling
 * @version 2011.07.31
 */
public class Coyote extends Animal
{
    // Characteristics shared by all foxes (class variables).

    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.45;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 8;
    private static final int FOX_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The coyote's food level, which is increased by eating rabbits ad foxes.
    private int foodLevel;

    /**
     * Create a coyote. A coyote can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(int max_age, int breed_age, int max_litter, boolean randomAge, Field field, Location location)
    {
        super(max_age, breed_age, max_litter, randomAge, field, location);
        if(randomAge) {
            foodLevel = rand.nextInt(FOX_FOOD_VALUE);
        }
        else {
            foodLevel = FOX_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newCoyotes A list to return newly born foxes.
     */
    public void act(List<Actor> newCoyotes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newCoyotes);            
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
            if (animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    eat(FOX_FOOD_VALUE);
                    // Remove the dead fox from the field.
                    return where;
                }
            }
            else if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    eat(RABBIT_FOOD_VALUE);
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
        }
        return null;
    }
    
    private void eat(int foodValue)
    {
    	foodLevel += foodValue;
    	if(foodLevel > FOX_FOOD_VALUE)
    		foodLevel = FOX_FOOD_VALUE;
    }
    
    /**
     * Check whether or not this coyote is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newCoyotes)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Coyote young = new Coyote(getMaxAge(), getBreedAge(), getMaxLitter(), false, field, loc);
            newCoyotes.add(young);
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
