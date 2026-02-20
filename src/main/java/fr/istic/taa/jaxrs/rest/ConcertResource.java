package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;

import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.domain.Concert;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;


@Path("/concerts")
@Produces("application/json")
public class ConcertResource {

    private final ConcertDao dao = new ConcertDao();

    // Get One concert by id
    @GET    
    @Path("/{id}")
    public Concert getConcert(Long id) {
        Concert concert= dao.findOne(id);
        if (concert == null) {
            throw new RuntimeException("Concert not found for id: " + id);
        }
        return concert;

    }
       // Get all 
    @GET    
    public List<Concert> getConcerts() {
        return dao.findAll();
    }

    

    
    @POST
    public Response createConcert(Concert concert) {
        dao.save(concert);
        return Response.created(URI.create("/concerts/"+ concert.getId()))
        .entity(concert).build();
    }
    

    
    @PUT
    @Path("{id}")
    public Concert updateConcert(Long id, Concert concert) {
        Concert existingConcert = dao.findOne(id);
        if (existingConcert == null) {
            throw new RuntimeException("Concert not found for id: " + id);
        }
        concert.setId(id);
        dao.update(concert);
        return concert;
    }

    @DELETE
    @Path("{id}")
    public Response deleteConcert(Long id) {
        Concert existingConcert = dao.findOne(id);
        if (existingConcert == null) {
            throw new RuntimeException("Concert not found for id: " + id); 
        }
        dao.delete(existingConcert);
        return Response.noContent().build();
    }

    
    
}
