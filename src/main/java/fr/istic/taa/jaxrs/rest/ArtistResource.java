package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;      

import fr.istic.taa.jaxrs.dao.ArtistDao;
import fr.istic.taa.jaxrs.domain.Artist;
import jakarta.ws.rs.POST;  
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("artists")
@Produces({"application/json"})
public class ArtistResource {
    private final ArtistDao dao = new ArtistDao();
    
    @GET    
    @Path("/{id}")
    public Artist getArtistById(Long id) {
        Artist a = dao.findOne(id);
        if (a == null) {
            throw new RuntimeException("Artist not found for id: " + id);   
        }
        return a;
    }

    @GET
    @Path("/")
    public List<Artist> getAllArtists() {
        return dao.findAll();       
    }
    
    @POST   
    public Response createArtist(Artist artist) {
        dao.save(artist);
        return Response.created(URI.create("/artists/"+artist.getId())).entity(artist).build();
    }

    @PUT
    @Path("/{id}")
    public Artist updateArtist(Long id, Artist artist) {
        Artist existingArtist = dao.findOne(id);
        if (existingArtist == null) {
            throw new RuntimeException("Artist not found for id: " + id);       
        }   
        artist.setId(id);
        dao.update(artist);
        return artist;
    }
    
    @DELETE
    @Path("/{id}")
    public void deleteArtist(Long id) {
        Artist existingArtist = dao.findOne(id);
        if (existingArtist == null) {
            throw new RuntimeException("Artist not found for id: " + id);   
        }
        dao.delete(existingArtist); 
    }
}
