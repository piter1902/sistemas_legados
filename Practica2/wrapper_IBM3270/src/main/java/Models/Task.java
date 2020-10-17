package Models;

/**
 * Interface that represents an abstract Task.
 */
public interface Task {

    /**
     * Method that saves a Task.
     * @return true iff task was saved succesfully.
     *  In other case. Return false.
     */
    boolean saveTask();
}
