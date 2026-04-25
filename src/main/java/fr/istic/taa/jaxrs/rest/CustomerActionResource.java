package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.ticket.BuyTicketDto;
import fr.istic.taa.jaxrs.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/custom")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customer Actions", description = "Customer-level actions available at API root")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed({"CUSTOMER", "ORGANIZER"})
public class CustomerActionResource {

    private final CurrentUserService currentUserService = new CurrentUserService();
    private final CustomerDao customerDao = new CustomerDao();
    private final TicketDao ticketDao = new TicketDao();

    @GET
    @Path("/profile")
    @Operation(summary = "Get profile", description = "Returns the profile of the authenticated user", responses = {
            @ApiResponse(responseCode = "200", description = "Profile returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response profile(@Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        return Response.ok(ResponseMapper.toUserDto(currentUser)).build();
    }

    @GET
    @Path("/mytickets")
    @Operation(summary = "Get my tickets", description = "Returns tickets bought by the authenticated customer", responses = {
            @ApiResponse(responseCode = "200", description = "Tickets returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Only customers can access this action")
    })
    public Response myTickets(@Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        if (!(currentUser instanceof Customer)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "This action is available only for customer accounts"))
                    .build();
        }

        Customer customer = (Customer) currentUser;
        return Response.ok(customerDao.findTicketsByCustomerId(customer.getId()).stream()
                .map(ResponseMapper::toTicketDto)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Path("/buyticket")
    @Operation(summary = "Buy ticket", description = "Buys a ticket for the authenticated customer", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket bought successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Only customers can buy tickets"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "409", description = "Ticket unavailable or already bought")
    })
    public Response buyTicket(BuyTicketDto dto, @Context SecurityContext securityContext) {
        if (dto == null || dto.getTicketId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "ticketId is required")).build();
        }

        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }
        if (!(currentUser instanceof Customer)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "This action is available only for customer accounts"))
                    .build();
        }

        Ticket ticket = ticketDao.findOne(dto.getTicketId());
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "ticket not found")).build();
        }

        if (!"available".equalsIgnoreCase(ticket.getStatut()) || ticket.getCapacity() <= 0) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "ticket is no longer available"))
                    .build();
        }

        Customer customer = (Customer) currentUser;
        boolean alreadyBought = ticket.getCustomers().stream().anyMatch(c -> c.getId().equals(customer.getId()));
        if (alreadyBought) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "ticket already bought by this customer"))
                    .build();
        }

        ticket.addCustomer(customer);
        customer.addTicketAchete(ticket);
        ticket.setCapacity(ticket.getCapacity() - 1);
        if (ticket.getCapacity() <= 0) {
            ticket.setStatut("soldout");
        }

        Ticket updatedTicket = ticketDao.update(ticket);
        return Response.ok(ResponseMapper.toTicketDto(updatedTicket)).build();
    }
}

