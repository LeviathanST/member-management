package dto;

public class UserCrewRoleDTO {
    private String account_id;
    private String userName;
    private int crew_role_id;
    private String crew_name;
    private String role_name;

    public UserCrewRoleDTO(String role_name, String crew_name, String userName) {
        this.role_name = role_name;
        this.crew_name = crew_name;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }
}
