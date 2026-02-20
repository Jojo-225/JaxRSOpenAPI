package fr.istic.taa.jaxrs.domain;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User implements Serializable {

    public Admin() {
        super();
    }

    public Admin(String lastName, String firstName, LocalDate dateOfBirth, String mail, String password) {
        super(lastName, firstName, dateOfBirth, mail, password);
    }
}
