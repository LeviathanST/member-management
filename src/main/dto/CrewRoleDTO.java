package dto;

public class CrewRoleDTO {
    private int id;
    private String name;
    private int guildId;

    public CrewRoleDTO(int id, String name, int guildId) {
        this.id = id;
        this.name = name;
        this.guildId = guildId;
    }
    public CrewRoleDTO(String name, int guildId) {
        this.name = name;
        this.guildId = guildId;
    }
    public int getId() {return id;}
    public String getName() {return name;}
    public int getGuildId() {return guildId;}
}
