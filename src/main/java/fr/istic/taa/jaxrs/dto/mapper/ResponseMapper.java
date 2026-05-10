package fr.istic.taa.jaxrs.dto.mapper;

import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Notification;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.notification.NotificationDto;
import fr.istic.taa.jaxrs.dto.notification.NotificationPreferencesDto;
import fr.istic.taa.jaxrs.dto.response.ArtistResponseDto;
import fr.istic.taa.jaxrs.dto.response.ConcertResponseDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;
import fr.istic.taa.jaxrs.dto.response.UserResponseDto;
import fr.istic.taa.jaxrs.domain.TicketSale;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.dto.ticket.CustomerTicketPurchaseResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ResponseMapper {

    private ResponseMapper() {
    }

    public static ArtistResponseDto toArtistDto(Artist artist) {
        if (artist == null) {
            return null;
        }

        List<Long> concertIds = artist.getConcerts() == null
                ? Collections.emptyList()
                : artist.getConcerts().stream().map(Concert::getId).collect(Collectors.toList());

        return new ArtistResponseDto(artist.getId(), artist.getName(), concertIds);
    }

    public static ConcertResponseDto toConcertDto(Concert concert) {
        if (concert == null) {
            return null;
        }

        Long organizerId = concert.getOrganizer() != null ? concert.getOrganizer().getId() : null;
        List<Long> ticketIds = concert.getTickets() == null
                ? Collections.emptyList()
                : concert.getTickets().stream().map(Ticket::getId).collect(Collectors.toList());
        Double minPrice = concert.getTickets() == null || concert.getTickets().isEmpty()
                ? null
                : concert.getTickets().stream().mapToDouble(Ticket::getPrice).min().orElse(0.0d);
        List<Long> artistIds = concert.getArtists() == null
                ? Collections.emptyList()
                : concert.getArtists().stream().map(Artist::getId).collect(Collectors.toList());

        return new ConcertResponseDto(
                concert.getId(),
                concert.getTopic(),
                concert.getDate(),
                concert.getDescription(),
                organizerId,
                minPrice,
                ticketIds,
                artistIds
        );
    }

    public static TicketResponseDto toTicketDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        Long concertId = ticket.getConcert() != null ? ticket.getConcert().getId() : null;
        List<Long> customerIds = ticket.getCustomers() == null
                ? Collections.emptyList()
                : ticket.getCustomers().stream()
                        .map(Customer::getId)
                        .distinct()
                        .collect(Collectors.toList());

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCapacity(),
                ticket.getPrice(),
                ticket.getStatut(),
                concertId,
                customerIds
        );
    }

    public static UserResponseDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getLastName(),
                user.getFirstName(),
                user.getDateOfBirth(),
                user.getMail(),
                roleFromUser(user)
        );
    }

    public static NotificationDto toNotificationDto(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getOrganizerId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static NotificationPreferencesDto toNotificationPreferencesDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        List<Long> organizerIds = customer.getFollowedOrganizers() == null
                ? Collections.emptyList()
                : customer.getFollowedOrganizers().stream()
                        .map(Organizer::getId)
                        .distinct()
                        .collect(Collectors.toList());

        return new NotificationPreferencesDto(customer.isNotifyAllOrganizers(), organizerIds);
    }

    private static String roleFromUser(User user) {
        if (user instanceof Admin) {
            return "ADMIN";
        }
        if (user instanceof Organizer) {
            return "ORGANIZER";
        }
        if (user instanceof Customer) {
            return "CUSTOMER";
        }
        return "USER";
    }

    public static CustomerTicketPurchaseResponseDto toTicketPurchaseDto(TicketSale sale) {
    CustomerTicketPurchaseResponseDto dto = new CustomerTicketPurchaseResponseDto();

    dto.setPurchaseId(sale.getId());
    dto.setReference(sale.getReference());
    dto.setPurchaseDate(sale.getPurchaseDate() != null ? sale.getPurchaseDate().toString() : null);

    dto.setQuantity(sale.getQuantity());
    dto.setUnitPrice(sale.getPriceAtPurchase());
    dto.setTotalPrice(sale.getTotalPrice());
    dto.setStatus(sale.getStatus());

    Ticket ticket = sale.getTicket();

    if (ticket != null) {
        dto.setTicketId(ticket.getId());
        dto.setTicketTitle(ticket.getTitle());

        Concert concert = ticket.getConcert();

        if (concert != null) {
            dto.setConcertId(concert.getId());
            dto.setConcertTopic(concert.getTopic());
            dto.setConcertDate(concert.getDate() != null ? concert.getDate().toString() : null);
        }
    }

    if (sale.getConcert() != null && dto.getConcertId() == null) {
        dto.setConcertId(sale.getConcert().getId());
        dto.setConcertTopic(sale.getConcert().getTopic());
        dto.setConcertDate(sale.getConcert().getDate() != null ? sale.getConcert().getDate().toString() : null);
    }

    return dto;
}
}
