package fr.istic.taa.jaxrs.rest;

import java.net.URI;
import java.util.stream.Collectors;

import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.ticket.CreateTicketDto;
import fr.istic.taa.jaxrs.dto.ticket.UpdateTicketDto;
import fr.istic.taa.jaxrs.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE; 
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/tickets")
@Produces({"application/json"})
@Tag(name = "Ticket", description = "API for managing tickets")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed({"ADMIN", "ORGANIZER", "CUSTOMER"})
public class TicketResource {
    private final TicketDao dao = new TicketDao(); 
    private final TicketService ticketService = new TicketService();
   
    @GET
    @Path("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Returns a single ticket", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of ticket"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response getTicketById(@PathParam("id") Long id) {
        try{
            Ticket ticket = ticketService.findOne(id);
            return Response.ok(ResponseMapper.toTicketDto(ticket)).build();
            // throw new RuntimeException("Ticket not found for id: " + id);                    
        }catch (RuntimeException e) {            
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }
    }

    //Read All
    @GET
    @Path("/")
    @Operation(summary = "Get all tickets", description = "Returns a list of all tickets", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of tickets")
    })
    public Response getAllTicket() {
        return Response.ok(ticketService.findAll().stream()
                .map(ResponseMapper::toTicketDto)
                .collect(Collectors.toList())).build();
    }

    //Create
    @POST
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket", responses = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Response createTicket(CreateTicketDto createTicketDto) {
        try{
        Ticket ticket= ticketService.createTicket(createTicketDto);
         return Response.created(URI.create("/tickets/"+ticket.getId()))
                 .entity(ResponseMapper.toTicketDto(ticket))
                 .build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create Ticket").build();

        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Update a ticket", description = "Updates an existing ticket", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response updateTicket(@PathParam("id") Long id, UpdateTicketDto updateTicketDto) {
        Ticket existingTicket = dao.findOne(id);
        if (existingTicket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }
           try {
            Ticket updateTicket = ticketService.update(updateTicketDto, existingTicket);
            return Response.ok(ResponseMapper.toTicketDto(updateTicket)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update Ticket").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Delete a ticket", description = "Deletes an existing ticket", responses = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response deleteTicket (@PathParam("id") Long id) {
        Ticket existingTicket = dao.findOne(id);
        if (existingTicket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
         try {
            ticketService.delete(existingTicket);
            return Response.noContent().entity("Ticket deleted successfully").build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete ticket").build();
        }
    }
    
    
}
