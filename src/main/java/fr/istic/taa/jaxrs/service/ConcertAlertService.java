package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.dao.ConcertSearchAlertDao;
import fr.istic.taa.jaxrs.dao.UserDao;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.ConcertSearchAlert;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.alert.ConcertAlertResponseDto;
import fr.istic.taa.jaxrs.dto.alert.CreateConcertAlertDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ConcertAlertService {

    private final ConcertSearchAlertDao alertDao = new ConcertSearchAlertDao();
    private final ConcertDao concertDao = new ConcertDao();
    private final UserDao userDao = new UserDao();
    private final EmailService emailService = new EmailService();

    public ConcertAlertResponseDto createAlert(Long userId, CreateConcertAlertDto dto) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (dto == null) {
            throw new IllegalArgumentException("payload is required");
        }

        ConcertSearchAlert alert = new ConcertSearchAlert();
        alert.setUserId(userId);
        alert.setTopic(clean(dto.getTopic()));
        alert.setArtistName(clean(dto.getArtistName()));
        alert.setDate(clean(dto.getDate()));
        alert.setDescription(clean(dto.getDescription()));
        alert.setOrganizerName(clean(dto.getOrganizerName()));
        alert.setPriceMin(dto.getPriceMin());
        alert.setPriceMax(dto.getPriceMax());
        alert.setActive(dto.getActive() == null || dto.getActive());
        alert.setCreatedAt(LocalDateTime.now());
        alert.setLastNotifiedAt(LocalDateTime.now());

        alertDao.save(alert);
        return toDto(alert);
    }

    public List<ConcertAlertResponseDto> getAlertsByUser(Long userId) {
        return alertDao.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public boolean deleteAlert(Long userId, Long alertId) {
        ConcertSearchAlert alert = alertDao.findOne(alertId);
        if (alert == null || !alert.getUserId().equals(userId)) {
            return false;
        }
        alertDao.delete(alert);
        return true;
    }

    public int processDailyAlerts() {
        List<ConcertSearchAlert> alerts = alertDao.findActiveAlerts();
        int sentEmails = 0;
        LocalDateTime now = LocalDateTime.now();

        for (ConcertSearchAlert alert : alerts) {
            LocalDateTime since = alert.getLastNotifiedAt() != null
                    ? alert.getLastNotifiedAt()
                    : (alert.getCreatedAt() != null ? alert.getCreatedAt() : now.minusDays(1));

            List<Concert> createdSince = concertDao.findConcertsCreatedAfter(since);
            List<Concert> matched = createdSince.stream()
                    .filter(concert -> matches(alert, concert))
                    .collect(Collectors.toList());

            if (!matched.isEmpty()) {
                User user = userDao.findOne(alert.getUserId());
                if (user != null && user.getMail() != null && !user.getMail().isBlank()) {
                    String subject = "Nouveaux concerts correspondant a votre alerte";
                    String body = buildEmailBody(matched, alert);
                    boolean sent = emailService.sendPlainText(user.getMail(), subject, body);
                    if (sent) {
                        sentEmails++;
                    }
                }
            }

            alertDao.markLastNotified(alert.getId(), now);
        }

        return sentEmails;
    }

    private ConcertAlertResponseDto toDto(ConcertSearchAlert alert) {
        return new ConcertAlertResponseDto(
                alert.getId(),
                alert.getUserId(),
                alert.getTopic(),
                alert.getArtistName(),
                alert.getDate(),
                alert.getDescription(),
                alert.getOrganizerName(),
                alert.getPriceMin(),
                alert.getPriceMax(),
                alert.isActive(),
                alert.getCreatedAt(),
                alert.getLastNotifiedAt()
        );
    }

    private boolean matches(ConcertSearchAlert alert, Concert concert) {
        if (concert == null) {
            return false;
        }

        if (!containsIgnoreCase(concert.getTopic(), alert.getTopic())) {
            return false;
        }

        if (!containsIgnoreCase(concert.getDescription(), alert.getDescription())) {
            return false;
        }

        if (!matchesDate(alert.getDate(), concert.getDate())) {
            return false;
        }

        if (!matchesOrganizer(alert.getOrganizerName(), concert)) {
            return false;
        }

        if (!matchesArtist(alert.getArtistName(), concert.getArtists())) {
            return false;
        }

        return matchesPrice(alert.getPriceMin(), alert.getPriceMax(), concert.getTickets());
    }

    private boolean containsIgnoreCase(String value, String criteria) {
        if (criteria == null || criteria.isBlank()) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return value.toLowerCase(Locale.ROOT).contains(criteria.toLowerCase(Locale.ROOT));
    }

    private boolean matchesDate(String criteriaDate, LocalDateTime concertDate) {
        if (criteriaDate == null || criteriaDate.isBlank()) {
            return true;
        }
        if (concertDate == null) {
            return false;
        }
        LocalDate expected = parseDate(criteriaDate.trim());
        if (expected == null) {
            return false;
        }
        return expected.equals(concertDate.toLocalDate());
    }

    private LocalDate parseDate(String raw) {
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(raw, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private boolean matchesOrganizer(String organizerCriteria, Concert concert) {
        if (organizerCriteria == null || organizerCriteria.isBlank()) {
            return true;
        }
        if (concert.getOrganizer() == null) {
            return false;
        }
        String criteria = organizerCriteria.toLowerCase(Locale.ROOT);
        String fullName = (safe(concert.getOrganizer().getFirstName()) + " " + safe(concert.getOrganizer().getLastName()))
                .toLowerCase(Locale.ROOT);
        return fullName.contains(criteria);
    }

    private boolean matchesArtist(String artistCriteria, List<Artist> artists) {
        if (artistCriteria == null || artistCriteria.isBlank()) {
            return true;
        }
        if (artists == null || artists.isEmpty()) {
            return false;
        }
        String criteria = artistCriteria.toLowerCase(Locale.ROOT);
        return artists.stream()
                .anyMatch(artist -> artist.getName() != null && artist.getName().toLowerCase(Locale.ROOT).contains(criteria));
    }

    private boolean matchesPrice(Double min, Double max, List<Ticket> tickets) {
        if (min == null && max == null) {
            return true;
        }
        if (tickets == null || tickets.isEmpty()) {
            return false;
        }
        return tickets.stream().anyMatch(ticket -> {
            double price = ticket.getPrice();
            if (min != null && price < min) {
                return false;
            }
            if (max != null && price > max) {
                return false;
            }
            return true;
        });
    }

    private String buildEmailBody(List<Concert> concerts, ConcertSearchAlert alert) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bonjour,\n\n");
        sb.append("De nouveaux concerts correspondent a votre alerte de recherche:\n\n");
        for (Concert concert : concerts) {
            sb.append("- ").append(safe(concert.getTopic()));
            if (concert.getDate() != null) {
                sb.append(" (").append(concert.getDate()).append(")");
            }
            if (concert.getOrganizer() != null) {
                sb.append(" - Organisateur: ")
                        .append(safe(concert.getOrganizer().getFirstName()))
                        .append(" ")
                        .append(safe(concert.getOrganizer().getLastName()));
            }
            sb.append("\n");
        }
        sb.append("\nCriteres de l'alerte:\n");
        sb.append("topic=").append(safe(alert.getTopic())).append(", ");
        sb.append("artist=").append(safe(alert.getArtistName())).append(", ");
        sb.append("date=").append(safe(alert.getDate())).append(", ");
        sb.append("organizer=").append(safe(alert.getOrganizerName())).append(", ");
        sb.append("priceMin=").append(alert.getPriceMin()).append(", ");
        sb.append("priceMax=").append(alert.getPriceMax()).append("\n");
        sb.append("\nA bientot.");
        return sb.toString();
    }

    private String clean(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
