package data;

public class GuildData {
    private String guildName;
    private String guildRoleName;
    private String Username;

    public GuildData(String guildName, String username, String guildRoleName) {
        this.guildName = guildName;
        this.Username = username;
        this.guildRoleName = guildRoleName;
    }

    public String getGuildRoleName() {return guildRoleName;}
    public void setGuildRoleName(String guildRoleName) {this.guildRoleName = guildRoleName;}

    public String getGuildName() {return guildName;}
    public void setGuildName(String guildName) {this.guildName = guildName;}

    public String getUsername() {return Username;}
}
