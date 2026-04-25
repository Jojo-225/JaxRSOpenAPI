package fr.istic.taa.jaxrs.rest.organizer;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.ticket.CreateTicketDto;
import fr.istic.taa.jaxrs.dto.ticket.UpdateTicketDto;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/organise/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Organizer - Tickets", description = "Organizer back-office for ticket management")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed("ORGANIZER")
public class TicketResource {

    private final TicketDao ticketDao = new TicketDao();
    private final ConcertDao concertDao = new ConcertDao();
    private final OrganizerDao organizerDao = new OrganizerDao();
    private final CurrentUserService currentUserService = new CurrentUserService();

    @GET
    @Path("/")
    @Operation(summary = "Get my tickets", description = "Returns tickets from organizer concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Tickets returned")
    })
    public Response getMyTickets(@Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<Ticket> tickets = organizerDao.findConcertsByOrganizerId(organizer.getId()).stream()
                .flatMap(c -> c.getTickets().stream())
                .collect(Collectors.toList());

        return Response.ok(tickets.stream().map(ResponseMapper::toTicketDto).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one ticket", description = "Returns one organizer ticket", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket returned"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response getTicketById(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Ticket ticket = ticketDao.findOne(id);
        if (ticket == null || !isTicketOwnedByOrganizer(ticket, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }

        return Response.ok(ResponseMapper.toTicketDto(ticket)).build();
    }

    @GET
    @Path("/concert/{concertId}")
    @Operation(summary = "Get tickets by concert", description = "Returns tickets for one organizer concert", responses = {
            @ApiResponse(responseCode = "200", description = "Tickets returned"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getTicketsByConcert(@PathParam("concertId") Long concertId,
                                        @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        if (concert == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + concertId).build();
        }

        return Response.ok(concert.getTickets().stream()
                .map(ResponseMapper::toTicketDto)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Path("/concert/{concertId}")
    @Operation(summary = "Create ticket for concert", description = "Creates a ticket in one organizer concert", responses = {
            @ApiResponse(responseCode = "201", description = "Ticket created"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response createTicketForConcert(@PathParam("concertId") Long concertId,
                                           CreateTicketDto dto,
                                           @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        if (concert == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + concertId).build();
        }

        if (dto == null || dto.getTitle() == null || dto.getStatut() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("title/statut required").build();
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setCapacity(dto.getCapacity());
        ticket.setStatut(dto.getStatut());
        ticket.setConcert(concert);
        ticketDao.save(ticket);

        return Response.created(URI.create("/organise/tickets/" + ticket.getId()))
                .entity(ResponseMapper.toTicketDto(ticket))
                .build();
    }

    @POST
    @Operation(summary = "Create ticket", description = "Creates a ticket using concertId in payload", responses = {
            @ApiResponse(responseCode = "201", description = "Ticket created"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response createTicket(CreateTicketDto dto, @Context SecurityContext securityContext) {
        if (dto == null || dto.getConcertId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("concertId is required").build();
        }
        return createTicketForConcert(dto.getConcertId(), dto, securityContext);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update ticket", description = "Updates an organizer ticket", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket updated"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response updateTicket(@PathParam("id") Long id, UpdateTicketDto dto,
                                 @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Ticket ticket = ticketDao.findOne(id);
        if (ticket == null || !isTicketOwnedByOrganizer(ticket, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }

        if (dto.getTitle() != null) {
            ticket.setTitle(dto.getTitle());
        }
        if (dto.getCapacity() >= 0) {
            ticket.setCapacity(dto.getCapacity());
        }
        if (dto.getStatut() != null) {
            ticket.setStatut(dto.getStatut());
        }

        Ticket updated = ticketDao.update(ticket);
        return Response.ok(ResponseMapper.toTicketDto(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete ticket", description = "Deletes an organizer ticket", responses = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response deleteTicket(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Ticket ticket = ticketDao.findOne(id);
        if (ticket == null || !isTicketOwnedByOrganizer(ticket, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ticketDao.delete(ticket);
        return Response.noContent().build();
    }

    private Organizer getAuthenticatedOrganizer(SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (!(currentUser instanceof Organizer)) {
            return null;
        }
        return organizerDao.findOne(currentUser.getId());
    }

    private boolean isTicketOwnedByOrganizer(Ticket ticket, Long organizerId) {
        return ticket.getConcert() != null
                && ticket.getConcert().getOrganizer() != null
                && organizerId.equals(ticket.getConcert().getOrganizer().getId());
    }

    private boolean isConcertOwnedByOrganizer(Concert concert, Long organizerId) {
        return concert.getOrganizer() != null && organizerId.equals(concert.getOrganizer().getId());
    }
}
