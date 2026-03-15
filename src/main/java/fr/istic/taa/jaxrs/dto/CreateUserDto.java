package fr.istic.taa.jaxrs.dto;
import java.time.LocalDate;

public class CreateUserDto {
    private String lastname;
    private String firstname;
    private LocalDate birthdate;
    private String email;
    private String pass;

    public CreateUserDto() {
    }

    public CreateUserDto(String lastname, String firstname, LocalDate birthdate, String email, String pass) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.email = email;
        this.pass = pass;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

}
