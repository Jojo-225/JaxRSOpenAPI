package fr.istic.taa.jaxrs.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User implements Serializable {

    private List<Ticket> tickets = new ArrayList<>();
    
    public Customer(String lastName, String firstName, String mail, String password) {
        super(lastName, firstName, null, mail, password);
    }

    @ManyToMany(mappedBy = "customers")
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
}
