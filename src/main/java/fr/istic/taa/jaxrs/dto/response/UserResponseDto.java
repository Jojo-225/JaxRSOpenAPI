package fr.istic.taa.jaxrs.dto.response;

import java.time.LocalDate;

public class UserResponseDto {
    private Long id;
    private String lastName;
    private String firstName;
    private LocalDate birthdate;
    private String email;
    private String role;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String lastName, String firstName, LocalDate birthdate, String email, String role) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthdate = birthdate;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
