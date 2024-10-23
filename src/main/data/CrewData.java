package data;

public class CrewData {
    private String account_id;
    private int crew_role_id;
    private String crew_name;
    private String role_name;

    public CrewData(String account_id, String role_name, String crew_name) {
        this.account_id = account_id;
        this.role_name = role_name;
        this.crew_name = crew_name;
    }

    public String getAccount_id() {
        return this.account_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public int getCrewRoleId() {
        return this.crew_role_id;
    }

    public void setCrewRoleId(int crew_role_id) {
        this.crew_role_id = crew_role_id;
    }

    public String getCrew_name() {
        return this.crew_name;
    }
}
