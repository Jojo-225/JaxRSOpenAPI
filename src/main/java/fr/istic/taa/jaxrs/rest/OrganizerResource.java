package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.net.URI;
import java.util.List;     

import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Organizer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("organizer")
@Produces({"application/json"})
public class OrganizerResource {
    private final OrganizerDao dao = new OrganizerDao();
    
    @GET
    @Path("/{id}")
    public Organizer getOrganizerById(Long id) {
        Organizer o = dao.findOne(id);
        if (o == null) {
            throw new RuntimeException("Organizer not found for id: " + id);                    
        }
        return o;   
    }

    @GET
    @Path("/")
    public List<Organizer> getAllOrganizers() {
        return dao.findAll();       }       
    
    @POST
    public Response createOrganizer(Organizer organizer) {
        dao.save(organizer);
        return Response.created(URI.create("/organizer/"+organizer.getId())).entity(organizer).build();
    }
    
    @PUT
    @Path("/{id}")
    public Organizer updateOrganizer(Long id, Organizer organizer) {
        Organizer existingOrganizer = dao.findOne(id);
        if (existingOrganizer == null) {
            throw new RuntimeException("Organizer not found for id: " + id);    
    }
        organizer.setId(id);
        dao.update(organizer);
        return organizer;
    }

    @DELETE
    @Path("/{id}")
    public void deleteOrganizer(Long id) {
        Organizer existingOrganizer = dao.findOne(id);
        if (existingOrganizer == null) {
            throw new RuntimeException("Organizer not found for id: " + id);    
        }
        dao.delete(existingOrganizer);
    }
}