package fr.istic.taa.jaxrs.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
    private Long id;
    private String title;
    private int capacity;
    private String statut;
    private Concert concert;
    private List<Customer> customers = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(String title, int capacity, String statut, Concert concert) {
        this.title = title;
        this.capacity = capacity;
        this.statut = statut;
        this.concert = concert;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @ManyToOne
    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    @ManyToMany
    public List<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        this.customers.remove(customer);
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}
