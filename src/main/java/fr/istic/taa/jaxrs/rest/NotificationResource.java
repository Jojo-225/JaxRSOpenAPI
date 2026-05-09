package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dto.notification.CreateNotificationDto;
import fr.istic.taa.jaxrs.dto.notification.NotificationDto;
import fr.istic.taa.jaxrs.service.NotificationService;
import fr.istic.taa.jaxrs.service.NotificationSseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.net.URI;
import java.util.Map;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Notifications", description = "Notifications and real-time SSE stream")
public class NotificationResource {

    private final NotificationService notificationService = new NotificationService();
    private final NotificationSseService notificationSseService = NotificationSseService.getInstance();

    @POST
    @Operation(summary = "Create notification", description = "Creates a notification and sends it through SSE when the user is connected", responses = {
            @ApiResponse(responseCode = "201", description = "Notification created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    public Response create(CreateNotificationDto dto) {
        try {
            NotificationDto notification = notificationService.create(dto);
            return Response.created(URI.create("/notifications/" + notification.getId()))
                    .entity(notification)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/user/{userId}")
    @Operation(summary = "Get user notifications", description = "Returns all notifications for a user")
    public Response findByUserId(@PathParam("userId") Long userId) {
        return Response.ok(notificationService.findByUserId(userId)).build();
    }

    @GET
    @Path("/user/{userId}/unread-count")
    @Operation(summary = "Get unread notification count", description = "Returns unread notification count for a user")
    public Response countUnreadByUserId(@PathParam("userId") Long userId) {
        return Response.ok(Map.of("count", notificationService.countUnreadByUserId(userId))).build();
    }

    @PUT
    @Path("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Marks one notification as read", responses = {
            @ApiResponse(responseCode = "200", description = "Notification updated"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public Response markAsRead(@PathParam("notificationId") Long notificationId) {
        NotificationDto notification = notificationService.markAsRead(notificationId);
        if (notification == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "notification not found"))
                    .build();
        }
        return Response.ok(notification).build();
    }

    @PUT
    @Path("/user/{userId}/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Marks all unread notifications as read for a user")
    public Response markAllAsRead(@PathParam("userId") Long userId) {
        int updated = notificationService.markAllAsRead(userId);
        return Response.ok(Map.of("updated", updated)).build();
    }

    @GET
    @Path("/stream/{userId}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Operation(summary = "Open notification SSE stream", description = "Keeps an SSE connection open for real-time notifications")
    public void stream(@PathParam("userId") Long userId,
                       @Context SseEventSink sink,
                       @Context Sse sse) {
        if (sink == null || sse == null) {
            return;
        }

        notificationSseService.register(userId, sink, sse);
    }
}
