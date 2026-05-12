package fr.istic.taa.jaxrs.dto.response;

public class OrganizerDashboardStatsDto {
    private long totalConcerts;
    private long upcomingConcerts;
    private long soldOutConcerts;
    private long ticketsSold;
    private long uniqueCustomers;
    private double ticketRevenue;
    private double averageBasket;
    private long ticketsRemaining;
    private double sellThroughRate;

    public OrganizerDashboardStatsDto() {
    }

    public OrganizerDashboardStatsDto(long totalConcerts, long upcomingConcerts, long soldOutConcerts,
                                      long ticketsSold, long uniqueCustomers,
                                      double ticketRevenue, double averageBasket,
                                      long ticketsRemaining, double sellThroughRate) {
        this.totalConcerts = totalConcerts;
        this.upcomingConcerts = upcomingConcerts;
        this.soldOutConcerts = soldOutConcerts;
        this.ticketsSold = ticketsSold;
        this.uniqueCustomers = uniqueCustomers;
        this.ticketRevenue = ticketRevenue;
        this.averageBasket = averageBasket;
        this.ticketsRemaining = ticketsRemaining;
        this.sellThroughRate = sellThroughRate;
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

    public double getTicketRevenue() {
        return ticketRevenue;
    }

    public void setTicketRevenue(double ticketRevenue) {
        this.ticketRevenue = ticketRevenue;
    }

    public double getAverageBasket() {
        return averageBasket;
    }

    public void setAverageBasket(double averageBasket) {
        this.averageBasket = averageBasket;
    }

    public long getTicketsRemaining() {
        return ticketsRemaining;
    }

    public void setTicketsRemaining(long ticketsRemaining) {
        this.ticketsRemaining = ticketsRemaining;
    }

    public double getSellThroughRate() {
        return sellThroughRate;
    }

    public void setSellThroughRate(double sellThroughRate) {
        this.sellThroughRate = sellThroughRate;
    }
}
