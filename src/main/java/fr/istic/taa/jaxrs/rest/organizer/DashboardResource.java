package fr.istic.taa.jaxrs.rest.organizer;

import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.response.ConcertResponseDto;
import fr.istic.taa.jaxrs.dto.response.OrganizerDashboardDto;
import fr.istic.taa.jaxrs.dto.response.OrganizerDashboardStatsDto;
import fr.istic.taa.jaxrs.service.CurrentUserService;
import fr.istic.taa.jaxrs.service.OrganizerStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/organise/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Organizer - Dashboard", description = "Organizer dashboard endpoints")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed("ORGANIZER")
public class DashboardResource {

    private final OrganizerDao organizerDao = new OrganizerDao();
    private final CurrentUserService currentUserService = new CurrentUserService();
    private final OrganizerStatsService organizerStatsService = new OrganizerStatsService();

    @GET
    @Operation(summary = "Get dashboard", description = "Returns dashboard payload with stats, upcoming concerts and quick actions", responses = {
            @ApiResponse(responseCode = "200", description = "Dashboard returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response getDashboard(@QueryParam("upcomingLimit") @DefaultValue("5") int upcomingLimit,
                                 @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<Concert> concerts = organizerDao.findConcertsByOrganizerId(organizer.getId());
        LocalDateTime now = LocalDateTime.now();

        List<ConcertResponseDto> upcomingConcerts = concerts.stream()
                .filter(c -> c.getDate() != null && c.getDate().isAfter(now))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .limit(Math.max(upcomingLimit, 0))
                .map(ResponseMapper::toConcertDto)
                .collect(Collectors.toList());

        OrganizerDashboardStatsDto stats = organizerStatsService.buildDashboardStats(concerts, now);

        OrganizerDashboardDto dashboard = new OrganizerDashboardDto(
                stats,
                upcomingConcerts,
                List.of("create_concert", "launch_promo", "export_sales")
        );

        return Response.ok(dashboard).build();
    }

    private Organizer getAuthenticatedOrganizer(SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (!(currentUser instanceof Organizer)) {
            return null;
        }
        return organizerDao.findOne(currentUser.getId());
    }
}
