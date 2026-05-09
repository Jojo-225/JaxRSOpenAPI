package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dto.notification.NotificationDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationSseService {

    // Singleton simple : toutes les ressources JAX-RS partagent les memes connexions SSE ouvertes.
    private static final NotificationSseService INSTANCE = new NotificationSseService();

    // userId -> connexions SSE ouvertes pour cet utilisateur.
    // CopyOnWriteArrayList permet d'ajouter/supprimer des connexions pendant l'envoi sans ConcurrentModificationException.
    private final Map<Long, List<SseEventSink>> sinksByUserId = new ConcurrentHashMap<>();

    // Objet fourni par JAX-RS pour construire les evenements SSE.
    // volatile garantit que les autres threads voient la derniere valeur enregistree.
    private volatile Sse sse;

    private NotificationSseService() {
    }

    public static NotificationSseService getInstance() {
        return INSTANCE;
    }

    public void register(Long userId, SseEventSink sink, Sse sse) {
        // A chaque ouverture de /notifications/stream/{userId}, on garde la connexion en memoire.
        this.sse = sse;
        sinksByUserId.computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>()).add(sink);
    }

    public void send(Long userId, NotificationDto notification) {
        Sse currentSse = this.sse;
        if (currentSse == null) {
            // Aucun client SSE n'a encore initialise le service.
            return;
        }

        List<SseEventSink> sinks = sinksByUserId.get(userId);
        if (sinks == null || sinks.isEmpty()) {
            // L'utilisateur n'est pas connecte en temps reel : la notification reste quand meme en base.
            return;
        }

        // L'evenement s'appellera "notification" cote Angular.
        OutboundSseEvent event = currentSse.newEventBuilder()
                .name("notification")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(NotificationDto.class, notification)
                .build();

        for (SseEventSink sink : sinks) {
            if (sink == null || sink.isClosed()) {
                // Nettoyage immediat des connexions deja fermees.
                remove(userId, sink);
                continue;
            }

            sink.send(event).exceptionally(error -> {
                // Si l'envoi echoue, la connexion est probablement coupee cote frontend.
                remove(userId, sink);
                return null;
            });
        }
    }

    public void remove(Long userId, SseEventSink sink) {
        List<SseEventSink> sinks = sinksByUserId.get(userId);
        if (sinks == null) {
            return;
        }

        sinks.remove(sink);
        if (sinks.isEmpty()) {
            // On supprime l'entree utilisateur pour eviter de garder des listes vides en memoire.
            sinksByUserId.remove(userId);
        }
    }
}
