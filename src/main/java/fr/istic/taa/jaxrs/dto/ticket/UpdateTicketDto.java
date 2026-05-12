package fr.istic.taa.jaxrs.dto.ticket;

public class UpdateTicketDto{

    private String title;
    private Integer capacity;
    private Double price;
    private String statut;

    public UpdateTicketDto() {
    }
    
    public UpdateTicketDto(String title, Integer capacity, Double price, String statut) {
        this.title = title;
        this.capacity = capacity;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}
