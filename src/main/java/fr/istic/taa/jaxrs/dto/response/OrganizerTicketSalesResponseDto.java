package fr.istic.taa.jaxrs.dto.response;

import java.util.ArrayList;
import java.util.List;

public class OrganizerTicketSalesResponseDto {
    private Long organizerId;
    private long totalConcerts;
    private long totalTicketsSold;
    private double totalRevenue;
    private double averageTicketPrice;
    private List<ConcertTicketSalesSummaryDto> concerts = new ArrayList<>();

    public OrganizerTicketSalesResponseDto() {
    }

    public OrganizerTicketSalesResponseDto(Long organizerId, long totalConcerts, long totalTicketsSold,
                                           double totalRevenue, double averageTicketPrice,
                                           List<ConcertTicketSalesSummaryDto> concerts) {
        this.organizerId = organizerId;
        this.totalConcerts = totalConcerts;
        this.totalTicketsSold = totalTicketsSold;
        this.totalRevenue = totalRevenue;
        this.averageTicketPrice = averageTicketPrice;
        if (concerts != null) {
            this.concerts = concerts;
        }
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public long getTotalConcerts() {
        return totalConcerts;
    }

    public void setTotalConcerts(long totalConcerts) {
        this.totalConcerts = totalConcerts;
    }

    public long getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(long totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getAverageTicketPrice() {
        return averageTicketPrice;
    }

    public void setAverageTicketPrice(double averageTicketPrice) {
        this.averageTicketPrice = averageTicketPrice;
    }

    public List<ConcertTicketSalesSummaryDto> getConcerts() {
        return concerts;
    }

    public void setConcerts(List<ConcertTicketSalesSummaryDto> concerts) {
        this.concerts = concerts;
    }
}

