package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.alert.ConcertAlertResponseDto;
import fr.istic.taa.jaxrs.dto.alert.CreateConcertAlertDto;
import fr.istic.taa.jaxrs.service.ConcertAlertScheduler;
import fr.istic.taa.jaxrs.service.ConcertAlertService;
import fr.istic.taa.jaxrs.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;
import java.util.Map;

@Path("/api/custom/concert-alerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Concert Alerts", description = "Saved concert search alerts and daily email digest")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed({"CUSTOMER", "ORGANIZER"})
public class ConcertAlertResource {

    private final CurrentUserService currentUserService = new CurrentUserService();
    private final ConcertAlertService concertAlertService = new ConcertAlertService();

    @POST
    @Operation(summary = "Create concert alert", description = "Creates a saved alert from search criteria", responses = {
            @ApiResponse(responseCode = "201", description = "Alert created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response create(CreateConcertAlertDto dto, @Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        try {
            ConcertAlertResponseDto alert = concertAlertService.createAlert(currentUser.getId(), dto);
            return Response.created(URI.create("/api/custom/concert-alerts/" + alert.getId())).entity(alert).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Operation(summary = "List my alerts", description = "Returns all saved alerts of the authenticated user")
    public Response list(@Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        return Response.ok(concertAlertService.getAlertsByUser(currentUser.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete my alert", description = "Deletes one alert belonging to the authenticated user", responses = {
            @ApiResponse(responseCode = "204", description = "Alert deleted"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    public Response delete(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        boolean deleted = concertAlertService.deleteAlert(currentUser.getId(), id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "alert not found")).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/run-daily-now")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Run daily alert job now", description = "Manual trigger to test email alert sending immediately")
    public Response runNow() {
        int sent = ConcertAlertScheduler.getInstance().runNow();
        return Response.ok(Map.of("sentEmails", sent)).build();
    }
}
