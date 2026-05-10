package fr.istic.taa.jaxrs.domain;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private List<Ticket> tickets = new ArrayList<>();
    private Boolean notifyAllOrganizers = true;
    private List<Organizer> followedOrganizers = new ArrayList<>();

    public Customer() {
        super();
    }
    
    public Customer(String lastName, String firstName, LocalDate dateOfBirth, String mail, String password) {
        super(lastName, firstName, dateOfBirth, mail, password);
    }

    @ManyToMany(mappedBy = "customers", cascade = jakarta.persistence.CascadeType.PERSIST)
    @JsonManagedReference
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicketAchete(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicketAchete(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Transient
    public boolean isNotifyAllOrganizers() {
        return notifyAllOrganizers == null || notifyAllOrganizers;
    }

    public void setNotifyAllOrganizers(boolean notifyAllOrganizers) {
        this.notifyAllOrganizers = notifyAllOrganizers;
    }

    @Column
    public Boolean getNotifyAllOrganizers() {
        return notifyAllOrganizers;
    }

    public void setNotifyAllOrganizers(Boolean notifyAllOrganizers) {
        this.notifyAllOrganizers = notifyAllOrganizers;
    }

    @ManyToMany
    @JoinTable(
            name = "customer_followed_organizers",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    public List<Organizer> getFollowedOrganizers() {
        return followedOrganizers;
    }

    public void setFollowedOrganizers(List<Organizer> followedOrganizers) {
        this.followedOrganizers = followedOrganizers;
    }
}
