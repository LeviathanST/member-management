package dto;

public class CrewDTO {
    private int id;
    private String name;

    public CrewDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CrewDTO(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
