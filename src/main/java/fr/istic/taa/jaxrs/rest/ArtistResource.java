package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.net.URI;

import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.service.ArtistService;
import fr.istic.taa.jaxrs.dto.CreateArtistDto;
import fr.istic.taa.jaxrs.dto.UpdateArtistDto;
import jakarta.ws.rs.POST;  
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("artists")
@Produces({"application/json"})
public class ArtistResource {
    private final ArtistService artistService = new ArtistService();
    
    @GET    
    @Path("/{id}")
    public Response getArtistById(@PathParam("id") Long id) {
       try {
        Artist artist = artistService.findOne(id);
        return Response.ok(artist).build();
       }catch(RuntimeException e)
       {
        return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id"+id).build();
        }
    }

    @GET
    @Path("/")
    public Response getAllArtists() {
        return Response.ok(artistService.findAll()).build();
    }
    
    @POST   
    public Response createArtist(CreateArtistDto createArtistDto) {
        Artist artist = artistService.createArtist(createArtistDto);
        return Response.created(URI.create("/artists/"+artist.getId())).entity(artist).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateArtist(@PathParam("id") Long id, UpdateArtistDto updateArtistDto) {
        Artist existingArtist = artistService.findOne(id);
        if (existingArtist == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id: "+ id).build();
        }
        try{
            Artist updateArtist = artistService.update(updateArtistDto, existingArtist);
            return Response.ok(updateArtist).build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update artist").build();

        }
    
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteArtist(@PathParam("id") Long id) {

        Artist existingArtist = artistService.findOne(id);
        if (existingArtist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try{
            artistService.delete(existingArtist);
            return Response.noContent().entity("Artist deleted successfully").build();
        }catch(RuntimeException e){
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete artist").build();

        }
    }
}
