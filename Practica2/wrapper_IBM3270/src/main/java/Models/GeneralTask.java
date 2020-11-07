package Models;

import java.util.Date;
import java.util.Objects;


public class GeneralTask implements Task {

    private String date;
    private String description;

    /**
     * Public default constructor.
     */
    public GeneralTask() {
        this.date = new Date().toString();
        this.description = "";
    }

    /**
     * Public constructor.
     * @param date date of the task (DDMM).
     * @param description description of the task.
     */
    public GeneralTask(String date, String description) {
        this.date = date;
        this.description = description;
    }

    /**
     * Method that saves a GENERAL Task in tareas.c
     * @return true iff task is correctly saved.
     */
    @Override
    public boolean saveTask() {
        // TODO: Implement this.
        return true;
    }

    /// Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Method that compares 2 objects
     * @param o object to compare with this.
     * @return true iff o == this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneralTask)) return false;
        GeneralTask that = (GeneralTask) o;
        return getDate().equals(that.getDate()) &&
                description.equals(that.description);
    }

    /**
     * Method to obtain a hash
     * @return Hash for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDate(), description);
    }

    @Override
    public String toString() {
        return "GeneralTask{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
