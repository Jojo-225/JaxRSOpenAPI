package fr.istic.taa.jaxrs.dto.response;

public class OrganizerDashboardStatsDto {
    private long totalConcerts;
    private long upcomingConcerts;
    private long soldOutConcerts;
    private long ticketsSold;
    private long uniqueCustomers;

    public OrganizerDashboardStatsDto() {
    }

    public OrganizerDashboardStatsDto(long totalConcerts, long upcomingConcerts, long soldOutConcerts,
                                      long ticketsSold, long uniqueCustomers) {
        this.totalConcerts = totalConcerts;
        this.upcomingConcerts = upcomingConcerts;
        this.soldOutConcerts = soldOutConcerts;
        this.ticketsSold = ticketsSold;
        this.uniqueCustomers = uniqueCustomers;
    }

    public long getTotalConcerts() {
        return totalConcerts;
    }

    public void setTotalConcerts(long totalConcerts) {
        this.totalConcerts = totalConcerts;
    }

    public long getUpcomingConcerts() {
        return upcomingConcerts;
    }

    public void setUpcomingConcerts(long upcomingConcerts) {
        this.upcomingConcerts = upcomingConcerts;
    }

    public long getSoldOutConcerts() {
        return soldOutConcerts;
    }

    public void setSoldOutConcerts(long soldOutConcerts) {
        this.soldOutConcerts = soldOutConcerts;
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
}

