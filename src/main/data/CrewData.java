package data;

public class CrewData {
    private String account_id;
    private int crew_role_id;

    public CrewData(String account_id, int crew_role_id) {
        this.account_id = account_id;
        this.crew_role_id = crew_role_id;
    }

    public String getAccount_id() {
        return this.account_id;
    }

    public int getCrewRoleId() {
        return this.crew_role_id;
    }

    public void setCrewRoleId(int crew_role_id) {
        this.crew_role_id = crew_role_id;
    }
}
