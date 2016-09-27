package simulation;
import java.util.List;
import java.util.Random;

public abstract class Actor {
	// Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    private int max_age;
    private int age;
    private static final Random rand = Randomizer.getRandom();

    public Actor(int max_age, boolean randomAge, Field field, Location location)
    {
    	alive = true;
    	this.max_age = max_age;
        this.field = field;
        setLocation(location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(getMaxAge());
        }
    }
    
    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born actors.
     */
    abstract public void act(List<Actor> newActors);
    
    protected int getMaxAge()
    {
        return max_age;
    }
    
    protected void setMaxAge(int a)
    {
        this.max_age = a;
    }
    
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Increase the age. This could result in the fox's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
}
