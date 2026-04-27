package fr.istic.taa.jaxrs.dto.response;

import java.time.LocalDateTime;

public class TicketSaleHistoryItemDto {
    private Long saleId;
    private LocalDateTime purchaseDate;
    private double price;

    private Long customerId;
    private String customerName;
    private String customerEmail;

    private Long concertId;
    private String concertTopic;

    private Long ticketId;
    private String ticketTitle;

    public TicketSaleHistoryItemDto() {
    }

    public TicketSaleHistoryItemDto(Long saleId, LocalDateTime purchaseDate, double price,
                                    Long customerId, String customerName, String customerEmail,
                                    Long concertId, String concertTopic,
                                    Long ticketId, String ticketTitle) {
        this.saleId = saleId;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.concertId = concertId;
        this.concertTopic = concertTopic;
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public String getConcertTopic() {
        return concertTopic;
    }

    public void setConcertTopic(String concertTopic) {
        this.concertTopic = concertTopic;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }
}

