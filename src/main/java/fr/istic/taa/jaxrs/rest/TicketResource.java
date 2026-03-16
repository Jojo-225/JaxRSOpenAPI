package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;

import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.dto.CreateTicketDto;
import fr.istic.taa.jaxrs.dto.UpdateTicketDto;
import fr.istic.taa.jaxrs.service.TicketService;
import jakarta.ws.rs.core.Response; 
import jakarta.ws.rs.PathParam;

@Path("tickets")
@Produces({"application/json"})
public class TicketResource {
    private final TicketDao dao = new TicketDao(); 
    private final TicketService ticketService = new TicketService();
   
    @GET
    @Path("/{id}")
    public Response getTicketById(@PathParam("id") Long id) {
        try{
            Ticket ticket = ticketService.findOne(id);
            return Response.ok(ticket).build();
            // throw new RuntimeException("Ticket not found for id: " + id);                    
        }catch (RuntimeException e) {            
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }
    }

    //Read All
    @GET
    @Path("/")
    public Response getAllTicket() {
        return Response.ok(ticketService.findAll()).build();
    }

    //Create
    @POST
    public Response createTicket(CreateTicketDto createTicketDto) {
        try{
        Ticket ticket= ticketService.createTicket(createTicketDto);
         return Response.created(URI.create("/tickets/"+ticket.getId())).entity(ticket).build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create Ticket").build();

        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") Long id, UpdateTicketDto updateTicketDto) {
        Ticket existingTicket = dao.findOne(id);
        if (existingTicket == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Ticket not found for id: " + id).build();
        }
           try {
            Ticket updateTicket = ticketService.update(updateTicketDto, existingTicket);
            return Response.ok(updateTicket).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update Ticket").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTicket (@PathParam("") Long id) {
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
