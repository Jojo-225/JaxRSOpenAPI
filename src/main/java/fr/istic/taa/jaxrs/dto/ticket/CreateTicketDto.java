package fr.istic.taa.jaxrs.dto.ticket;



public class CreateTicketDto {
    private String title;
    private int capacity;
    private String statut;
    private Long concertId;


    public CreateTicketDto() {
    }
    
    public CreateTicketDto(String title, int capacity, String statut, Long concertId) {
        this.title = title;
        this.capacity = capacity;
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
