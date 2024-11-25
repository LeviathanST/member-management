package dto;

public class CrewRoleDTO {
    private String role;
    private int crewId;
    private int id;
    private String crewName;

    public CrewRoleDTO(String role, String crewName) {
        this.role = role;
        this.crewName = crewName;
    }
    public String getRole() {return role;}

    public int getCrewId() {return crewId;}
    public void setCrewId(int crewId) {this.crewId = crewId;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getCrewName() {return crewName;}
}
