package fr.istic.taa.jaxrs.dto.ticket;

public class BuyTicketDto {
    private Long ticketId;

    public BuyTicketDto() {
    }

    public BuyTicketDto(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}

