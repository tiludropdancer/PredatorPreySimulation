import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael K��lling
 * @version 2011.07.31
 */
public class Hunter extends Actor
{
	private static final double HUNTING_PROBABILITY = 0.45;
    private static final Random rand = Randomizer.getRandom();
    private int hunt_age;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hunter(int max_age, int hunt_age, boolean randomAge, Field field, Location location)
    {
        super(max_age, randomAge, field, location);
        this.hunt_age = hunt_age;
    }
    
    public void act(List<Actor> newHunters)
    {
        incrementAge();
        if(isAlive()) {           
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
        }
    }
    
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Coyote) {
                Coyote coyote = (Coyote) animal;
                if(coyote.isAlive() && rand.nextDouble() <= HUNTING_PROBABILITY) { 
                	coyote.setDead();
                    // Remove the dead coyote from the field.
                    return where;
                }
            }
            else if (animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive() && rand.nextDouble() <= HUNTING_PROBABILITY) { 
                    fox.setDead();
                    // Remove the dead fox from the field.
                    return where;
                }
            }
            else if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive() && rand.nextDouble() <= HUNTING_PROBABILITY) { 
                    rabbit.setDead();
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
        }
        return null;
    }

    protected int getHuntAge()
    {
        return hunt_age;
    }
    
    protected void setHuntAge(int a)
    {
        this.hunt_age = a;
    }
}
