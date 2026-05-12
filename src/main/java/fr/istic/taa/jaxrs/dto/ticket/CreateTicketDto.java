package fr.istic.taa.jaxrs.dto.ticket;



public class CreateTicketDto {
    private String title;
    private int capacity;
    private double price;
    private String statut;
    private Long concertId;


    public CreateTicketDto() {
    }
    
    public CreateTicketDto(String title, int capacity, double price, String statut, Long concertId) {
        this.title = title;
        this.capacity = capacity;
        this.price = price;
        this.statut = statut;
        this.concertId = concertId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }
}
