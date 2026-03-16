package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;

import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.dto.CreateConcertDto;
import fr.istic.taa.jaxrs.dto.UpdateConcertDto;
import fr.istic.taa.jaxrs.service.ConcertService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;


@Path("concerts")
@Produces("application/json")
public class ConcertResource {

    private final ConcertService concertService = new ConcertService();

    // Get One concert by id
    @GET    
    @Path("/{id}")
    public Response getConcertById(@PathParam("id") Long id) {
       try{
         Concert concert = concertService.findOne(id);
         return Response.ok(concert).build();
        
        }catch(RuntimeException e ){
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
         }

    }

    @GET    
    @Path("/")
    public Response getAllConcerts() {
        return Response.ok(concertService.findAll()).build();
    }
 
    @POST
    public Response createConcert(CreateConcertDto createConcertDto) {
        Concert concert= concertService.createConcert(createConcertDto);
        return Response.created(URI.create("/concerts/"+ concert.getId()))
        .entity(concert).build();
    }
    

    
    @PUT
    @Path("{id}")
    public Response updateConcert(@PathParam("id") Long id, UpdateConcertDto updateConcertDto) {
        Concert existingConcert = concertService.findOne(id);
        if (existingConcert == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
        }try{
            Concert updateConcert= concertService.update(updateConcertDto, existingConcert);
            return Response.ok(updateConcert).build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update concert").build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteConcert(@PathParam("") Long id) {
        Concert existingConcert = concertService.findOne(id);
        if (existingConcert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try{
            concertService.delete(existingConcert);
            return Response.noContent().entity("Ticket deleted successfully").build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete concert").build();
        }
       
    }

    
    
}
