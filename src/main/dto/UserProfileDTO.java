package dto;

import java.sql.Date;
import constants.Sex;

public class UserProfileDTO{
    private String account_id;
    private String full_name;
    private Sex sex;
    private String student_code;
    private String contact_email;
    private int generation_id;
    private Date date_of_birth;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              


    public UserProfileDTO() {}

    public UserProfileDTO(String account_id, String full_name, Sex sex,
                        String student_code, String contact_email, int generation_id, Date date_of_birth) {

        this.account_id = account_id;
        this.full_name = full_name;
        this.sex = sex;
        this.student_code = student_code;
        this.contact_email = contact_email;
        this.generation_id = generation_id;
        this.date_of_birth = date_of_birth;
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

    public int getGenerationId() {
        return this.generation_id;
    }

    public void setGenerationId(int generation_id) {
        this.generation_id = generation_id;
    }

    public Date getDateOfBirth() {
        return this.date_of_birth;
    }

    public void setDateOfBirth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

}


