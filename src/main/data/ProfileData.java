package data;

// import com.mysql.cj.result.LocalDateTimeValueFactory;
import java.sql.Date;
import constants.Sex;

public class ProfileData {
    private String id;
    private String account_id;
    private String full_name;
    private Sex sex;
    private String student_code;
    private String contact_email;
    private String generation;
    private Date date_of_birth;

    public ProfileData() {}

    public ProfileData(String id, String account_id, String full_name, Sex sex,
                        String student_code, String contact_email, String generation, Date date_of_birth) {
        this.id = id;
        this.account_id = account_id;
        this.full_name = full_name;
        this.sex = sex;
        this.student_code = student_code;
        this.contact_email = contact_email;
        this.generation = generation;
        this.date_of_birth = date_of_birth;
    }

    public String getId() { 
        return this.id; 
    }

    public void setId(String id) {
         this.id = id;
    }

    public String getAccountId() { 
        return this.account_id; 
    }

    public void setAccountId(String account_id) { 
        this.account_id = account_id; 
    }

    public String getFullName() { 
        return this.full_name;
    }

    public void setFullName(String full_name) { 
        this.full_name = full_name;
    }

    public Sex getSex() {
         return this.sex;
    }

    public void setSex(Sex sex) { 
        this.sex = sex; 
    }

    public String getStudentCode() { 
        return this.student_code;
    }

    public void setStudentCode(String student_code) {
         this.student_code = student_code;
    }

    public String getContactEmail() { 
        return this.contact_email; 
    }

    public void setContactEmail(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getGeneration() {
        return this.generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public Date getDateOfBirth() {
        return this.date_of_birth;
    }

    public void setDateOfBirth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
