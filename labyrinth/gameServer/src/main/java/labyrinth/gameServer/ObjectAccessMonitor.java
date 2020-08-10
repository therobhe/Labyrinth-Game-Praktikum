package labyrinth.gameServer;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class restricting the access to the contained object to one thread at a time
 * @param <ObjectT> Type of the object
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class ObjectAccessMonitor<ObjectT>
{
	//Properties
	
	private ObjectT object;
	
	//Constructors
	
	/**
	 * ObjectAccessMonitor constructor
	 * @param object The object
	 */
	public ObjectAccessMonitor(ObjectT object) { this.object = object; }
	
	//Methods
	
	/**
	 * Performs an action on the contained object
	 * @param action The action to perform on the object
	 */
	public synchronized void executeAction(Consumer<ObjectT> action) { action.accept(object); }
	
	/**
	 * Executes a function on the contained object and returns the result of the function.
	 * @param function The function that should be executed
	 * @return The result of the passed function
	 */
	public synchronized <ResultT> ResultT executeFunction(Function<ObjectT, ResultT> function)
	{ return function.apply(object); }
}
