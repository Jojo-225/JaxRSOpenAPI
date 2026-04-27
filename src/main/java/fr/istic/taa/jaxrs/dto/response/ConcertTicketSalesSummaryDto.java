package fr.istic.taa.jaxrs.dto.response;

import java.time.LocalDateTime;

public class ConcertTicketSalesSummaryDto {
    private Long concertId;
    private String topic;
    private LocalDateTime date;
    private long ticketsSold;
    private long uniqueCustomers;
    private double revenue;

    public ConcertTicketSalesSummaryDto() {
    }

    public ConcertTicketSalesSummaryDto(Long concertId, String topic, LocalDateTime date,
                                        long ticketsSold, long uniqueCustomers, double revenue) {
        this.concertId = concertId;
        this.topic = topic;
        this.date = date;
        this.ticketsSold = ticketsSold;
        this.uniqueCustomers = uniqueCustomers;
        this.revenue = revenue;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public long getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(long ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public long getUniqueCustomers() {
        return uniqueCustomers;
    }

    public void setUniqueCustomers(long uniqueCustomers) {
        this.uniqueCustomers = uniqueCustomers;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}

