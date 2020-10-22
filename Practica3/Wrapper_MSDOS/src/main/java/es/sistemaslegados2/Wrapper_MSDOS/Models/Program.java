package es.sistemaslegados2.Wrapper_MSDOS.Models;

public class Program {
    private String name;
    private String type;
    private String tape;
    private String registry;

    public Program(String name, String type, String tape, String registry) {
        this.name = name;
        this.type = type;
        this.tape = tape;
        this.registry = registry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTape() {
        return tape;
    }

    public void setTape(String tape) {
        this.tape = tape;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "Program{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", tape='" + tape + '\'' +
                ", registry='" + registry + '\'' +
                '}';
    }
}
