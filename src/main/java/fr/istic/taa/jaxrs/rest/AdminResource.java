package fr.istic.taa.jaxrs.rest;

import java.net.URI;
import java.util.List;      

import fr.istic.taa.jaxrs.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.service.AdminService;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("admin")
@Produces({"application/json"})
public class AdminResource {
    private final AdminDao dao = new AdminDao();
    private final AdminService adminService = new AdminService();

    @GET    
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Admin a = adminService.getById(id);
        if (a == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(a).build();
    }
    
    @GET
    @Path("/")
    public List<Admin> getAllAdmins() {
        return dao.findAll();
    }
    
    @POST
    @Path("/create")
    public Response createAdmin(Admin admin) {
        adminService.create(admin);
        return Response.created(URI.create("/admin/"+admin.getId())).entity(admin).build();
    }

    @PUT
    @Path("/{id}")
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminService.getById(id);
        if (existingAdmin == null) {
            throw new RuntimeException("Admin not found for id: " + id);
        }
        admin.setId(id);
        adminService.update(admin);
        return admin;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAdmin(Long id) {         
        Admin existingAdmin = adminService.getById(id);
        if (existingAdmin == null) {
            throw new RuntimeException("Admin not found for id: " + id);
        }
        adminService.delete(existingAdmin);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
}