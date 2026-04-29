package fr.istic.taa.jaxrs.dto.ticket;

public class BuyTicketDto {
    private Long ticketId;
    private Integer quantity;

    public BuyTicketDto() {
    }

    public BuyTicketDto(Long ticketId) {
        this.ticketId = ticketId;
        this.quantity = 1;
    }

    public BuyTicketDto(Long ticketId, Integer quantity) {
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}