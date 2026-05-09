package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.NotificationDao;
import fr.istic.taa.jaxrs.domain.Concert;
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

    private final NotificationDao notificationDao = new NotificationDao();
    private final NotificationSseService notificationSseService = NotificationSseService.getInstance();

    public NotificationDto create(CreateNotificationDto dto) {
        if (dto == null || dto.getUserId() == null || dto.getMessage() == null || dto.getMessage().isBlank()) {
            throw new IllegalArgumentException("userId and message are required");
        }

        // Toute notification est d'abord persistante : si l'utilisateur est hors ligne, il la retrouvera plus tard.
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
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

        // Point de branchement metier : une vente de ticket notifie l'organisateur du concert.
        Concert concert = sale.getConcert();
        if (concert == null || concert.getOrganizer() == null) {
            return;
        }

        Organizer organizer = concert.getOrganizer();
        String concertName = concert.getTopic() == null ? "votre concert" : concert.getTopic();
        create(new CreateNotificationDto(
                organizer.getId(),
                "Un ticket vient d'etre vendu pour votre concert : " + concertName
        ));
    }
}
