package fr.istic.taa.jaxrs.dto.ticket;

import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;

import java.util.List;  
import java.util.ArrayList;  

public class UpdateTicketDto{

        private String title;
    private int capacity;
    private String statut;
    private Concert concert;
    private List<Customer> customers = new ArrayList<>();

    public UpdateTicketDto() {
    }
    
    public UpdateTicketDto(String title, int capacity, String statut, Concert concert) {
        this.title = title;
        this.capacity = capacity;
        this.statut = statut;
        this.concert = concert;
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

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

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