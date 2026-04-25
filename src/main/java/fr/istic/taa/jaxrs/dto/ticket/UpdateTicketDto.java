package fr.istic.taa.jaxrs.dto.ticket;

import java.util.List;  
import java.util.ArrayList;  

public class UpdateTicketDto{

    private String title;
    private Integer capacity;
    private String statut;
    private List<Long> customerIds = new ArrayList<>();

    public UpdateTicketDto() {
    }
    
    public UpdateTicketDto(String title, Integer capacity, String statut) {
        this.title = title;
        this.capacity = capacity;
        this.statut = statut;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void addCustomerId(Long customerId) {
        this.customerIds.add(customerId);
    }

    public void removeCustomerId(Long customerId) {
        this.customerIds.remove(customerId);
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }


}
