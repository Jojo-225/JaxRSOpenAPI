package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.TicketSale;
import fr.istic.taa.jaxrs.dto.response.OrganizerDashboardStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public class OrganizerStatsService {

    public OrganizerDashboardStatsDto buildDashboardStats(List<Concert> concerts, List<TicketSale> sales, LocalDateTime now) {
        long totalConcerts = concerts.size();
        long upcomingConcerts = concerts.stream()
                .filter(c -> c.getDate() != null && c.getDate().isAfter(now))
                .count();

        long soldOutConcerts = concerts.stream()
                .filter(c -> !c.getTickets().isEmpty())
                .filter(c -> c.getTickets().stream()
                        .allMatch(t -> t.getCapacity() <= 0 || "soldout".equalsIgnoreCase(t.getStatut())))
                .count();

        long ticketsSold = sales.stream()
                .mapToLong(this::saleQuantity)
                .sum();

        long uniqueCustomers = sales.stream()
                .map(TicketSale::getCustomer)
                .filter(customer -> customer != null && customer.getId() != null)
                .map(Customer::getId)
                .distinct()
                .count();

        long ticketsRemaining = concerts.stream()
                .flatMap(c -> c.getTickets().stream())
                .mapToLong(t -> Math.max(t.getCapacity(), 0))
                .sum();

        double ticketRevenue = round2(sales.stream()
                .mapToDouble(this::saleTotal)
                .sum());

        double averageBasket = sales.isEmpty() ? 0.0d : round2(ticketRevenue / sales.size());
        double sellThroughRate = (ticketsSold + ticketsRemaining) > 0
                ? round2((ticketsSold * 100.0d) / (ticketsSold + ticketsRemaining))
                : 0.0d;

        return new OrganizerDashboardStatsDto(
                totalConcerts,
                upcomingConcerts,
                soldOutConcerts,
                ticketsSold,
                uniqueCustomers,
                ticketRevenue,
                averageBasket,
                ticketsRemaining,
                sellThroughRate
        );
    }

    private double round2(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }

    private double saleTotal(TicketSale sale) {
        if (sale.getTotalPrice() > 0) {
            return sale.getTotalPrice();
        }
        return sale.getPriceAtPurchase() * saleQuantity(sale);
    }

    private int saleQuantity(TicketSale sale) {
        return sale.getQuantity() > 0 ? sale.getQuantity() : 1;
    }
}
