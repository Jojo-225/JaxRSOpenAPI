package fr.istic.taa.jaxrs.dto.user;
import java.time.LocalDate;

public class UpdateUserDto {

    private String lastname;
    private String firstname;
    // private String email;
    private LocalDate birthdate;
    private String password;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String lastname, String firstname, String email, LocalDate birthdate, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.password = password;
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
        return password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPass(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
