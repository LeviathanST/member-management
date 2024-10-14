package data;

public class CrewData {
    private String userName;
    private int crew_role_id;

    public CrewData(String userName, int crew_role_id){
        this.userName = userName;
        this.crew_role_id = crew_role_id;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getCrewRoleId() {
        return this.crew_role_id;
    }

    public void setCrewRoleId(int crew_role_id) {
        this.crew_role_id = crew_role_id;
    }
 }
