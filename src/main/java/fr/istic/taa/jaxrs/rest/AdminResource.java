package fr.istic.taa.jaxrs.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.net.URI;
import java.util.List;      
import fr.istic.taa.jaxrs.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Response;

@Path("admin")
@Produces({"application/json"})
public class AdminResource {
    private final AdminDao dao = new AdminDao();

    @GET    
    @Path("/{id}")
    public Admin getAdminById(Long id) {
        Admin a = dao.findOne(id);
        if (a == null) {
            throw new RuntimeException("Admin not found for id: " + id);
        }
        return a;
    }
    
    @GET
    @Path("/")
    public List<Admin> getAllAdmins() {
        return dao.findAll();
    }
    
    @POST
    public Response createAdmin(Admin admin) {
        dao.save(admin);
        return Response.created(URI.create("/admin/"+admin.getId())).entity(admin).build();
    }

    @PUT
    @Path("/{id}")
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = dao.findOne(id);
        if (existingAdmin == null) {
            throw new RuntimeException("Admin not found for id: " + id);
        }
        admin.setId(id);
        dao.update(admin);
        return admin;
    }

    @DELETE
    @Path("/{id}")
    public void deleteAdmin(Long id) {         
        Admin existingAdmin = dao.findOne(id);
        if (existingAdmin == null) {
            throw new RuntimeException("Admin not found for id: " + id);
        }
        dao.delete(existingAdmin);
    }
    
}