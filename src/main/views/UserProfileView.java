package views;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constants.ResponseStatus;
import constants.Sex;
import controllers.ApplicationController;
import java.lang.Object;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;


public class UserProfileView extends View{
    public UserProfileView(Connection con) {
        super(con);
    }

    public void addUserProfile(Connection con) {
        String fullName, studentCode, email, contactEmail;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        do {
            viewTitle("| INSERT YOUR PROFILE |", textIO);
            UserProfileDTO user_profile = new UserProfileDTO();
            do {
                fullName = textIO.newStringInputReader().read("Enter your full name : ");
            } while (isValidFullName(fullName) == false);
            user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
            do {
                studentCode = textIO.newStringInputReader().read("Enter your roll number : ");
            } while (isValidStudentCode(studentCode) == false);
            do {
                email = textIO.newStringInputReader().read("Enter your email : ");
            } while (isValidEmail(email) == false);
            do {
                contactEmail = textIO.newStringInputReader().withMinLength(0).read("Enter your contact email (enter to skip): ");
            } while (checkContactEmail(contactEmail) == false);

            user_profile.setFullName(fullName);
            user_profile.setStudentCode(studentCode);
            user_profile.setEmail(email);
            user_profile.setContactEmail(contactEmail);
            user_profile.setDateOfBirth(setDob());

            response = ApplicationController.createOneUserProfile(user_profile);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } else textIO.getTextTerminal().println(response.getMessage());
        } while (response.getStatus() != ResponseStatus.OK);
        clearScreen();
    }


    public void updateProfile() {
        String fullName, studentCode, email, contactEmail;
        ResponseDTO<Object> response = new ResponseDTO<Object>(null, null, null);
        do {
            viewTitle("| UPDATE YOUR PROFILE |", textIO);
            UserProfileDTO user_profile = new UserProfileDTO();
            do {
                fullName = textIO.newStringInputReader().read("Enter your full name : ");
            } while (isValidFullName(fullName) == false);
            user_profile.setSex(textIO.newEnumInputReader(Sex.class).read("Enter your sex : "));
            do {
                studentCode = textIO.newStringInputReader().read("Enter your roll number : ");
            } while (isValidStudentCode(studentCode) == false);
            do {
                email = textIO.newStringInputReader().read("Enter your email : ");
            } while (isValidEmail(email) == false);
            do {
                contactEmail = textIO.newStringInputReader().read("Enter your contact email : ");
            } while (checkContactEmail(contactEmail) == false);

            user_profile.setFullName(fullName);
            user_profile.setStudentCode(studentCode);
            user_profile.setEmail(email);
            user_profile.setContactEmail(contactEmail);
            user_profile.setDateOfBirth(setDob());

            response = ApplicationController.updateUserProfile(user_profile);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            } else textIO.getTextTerminal().println(response.getMessage());
        } while (response.getStatus() != ResponseStatus.OK);
        clearScreen();
    }

    public void showProfile() {
        UserProfileDTO profile = new UserProfileDTO();
        ResponseDTO<UserProfileDTO> response = new ResponseDTO<UserProfileDTO>(null, null, null);
        response = ApplicationController.readOneUserProfile(profile);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
        textIO.getTextTerminal().println("Account ID: " + profile.getAccountId());
        textIO.getTextTerminal().println("Full Name: " + profile.getFullName());
        textIO.getTextTerminal().println("Sex: " + profile.getSex());
        textIO.getTextTerminal().println("Student Code: " + profile.getStudentCode());
        textIO.getTextTerminal().println("Email: " + profile.getEmail());
        textIO.getTextTerminal().println("Contact Email: " + profile.getContactEmail());
        textIO.getTextTerminal().println("Generation : " + profile.getGenerationName());
        textIO.getTextTerminal().println("Date of Birth: " + profile.getDateOfBirth());
    }


    public boolean checkContactEmail(String contactEmail) {
        if(contactEmail.length() == 0) 
            return true;
        else if(isValidEmail(contactEmail) == false)
            return false;
        return true;
    }

    public java.sql.Date setDob() {
        boolean check = false;
        java.sql.Date sqlDate = new Date(0);
        do {          
            String date = textIO.newStringInputReader().read("Enter your birthday (dd-MM-yyy) : ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat.setLenient(false);
            try {
                long millis = dateFormat.parse(date).getTime();
                sqlDate = new java.sql.Date(millis);
                check = true;
            } catch (ParseException e) {
                printError("Invalid date.");
            }
        } while (check == false);
        return sqlDate;
    }

    public boolean isValidStudentCode(String student_code) {
        String regex = "[Ss][ASEase]\\d{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(student_code);
        if(matcher.matches() == false) {
            printError("Invalid student code.");
            return false;
        } else return true;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        Pattern pattern = Pattern.compile(emailRegex);
        
        if (email == null) {
            printError("Email can not be null.");
            return false;
        }
        
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches() == false) {
            printError("Invalid email.");
            return false;
        } else return true;
    }

    public boolean isValidFullName(String fullName) {

        String[] words = fullName.trim().split("\\s+");
        
        // Full name must not contain special character
        for (String word : words) {
            if (!word.matches("[a-zA-Z]+")) {
                printError("Full name must not contains special characters.");
                return false;
            }
        }
        
        // Full name has at least 2 
        if (words.length < 2) {
            printError("Full name must has contains at least 2 words.");
            return false;
        }
        return true;
    }

   
}
