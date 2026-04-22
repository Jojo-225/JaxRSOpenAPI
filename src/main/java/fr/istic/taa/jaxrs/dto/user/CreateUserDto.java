package fr.istic.taa.jaxrs.dto.user;
import java.time.LocalDate;

public class CreateUserDto {
    private String lastname;
    private String firstname;
    private LocalDate birthdate;
    private String email;
    private String password;

    public CreateUserDto() {
    }

    public CreateUserDto(String lastname, String firstname, LocalDate birthdate, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
