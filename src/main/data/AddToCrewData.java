package data;

public class AddToCrewData {
    private String userName;
    private int crew_role_id;

    public AddToCrewData(String userName, int crew_role_id){
        this.userName = userName;
        this.crew_role_id = crew_role_id;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getCrew_role_id() {
        return this.crew_role_id;
    }

    public void setCrew_role_id(int crew_role_id) {
        this.crew_role_id = crew_role_id;
    }
 }
