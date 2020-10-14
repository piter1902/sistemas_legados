package Models;

import java.util.Date;
import java.util.Objects;

public class SpecificTask implements Task {

    private Date date;
    private String name;
    private String description;

    /**
     * Public default constructor.
     */
    public SpecificTask() {
        this.date = new Date();
        this.name = "";
        this.description = "";
    }

    /**
     * Public constructor.
     * @param date date of the task.
     * @param name name of the task.
     * @param description description of the task.
     */
    public SpecificTask(Date date, String name, String description){
        this.name = name;
        this.date = date;
        this.description = description;
    }

    /**
     * Method that saves a SPECIFIC Task in tareas.c
     * @return true iff task was saved succesfully.
     *   In other case. Return false.
     */
    @Override
    public boolean saveTask() {
        // TODO: Implement this.
        return false;
    }

    /// Getters and Setters

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method that compares 2 objects
     * @param o object to compare with this.
     * @return true iff o == this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecificTask)) return false;
        SpecificTask that = (SpecificTask) o;
        return getDate().equals(that.getDate()) &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    /**
     * Method to obtain a hash
     * @return Hash for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getName(), getDescription());
    }
}
