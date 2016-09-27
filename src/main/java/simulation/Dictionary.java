package simulation;

public class Dictionary {
	private int MAX_AGE;
    private int MAX_LITTER_SIZE;
    private int BREEDING_AGE;
    private int HUNT_AGE;
    
    protected int getHuntAge()
    {
    	return HUNT_AGE;
    }
    
    protected void setHuntAge(int u)
    {
    	HUNT_AGE = u;
    }
    
    protected int getBreedAge()
    {
    	return BREEDING_AGE;
    }
    
    protected void setBreedAge(int u)
    {
    	BREEDING_AGE = u;
    }
    
    protected int getMaxAge()
    {
    	return MAX_AGE;
    }
    
    protected void setMaxAge(int y)
    {
    	MAX_AGE = y;
    }
    
    protected int getMaxLitter()
    {
    	return MAX_LITTER_SIZE;
    }
    
    protected void setMaxLitter(int z)
    {
    	MAX_LITTER_SIZE = z;
    }
}
