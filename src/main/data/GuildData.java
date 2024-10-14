package data;

public class GuildData {
    private int guildRoleId;
    private String Username;

    public GuildData(int guildRoleId, String guildName) {
        this.guildRoleId = guildRoleId;
        this.Username = guildName;
    }

    public int getGuildRoleId() {return guildRoleId;}
    public void setGuildRoleId(int guildRoleId) {this.guildRoleId = guildRoleId;}

    public String getUsername() {return Username;}
}
