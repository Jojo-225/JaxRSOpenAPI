package fr.istic.taa.jaxrs.domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("ORGANIZER")
public class Organizer extends User implements Serializable {
    private List<Concert> concertsOrganised = new ArrayList<>();

    public Organizer() {
    }

    public Organizer(String lastName, String firstName, LocalDate dateOfBirth, String mail, String password) {
        super(lastName, firstName, dateOfBirth, mail, password);
    }

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.PERSIST)
    public List<Concert> getconcerts() {
        return concertsOrganised;
    }

    public void addConcert(Concert concert) {
        this.concertsOrganised.add(concert);
    }

    public void removeConcert(Concert concert) {
        this.concertsOrganised.remove(concert);
    }

    public void setconcerts(List<Concert> concertsOrganised) {
        this.concertsOrganised = concertsOrganised;
    }
}