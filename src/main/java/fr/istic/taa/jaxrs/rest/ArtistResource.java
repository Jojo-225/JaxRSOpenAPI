package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;      

import fr.istic.taa.jaxrs.dao.ArtistDao;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.service.ArtistService;
import jakarta.ws.rs.POST;  
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("artists")
@Produces({"application/json"})
public class ArtistResource {
    private final ArtistDao dao = new ArtistDao();
    private final ArtistService artistService = new ArtistService();
    
    @GET    
    @Path("/{id}")
    public Response getArtistById(Long id) {
        Artist a = artistService.getById(id);
        if (a == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(a).build();
    }

    @GET
    @Path("/")
    public Response getAllArtists() {
        return Response.status(Response.Status.OK).entity(artistService.getAllArtists()).build();
    }
    
    @POST   
    public Response createArtist(Artist artist) {
        artistService.create(artist);
        return Response.created(URI.create("/artists/"+artist.getId())).entity(artist).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateArtist(Long id, Artist artist) {
        Artist existingArtist = artistService.getById(id);
        if (existingArtist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        artist.setId(id);
        artistService.update(artist);
        return Response.status(Response.Status.OK).entity(artist).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteArtist(Long id) {
        Artist existingArtist = artistService.getById(id);
        if (existingArtist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        artistService.delete(existingArtist);
        return Response.status(Response.Status.OK).build();
    }
}
