package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.dao.NotificationDao;
import fr.istic.taa.jaxrs.dao.TicketSaleDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Notification;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.TicketSale;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.notification.CreateNotificationDto;
import fr.istic.taa.jaxrs.dto.notification.NotificationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationService {

    private final CustomerDao customerDao = new CustomerDao();
    private final NotificationDao notificationDao = new NotificationDao();
    private final TicketSaleDao ticketSaleDao = new TicketSaleDao();
    private final NotificationSseService notificationSseService = NotificationSseService.getInstance();

    public NotificationDto create(CreateNotificationDto dto) {
        if (dto == null || dto.getUserId() == null || dto.getMessage() == null || dto.getMessage().isBlank()) {
            throw new IllegalArgumentException("userId and message are required");
        }

        // Toute notification est d'abord persistante : si l'utilisateur est hors ligne, il la retrouvera plus tard.
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setOrganizerId(dto.getOrganizerId());
        notification.setMessage(dto.getMessage());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationDao.save(notification);

        // Apres sauvegarde, on tente une diffusion temps reel via SSE.
        NotificationDto notificationDto = ResponseMapper.toNotificationDto(notification);
        notificationSseService.send(notificationDto.getUserId(), notificationDto);
        return notificationDto;
    }

    public List<NotificationDto> findByUserId(Long userId) {
        return notificationDao.findByUserId(userId)
                .stream()
                .map(ResponseMapper::toNotificationDto)
                .collect(Collectors.toList());
    }

    public long countUnreadByUserId(Long userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

    public NotificationDto markAsRead(Long notificationId) {
        return ResponseMapper.toNotificationDto(notificationDao.markAsRead(notificationId));
    }

    public int markAllAsRead(Long userId) {
        return notificationDao.markAllAsRead(userId);
    }

    public void createTicketSaleNotification(TicketSale sale) {
        if (sale == null) {
            return;
        }

        // Point de branchement metier : une vente de ticket notifie l'organisateur aux seuils importants.
        Concert concert = sale.getConcert();
        if (concert == null || concert.getId() == null || concert.getOrganizer() == null) {
            return;
        }

        Organizer organizer = concert.getOrganizer();
        String concertName = concert.getTopic() == null ? "votre concert" : concert.getTopic();

        // Apres l'achat, on recalcule l'etat global du concert pour connaitre le pourcentage vendu.
        long soldAfterSale = ticketSaleDao.countSoldTicketsByConcertId(concert.getId());
        long remaining = ticketSaleDao.countRemainingTicketsByConcertId(concert.getId());
        long totalTickets = soldAfterSale + remaining;

        if (totalTickets <= 0 || sale.getQuantity() <= 0) {
            return;
        }

        long soldBeforeSale = Math.max(0, soldAfterSale - sale.getQuantity());

        // On notifie uniquement si la vente actuelle fait passer de 0 a au moins 1 ticket vendu.
        if (soldBeforeSale == 0 && soldAfterSale > 0) {
            notifyOrganizer(organizer, "Le premier ticket a ete vendu pour votre concert : " + concertName);
        }

        notifyIfThresholdCrossed(organizer, concertName, soldBeforeSale, soldAfterSale, totalTickets, 50);
        notifyIfThresholdCrossed(organizer, concertName, soldBeforeSale, soldAfterSale, totalTickets, 80);

        if (soldBeforeSale < totalTickets && soldAfterSale >= totalTickets) {
            notifyOrganizer(organizer, "Tous les tickets ont ete vendus pour votre concert : " + concertName);
        }
    }

    private void notifyIfThresholdCrossed(
            Organizer organizer,
            String concertName,
            long soldBeforeSale,
            long soldAfterSale,
            long totalTickets,
            int threshold
    ) {
        long thresholdQuantity = minimumQuantityForThreshold(totalTickets, threshold);

        // Exemple : si une vente passe de 45% a 55%, elle declenche la notification des 50%.
        if (soldBeforeSale < thresholdQuantity && soldAfterSale >= thresholdQuantity) {
            notifyOrganizer(
                    organizer,
                    threshold + "% des tickets ont ete vendus pour votre concert : " + concertName
            );
        }
    }

    private long minimumQuantityForThreshold(long totalTickets, int threshold) {
        // Arrondi au-dessus pour eviter de notifier trop tot sur les petites jauges.
        return (long) Math.ceil((totalTickets * threshold) / 100.0d);
    }

    private void notifyOrganizer(Organizer organizer, String message) {
        if (organizer == null || organizer.getId() == null) {
            return;
        }

        create(new CreateNotificationDto(organizer.getId(), message));
    }

    public void createNewConcertNotification(Concert concert) {
        if (concert == null || concert.getId() == null || concert.getOrganizer() == null) {
            return;
        }

        createNewConcertNotification(concert, concert.getOrganizer());
    }

    public void createNewConcertNotification(Concert concert, Organizer organizer) {
        if (concert == null || concert.getId() == null || organizer == null || organizer.getId() == null) {
            return;
        }

        String concertName = concert.getTopic() == null ? "un nouveau concert" : concert.getTopic();
        String message = "Un nouveau concert vient d'etre programme : " + concertName;
        Long organizerId = organizer.getId();

        // Le client recoit l'annonce s'il suit tous les organisateurs ou l'organisateur de ce concert.
        for (Customer customer : customerDao.findAll()) {
            if (customer.getId() != null && shouldReceiveNewConcertNotification(customer, organizer)) {
                create(new CreateNotificationDto(customer.getId(), organizerId, message));
            }
        }
    }

    private boolean shouldReceiveNewConcertNotification(Customer customer, Organizer organizer) {
        if (customer == null || organizer == null || organizer.getId() == null) {
            return false;
        }

        if (customer.isNotifyAllOrganizers()) {
            return true;
        }

        return customer.getFollowedOrganizers() != null
                && customer.getFollowedOrganizers()
                        .stream()
                        .anyMatch(followedOrganizer -> organizer.getId().equals(followedOrganizer.getId()));
    }
}
