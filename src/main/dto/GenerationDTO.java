package dto;

public class GenerationDTO {
    private int id;
    private String name;

    public GenerationDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public GenerationDTO(String name) {
        this.name = name;
    }
    public int getId() {return id;}
    public String getName() {return name;}
}
