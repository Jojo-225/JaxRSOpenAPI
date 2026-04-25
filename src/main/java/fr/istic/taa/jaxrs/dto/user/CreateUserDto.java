package fr.istic.taa.jaxrs.dto.user;
import java.time.LocalDate;

public class CreateUserDto {
    private String lastname;
    private String firstname;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String role;

    public CreateUserDto() {
    }

    public CreateUserDto(String lastname, String firstname, LocalDate dateOfBirth, String email, String password, String role) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.role = role;
    
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }       

    public void setRole(String role) {
        this.role = role;
    }

}
