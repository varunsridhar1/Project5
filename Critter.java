package assignment5;

import java.lang.reflect.*;
import java.util.Iterator;
import java.util.List;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Circle;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected final String look(int direction, boolean steps) {
        this.energy -=Params.look_energy_cost;
        int numSteps = 0;
        if(steps)
            numSteps = 2;
        else
            numSteps = 1;
        if(direction == 0){
            return whatsOccupied((this.x_coord + numSteps) %Params.world_width,this.y_coord).toString();
        }
        else if(direction == 1){
            return whatsOccupied((this.x_coord + numSteps)%Params.world_width,(this.y_coord - numSteps) %Params.world_height).toString();
        }
        else if(direction == 2){
            return whatsOccupied(this.x_coord,(this.y_coord - numSteps) % Params.world_height).toString();
        }
        else if(direction == 3){
            return whatsOccupied((this.x_coord - numSteps) % Params.world_width,(this.y_coord - numSteps) % Params.world_height).toString();
        }
        else if(direction == 4){
            return whatsOccupied((this.x_coord - numSteps) % Params.world_width,this.y_coord).toString();
        }
        else if(direction == 5){
            return whatsOccupied((this.x_coord - numSteps) % Params.world_width,(this.y_coord+numSteps) % Params.world_height).toString();
        }
        else if(direction == 6){
            return whatsOccupied(this.x_coord,(this.y_coord+numSteps) % Params.world_height).toString();
        }
        else {
            return whatsOccupied((this.x_coord + numSteps) % Params.world_width,(this.y_coord+numSteps) % Params.world_height).toString();
        }
    }
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private int nextX_coord;
	private int nextY_coord;
	
	private boolean moved = false;
	private boolean inFight = false;
	private static boolean shown = false;
	
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;									// decrement the critter's energy
		if(!moved) {														// if the critter hasn't already moved in this time step
			if(direction == 0) {											// move the critter in the specified direction
				if(!inFight) 
					nextX_coord = (x_coord + 1) % Params.world_width;
				if(inFight && !isOccupied((x_coord + 1) % Params.world_width, y_coord))
					x_coord = (x_coord + 1) % Params.world_width;
			}
			else if(direction == 1) {
				if(!inFight) {
					nextX_coord = (x_coord + 1) % Params.world_width;
					nextY_coord = (y_coord - 1) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord + 1) % Params.world_width, (y_coord - 1) % Params.world_height)) {
					x_coord = (x_coord + 1) % Params.world_width;
					y_coord = (y_coord - 1) % Params.world_height;
				}
			}
			else if(direction == 2) {
				if(!inFight) 
					nextY_coord = (y_coord - 1) % Params.world_height;
				
				if(inFight && !isOccupied(x_coord, (y_coord - 1) % Params.world_height))
					y_coord = (y_coord - 1) % Params.world_height;
			}
			else if(direction == 3) {
				if(!inFight) {
					nextX_coord = (x_coord - 1) % Params.world_width;
					nextY_coord = (y_coord - 1) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord - 1) % Params.world_width, (y_coord - 1) % Params.world_height)) {
					x_coord = (x_coord - 1) % Params.world_width;
					y_coord = (y_coord - 1) % Params.world_height;
				}
			}
			else if(direction == 4) {
				if(!inFight) 
					nextX_coord = (x_coord - 1) % Params.world_width;
				
				if(inFight && !isOccupied((x_coord - 1) % Params.world_width, y_coord)) 
					x_coord = (x_coord - 1) % Params.world_width;
			}
			else if(direction == 5) {
				if(!inFight) {
					nextX_coord = (x_coord - 1) % Params.world_width;
					nextY_coord = (y_coord + 1) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord - 1) % Params.world_width, (y_coord + 1) % Params.world_height)) {
					x_coord = (x_coord - 1) % Params.world_width;
					y_coord = (y_coord + 1) % Params.world_height;
				}
			}	
			else if(direction == 6) {
				if(!inFight) 
					nextY_coord = (y_coord + 1) % Params.world_height;
				
				if(inFight && !isOccupied(x_coord, (y_coord + 1) & Params.world_height))
					y_coord = (y_coord + 1) % Params.world_height;
			}
			else {
				if(!inFight) {
					nextX_coord = (x_coord + 1) % Params.world_width;
					nextY_coord = (y_coord + 1) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord + 1) % Params.world_width, (y_coord + 1) % Params.world_height)) {
					x_coord = (x_coord + 1) % Params.world_width;
					y_coord = (y_coord + 1) % Params.world_height;
				}
			}
		}
		moved = true;														// set moved flag
	}
	
	protected final void run(int direction) {
		energy -= Params.run_energy_cost;									// decrement the critter's energy
		if(!moved) {														// if the critter hasn't already moved in this time step
			if(direction == 0) {											// move the critter in the specified direction
				if(!inFight)
					nextX_coord = (x_coord + 2) % Params.world_width;
				
				if(inFight && !isOccupied((x_coord + 2) % Params.world_width, y_coord))
					x_coord = (x_coord + 2) % Params.world_width;
			}
			else if(direction == 1) {//move the critter in next direction, 7 or so things that look exactly like this
				if(!inFight) {
					nextX_coord = (x_coord + 2) % Params.world_width;
					nextY_coord = (y_coord - 2) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord + 2) % Params.world_width, (y_coord - 2) % Params.world_height)) {
					x_coord = (x_coord + 2) % Params.world_width;//Taking care of fight characteristics
					y_coord = (y_coord - 2) % Params.world_height;
				}
			}
			else if(direction == 2) {
				if(!inFight)
					nextY_coord = (y_coord - 2) % Params.world_height;
				
				if(inFight && !isOccupied(x_coord, (y_coord - 2) % Params.world_height)) 
					y_coord = (y_coord - 2) % Params.world_height;
			}
			else if(direction == 3) {
				if(!inFight) {
					nextX_coord = (x_coord - 2) % Params.world_width;
					nextY_coord = (y_coord - 2) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord - 2) % Params.world_width, (y_coord - 2) % Params.world_height)) {
					x_coord = (x_coord - 2) % Params.world_width;
					y_coord = (y_coord - 2) % Params.world_height;
				}
			}
			else if(direction == 4) {
				if(!inFight)
					nextX_coord = (x_coord - 2) % Params.world_width;
				
				if(inFight && !isOccupied((x_coord - 2) % Params.world_width, y_coord)) 
					x_coord = (x_coord - 2) % Params.world_width;
			}
			else if(direction == 5) {
				if(!inFight) {
					nextX_coord = (x_coord - 2) % Params.world_width;
					nextY_coord = (y_coord + 2) % Params.world_height;
				}
				
				if(inFight && !isOccupied((x_coord - 2) % Params.world_width, (y_coord + 2) % Params.world_height)) {
					x_coord = (x_coord - 2) % Params.world_width;
					y_coord = (y_coord + 2) % Params.world_height;
				}
			}
			else if(direction == 6) {
				if(!inFight)
					nextY_coord = (y_coord + 2) % Params.world_height;
				if(inFight && !isOccupied(x_coord, (y_coord + 2) % Params.world_height))
					y_coord = (y_coord + 2) % Params.world_height;
			}
			else {
				if(!inFight) {
					nextX_coord = (x_coord + 2) % Params.world_width;
					nextY_coord = (y_coord + 2) % Params.world_height;
				}
				if(inFight && !isOccupied((x_coord + 2) % Params.world_width, (y_coord + 2) % Params.world_height)) {
					x_coord = (x_coord + 2) % Params.world_width;
					y_coord = (y_coord + 2) % Params.world_height;
				}
			}
		}
		moved = true;														// set moved flag
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if(getEnergy() >= Params.min_reproduce_energy) {
			offspring.energy = getEnergy() / 2;
			energy = (int)Math.ceil(0.5 * (double)getEnergy());
			if(direction == 0) {
				offspring.x_coord = x_coord + 1;
				offspring.y_coord = y_coord;
			}
			else if(direction == 1) {
				offspring.x_coord = x_coord + 1;
				offspring.y_coord = y_coord - 1;
			}
			else if(direction == 2) {
				offspring.x_coord = x_coord;
				offspring.y_coord = y_coord - 1;
			}
			else if(direction == 3) {
				offspring.x_coord = x_coord - 1;
				offspring.y_coord = y_coord - 1;
			}
			else if(direction == 4) {
				offspring.x_coord = x_coord - 1;
				offspring.y_coord = y_coord;
			}
			else if(direction == 5) {
				offspring.x_coord = x_coord - 1;
				offspring.y_coord = y_coord + 1;
			}
			else if(direction == 6) {
				offspring.x_coord = x_coord;
				offspring.y_coord = y_coord + 1;
			}
			else {
				offspring.x_coord = x_coord + 1;
				offspring.y_coord = y_coord + 1;
			}
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	
	public static void worldTimeStep() {
		for(Critter c: population) 												// invoke doTimeStep on every living critter in the collection
			c.doTimeStep(); //First Instruction. World time step should do every time step
		
		for(Critter c: population) {
			c.x_coord = c.nextX_coord;
			c.y_coord = c.nextY_coord;
		}
			
		//ENCOUNTERS
		for(Critter c: population) {// for all the critters
			for(Critter d: population) {// and anyone they'd fight
				if(c != d && c.x_coord == d.x_coord && c.y_coord == d.y_coord && c.getEnergy() > 0 && d.getEnergy() > 0) {//if in same location
					c.inFight = true;//and if strong enough
					d.inFight = true;//
					boolean cFight = c.fight(d.toString());//convert to word
					boolean dFight = d.fight(c.toString());//convert to word
					if(c.x_coord == d.x_coord && c.y_coord == d.y_coord && c.getEnergy() > 0 && d.getEnergy() > 0) {//check again
						int cRoll = 0;
						int dRoll = 0;
						if(cFight)
							cRoll = getRandomInt(c.getEnergy());//roll to see who wins
						if(dFight)
							dRoll = getRandomInt(d.getEnergy());
						if(cRoll > dRoll) {
							c.energy += (d.getEnergy() / 2);//whoever loses energy goes to 0, others is 1.5 times
							d.energy = 0;
						}
						else {
							d.energy += (c.getEnergy() / 2);
							c.energy = 0;
						}
					}
				}
			}
		}
		
		for(Critter c: population)
			c.energy -= Params.rest_energy_cost;
		
		Iterator<Critter> i = population.iterator();
		while(i.hasNext()) {
			if(i.next().getEnergy() <= 0)
				i.remove();
		}
		
		for(Critter c: population) {
			c.moved = false;
			c.inFight = false;
		}
		
		for(int j = 0; j < Params.refresh_algae_count; j++) {
			Critter algae = new Algae();
			algae.energy = Params.start_energy;
			algae.x_coord = getRandomInt(Params.world_width);
			algae.y_coord = getRandomInt(Params.world_height);
			population.add(algae);
		}
		
		population.addAll(babies);
		babies.clear();
	}
	
	public static void displayWorld(GridPane pane) {
		GridPane world = new GridPane();
		if(!shown) {
			final int numCritterCols = 5;
			final int numCritterRows = 5;
			for(int i = 0; i < numCritterCols; i++) {
				ColumnConstraints colConst = new ColumnConstraints();
				colConst.setPercentWidth(100.0/numCritterCols);
				world.getColumnConstraints().add(colConst);
			}
			for(int i = 0; i < numCritterRows; i++) {
				RowConstraints rowConst = new RowConstraints();
				rowConst.setPercentHeight(100.0/numCritterRows);
				world.getRowConstraints().add(rowConst);
			}
			world.setGridLinesVisible(true);
		
			pane.add(world, 1, 0);
			shown = true;
		}
	} 
	
	/* create and initialize a Critter subclass
	 * critter_class_name must be the name of a concrete subclass of Critter, if not
	 * an InvalidCritterException must be thrown
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> myCritter = null;
		Constructor<?> myConstructor = null;
		Object instanceOfMyCritter = null;
		
		try {
			myCritter = Class.forName(myPackage + "." + critter_class_name);  // Class object of specified name
		} catch(ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		
		try {
			myConstructor = myCritter.getConstructor();					// No parameter constructor object
			instanceOfMyCritter = myConstructor.newInstance();			// Create new object using constructor
		} catch(InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (NoSuchMethodException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (IllegalArgumentException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (InvocationTargetException e) {
			throw new InvalidCritterException(critter_class_name);
		} 
			
		
		Critter newCritter = (Critter)instanceOfMyCritter;
		newCritter.energy = Params.start_energy;						// start energy of new Critter
		newCritter.x_coord = getRandomInt(Params.world_width);			// set x coordinate of the new Critter
		newCritter.y_coord = getRandomInt(Params.world_height);			// set y coordinate of the new Critter
		
		population.add(newCritter);										// add new Critter to the list of Critters
	}
	
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();		// list to store critters of the given type
		Class<?> myCritter = null;										// class variable
		
		try {
			myCritter = Class.forName(myPackage + "." + critter_class_name);				// assigns specific class to variable
		}
		catch(ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);		// throws exception if class does not exist
		}
		
		for(Critter crit: population) {									// iterate through all critters
			if(myCritter.isInstance(crit))								// if that critter is an instance of the given type
				result.add(crit);										// add it to the list
		}
	
		return result;													// return the list of critters
	}
	
	public static String runStats(List<Critter> critters) {
		String result = "";
		result += critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			result += prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		return result;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
	}
	
	private boolean isOccupied(int x, int y) {
		for(Critter c: population) {
			if(c.x_coord == x && c.y_coord == y)//used to check if occupied
				return true;
		}
		return false;
	}
	
	private Critter whatsOccupied(int x, int y) {
        for(Critter c: population) {
            if(c.x_coord == x && c.y_coord == y)//used to check if occupied
                return c;
        }
        return null;
    }
	
	
}
