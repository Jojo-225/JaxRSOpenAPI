package fr.istic.taa.jaxrs.dto.mapper;

import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.response.ArtistResponseDto;
import fr.istic.taa.jaxrs.dto.response.ConcertResponseDto;
import fr.istic.taa.jaxrs.dto.response.TicketResponseDto;
import fr.istic.taa.jaxrs.dto.response.UserResponseDto;

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
        List<Long> artistIds = concert.getArtists() == null
                ? Collections.emptyList()
                : concert.getArtists().stream().map(Artist::getId).collect(Collectors.toList());

        return new ConcertResponseDto(
                concert.getId(),
                concert.getTopic(),
                concert.getDate(),
                concert.getDescription(),
                organizerId,
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
                : ticket.getCustomers().stream().map(Customer::getId).collect(Collectors.toList());

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCapacity(),
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
}
