package dto;

public class GuildDTO {
    private int id;
    private String name;

    public GuildDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GuildDTO(String name) {
        this.name = name;
    }

    public int getId() {return id;}

    public String getName() {return name;}
}
