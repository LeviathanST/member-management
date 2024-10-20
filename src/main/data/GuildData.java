package data;

public class GuildData {
    private String userName;
    private String guildRole;
    private String guildName;

    public GuildData(String userName, String guildRole, String guildName) {
        this.userName = userName;
        this.guildRole = guildRole;
        this.guildName = guildName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getGuildRole() {
        return this.guildRole;
    }

    public String getGuildName() {
        return this.guildName;
    }
}
