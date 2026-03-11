package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;

import fr.istic.taa.jaxrs.dao.TicketDao;
import fr.istic.taa.jaxrs.domain.Ticket;
import jakarta.ws.rs.core.Response; 
import jakarta.ws.rs.PathParam;

@Path("/tickets")
@Produces({"application/json"})
public class TicketResource {
    private final TicketDao dao = new TicketDao(); 
    
    //Read One
    @GET
    @Path("/{id}")
    public  Ticket getTickets(@PathParam("id") Long id) {
        Ticket t= dao.findOne(id);   
        if (t == null) {
            throw new RuntimeException("Ticket not found for id: " + id);
        }
        return t;
    }

    //Read All
    @GET
    @Path("/all/{id}")
    public List<Ticket> getAllTicket() {
        return dao.findAll(); 
    }

    //Create
    @POST
    public Response createTicket(Ticket ticket) {
         dao.save(ticket);
         return Response.created(URI.create("/tickets/"+ticket.getId())).entity(ticket).build();
    }

    @PUT
    @Path("/{id}")
    public Ticket updateTicket(Long id, String ticket) {
        Ticket existingTicket = dao.findOne(id);
        if (existingTicket == null) {
            throw new RuntimeException("Ticket not found for id: " + id);
        }
        existingTicket.setId(id);
        dao.update(existingTicket);
        return existingTicket;
    }

    @DELETE
    @Path("/{id}")
    public void deleteTicket(Long id) {
        Ticket existingTicket = dao.findOne(id);
        if (existingTicket == null) {
            throw new RuntimeException("Ticket not found for id: " + id);
        }
        dao.delete(existingTicket);
    }
    
    
}
