package dto;

public class GuildRoleDTO {
    private String role;
    private int guildId;
    private int id;
    private String guildName;


    public GuildRoleDTO(String role,String guildName) {
        this.role = role;
        this.guildName = guildName;
    }

    public String getGuildName() {return guildName;}

    public String getRole() {return role;}

    public int getGuildId() {return guildId;}
    public void setGuildId(int guildId) {this.guildId = guildId;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}
