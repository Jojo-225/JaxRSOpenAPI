package fr.istic.taa.jaxrs.dto;
import java.time.LocalDate;

public class UpdateUserDto {

    private String lastname;
    private String firstname;
    // private String email;
    private LocalDate birthdate;
    private String pass;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String lastname, String firstname, String email, LocalDate birthdate, String pass) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.pass = pass;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPass() {
        return pass;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
