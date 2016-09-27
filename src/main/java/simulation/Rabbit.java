package simulation;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael K��lling
 * @version 2011.07.31
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.48;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(int max_age, int breed_age, int max_litter, boolean randomAge, Field field, Location location)
    {
        super(max_age, breed_age, max_litter, randomAge, field, location);
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Actor> newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
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
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(getMaxAge(), getBreedAge(), getMaxLitter(), false, field, loc);
            newRabbits.add(young);
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
