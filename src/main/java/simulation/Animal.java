import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael K��lling
 * @version 2011.07.31
 */
public abstract class Animal extends Actor
{
    private int breed_age;
    private int max_litter;
    private boolean gender;
    private static final Random rand = Randomizer.getRandom();
    
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(int max_age, int breed_age, int max_litter, boolean randomAge, Field field, Location location)
    {
        super(max_age, randomAge, field, location);
        this.breed_age = breed_age;
        this.max_litter = max_litter;
        gender = rand.nextBoolean();
    }
    
    protected int getBreedAge()
    {
        return breed_age;
    }
    
    protected void setBreedAge(int a)
    {
        this.breed_age = a;
    }
    
    protected int getMaxLitter()
    {
        return max_litter;
    }
    
    protected void setMaxLitter(int a)
    {
        this.max_litter = a;
    }
    
    protected boolean isFemale()
    {
    	return gender;
    }

    /**
     * An animal can breed if it has reached the breeding age and is a female.
     * @return true if the rabbit can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return getAge() >= getBreedAge() && isFemale();
    }
}
