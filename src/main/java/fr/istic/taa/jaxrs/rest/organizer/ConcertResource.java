package fr.istic.taa.jaxrs.rest.organizer;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.concert.CreateConcertDto;
import fr.istic.taa.jaxrs.dto.concert.UpdateConcertDto;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.response.OrganizerDashboardStatsDto;
import fr.istic.taa.jaxrs.service.CurrentUserService;
import fr.istic.taa.jaxrs.service.OrganizerStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/organise/concerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Organizer - Concerts", description = "Organizer back-office for concert management")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed("ORGANIZER")
public class ConcertResource {

    private final ConcertDao concertDao = new ConcertDao();
    private final OrganizerDao organizerDao = new OrganizerDao();
    private final CurrentUserService currentUserService = new CurrentUserService();
    private final OrganizerStatsService organizerStatsService = new OrganizerStatsService();

    @GET
    @Path("/")
    @Operation(summary = "Get my concerts", description = "Returns concerts owned by the authenticated organizer", responses = {
            @ApiResponse(responseCode = "200", description = "Concerts returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response getMyConcerts(@Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(organizerDao.findConcertsByOrganizerId(organizer.getId()).stream()
                .map(ResponseMapper::toConcertDto)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one of my concerts", description = "Returns a specific concert owned by the organizer", responses = {
            @ApiResponse(responseCode = "200", description = "Concert returned"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getMyConcertById(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(id);
        if (concert == null || !isOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
        }

        return Response.ok(ResponseMapper.toConcertDto(concert)).build();
    }

    @POST
    @Operation(summary = "Create concert", description = "Creates a concert for the authenticated organizer", responses = {
            @ApiResponse(responseCode = "201", description = "Concert created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    public Response createConcert(CreateConcertDto dto, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (dto == null || dto.getTopic() == null || dto.getDate() == null || dto.getDescription() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("topic/date/description required").build();
        }

        Concert concert = new Concert();
        concert.setTopic(dto.getTopic());
        concert.setDate(dto.getDate());
        concert.setDescription(dto.getDescription());
        concert.setOrganizer(organizer);
        concertDao.save(concert);

        return Response.created(URI.create("/organise/concerts/" + concert.getId()))
                .entity(ResponseMapper.toConcertDto(concert))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update concert", description = "Updates a concert owned by the organizer", responses = {
            @ApiResponse(responseCode = "200", description = "Concert updated"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response updateConcert(@PathParam("id") Long id, UpdateConcertDto dto,
                                  @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(id);
        if (concert == null || !isOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
        }

        if (dto.getTopic() != null) {
            concert.setTopic(dto.getTopic());
        }
        if (dto.getDate() != null) {
            concert.setDate(dto.getDate());
        }
        if (dto.getDescription() != null) {
            concert.setDescription(dto.getDescription());
        }

        Concert updated = concertDao.update(concert);
        return Response.ok(ResponseMapper.toConcertDto(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete concert", description = "Deletes a concert owned by the organizer", responses = {
            @ApiResponse(responseCode = "204", description = "Concert deleted"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response deleteConcert(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(id);
        if (concert == null || !isOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        concertDao.delete(concert);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/customers")
    @Operation(summary = "Get customers by concert", description = "Returns customers who bought tickets for one organizer concert", responses = {
            @ApiResponse(responseCode = "200", description = "Customers returned"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getCustomersByConcert(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(id);
        if (concert == null || !isOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
        }

        Set<Customer> customers = new HashSet<>();
        for (Ticket ticket : concert.getTickets()) {
            customers.addAll(ticket.getCustomers());
        }

        return Response.ok(customers.stream()
                .map(ResponseMapper::toUserDto)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/upcoming")
    @Operation(summary = "Get upcoming concerts", description = "Returns upcoming concerts for the organizer dashboard", responses = {
            @ApiResponse(responseCode = "200", description = "Upcoming concerts returned")
    })
    public Response getUpcomingConcerts(@Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Concert> upcoming = organizerDao.findConcertsByOrganizerId(organizer.getId()).stream()
                .filter(c -> c.getDate() != null && c.getDate().isAfter(now))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

        return Response.ok(upcoming.stream().map(ResponseMapper::toConcertDto).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/stats/dashboard")
    @Operation(summary = "Get organizer dashboard stats", description = "Returns aggregated statistics for organizer back-office", responses = {
            @ApiResponse(responseCode = "200", description = "Stats returned")
    })
    public Response getDashboardStats(@Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<Concert> concerts = organizerDao.findConcertsByOrganizerId(organizer.getId());
        LocalDateTime now = LocalDateTime.now();
        OrganizerDashboardStatsDto stats = organizerStatsService.buildDashboardStats(concerts, now);

        return Response.ok(stats).build();
    }

    private Organizer getAuthenticatedOrganizer(SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (!(currentUser instanceof Organizer)) {
            return null;
        }
        return organizerDao.findOne(currentUser.getId());
    }

    private boolean isOwnedByOrganizer(Concert concert, Long organizerId) {
        return concert.getOrganizer() != null
                && concert.getOrganizer().getId() != null
                && concert.getOrganizer().getId().equals(organizerId);
    }
}
