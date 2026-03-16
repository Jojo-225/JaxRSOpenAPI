package fr.istic.taa.jaxrs.rest;

import java.net.URI;

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


@Path("/api/admin")
@Produces({"application/json"})
public class AdminResource {
    private final AdminDao dao = new AdminDao();
    private final AdminService adminService = new AdminService();

    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    public Response getById(@PathParam("id") Long id) {
        System.out.println(">>> getById appelé avec id = " + id);
        Admin a = adminService.getById(id);
        System.out.println(">>> admin trouvé = " + a);
        if (a == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(a).build();
    }
    
    @GET
    @Path("/")
    public Response getAllAdmins() {
          try {
        var admins = dao.findAll();
        return Response.ok("nb admins = " + admins.size()).build();
        } catch (Exception e) {
        e.printStackTrace();
        return Response.serverError().entity(e.toString()).build();
        }
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