package fr.istic.taa.jaxrs.dto.ticket;

import java.util.List;  
import java.util.ArrayList;  

public class UpdateTicketDto{

    private String title;
    private Integer capacity;
    private String statut;

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

}
