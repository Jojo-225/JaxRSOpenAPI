package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.response.OrganizerDashboardStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public class OrganizerStatsService {

    public OrganizerDashboardStatsDto buildDashboardStats(List<Concert> concerts, LocalDateTime now) {
        long totalConcerts = concerts.size();
        long upcomingConcerts = concerts.stream()
                .filter(c -> c.getDate() != null && c.getDate().isAfter(now))
                .count();

        long soldOutConcerts = concerts.stream()
                .filter(c -> !c.getTickets().isEmpty())
                .filter(c -> c.getTickets().stream()
                        .allMatch(t -> t.getCapacity() <= 0 || "soldout".equalsIgnoreCase(t.getStatut())))
                .count();

        long ticketsSold = concerts.stream()
                .flatMap(c -> c.getTickets().stream())
                .mapToLong(t -> t.getCustomers().size())
                .sum();

        long uniqueCustomers = concerts.stream()
                .flatMap(c -> c.getTickets().stream())
                .flatMap(t -> t.getCustomers().stream())
                .map(Customer::getId)
                .distinct()
                .count();

        long ticketsRemaining = concerts.stream()
                .flatMap(c -> c.getTickets().stream())
                .mapToLong(t -> Math.max(t.getCapacity(), 0))
                .sum();

        double ticketRevenue = round2(concerts.stream()
                .flatMap(c -> c.getTickets().stream())
                .mapToDouble(t -> t.getPrice() * t.getCustomers().size())
                .sum());

        double averageBasket = ticketsSold > 0 ? round2(ticketRevenue / ticketsSold) : 0.0d;
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
}

