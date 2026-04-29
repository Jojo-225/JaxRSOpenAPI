package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.CustomerDao;
import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.dao.TicketSaleDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.domain.TicketSale;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.ticket.BuyTicketDto;
import fr.istic.taa.jaxrs.dto.ticket.CustomerTicketPurchaseResponseDto;
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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Map;
import java.time.LocalDateTime;
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
    private final TicketSaleDao ticketSaleDao = new TicketSaleDao();

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

    @GET
    @Path("/tickets/{id}")
    @Operation(summary = "Get ticket detail", description = "Returns ticket detail by id for authenticated customer/organizer", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket returned"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response getTicketDetail(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", "unauthorized")).build();
        }

        Ticket ticket = ticketDao.findOne(id);
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "ticket not found")).build();
        }

        return Response.ok(ResponseMapper.toTicketDto(ticket)).build();
    }

   @POST
    @Path("/buyticket")
    @Operation(summary = "Buy ticket", description = "Buys one or more tickets for the authenticated customer", responses = {
        @ApiResponse(responseCode = "200", description = "Ticket bought successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Only customers can buy tickets"),
        @ApiResponse(responseCode = "404", description = "Ticket not found"),
        @ApiResponse(responseCode = "409", description = "Ticket unavailable")
    })
    public Response buyTicket(BuyTicketDto dto, @Context SecurityContext securityContext) {
    if (dto == null || dto.getTicketId() == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "ticketId is required"))
                .build();
    }

    int quantity = dto.getQuantity() == null ? 1 : dto.getQuantity();

    if (quantity <= 0) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "quantity must be greater than 0"))
                .build();
    }

    User currentUser = currentUserService.getCurrentUser(securityContext);

    if (currentUser == null) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "unauthorized"))
                .build();
    }

    if (!(currentUser instanceof Customer)) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "This action is available only for customer accounts"))
                .build();
    }

    Customer customer = (Customer) currentUser;

    try {
        TicketSale sale = ticketSaleDao.buyTicket(customer.getId(), dto.getTicketId(), quantity);

        CustomerTicketPurchaseResponseDto responseDto = ResponseMapper.toTicketPurchaseDto(sale);

        return Response.ok(responseDto).build();

    } catch (IllegalArgumentException e) {
        if ("TICKET_NOT_FOUND".equals(e.getMessage())) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "ticket not found"))
                    .build();
        }

        if ("INVALID_QUANTITY".equals(e.getMessage())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "quantity must be greater than 0"))
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "invalid request"))
                .build();

    } catch (IllegalStateException e) {
        if ("TICKET_UNAVAILABLE".equals(e.getMessage())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "ticket is no longer available or requested quantity is too high"))
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", "ticket unavailable"))
                .build();
    }
}

@GET
@Path("/my-purchases")
@Operation(summary = "Get my purchases", description = "Returns ticket purchases made by the authenticated customer", responses = {
        @ApiResponse(responseCode = "200", description = "Purchases returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Only customers can access this action")
})
public Response myPurchases(@Context SecurityContext securityContext) {
    User currentUser = currentUserService.getCurrentUser(securityContext);

    if (currentUser == null) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "unauthorized"))
                .build();
    }

    if (!(currentUser instanceof Customer)) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "This action is available only for customer accounts"))
                .build();
    }

    Customer customer = (Customer) currentUser;

    return Response.ok(
            ticketSaleDao.findByCustomerId(customer.getId())
                    .stream()
                    .map(ResponseMapper::toTicketPurchaseDto)
                    .collect(Collectors.toList())
    ).build();
}
}
