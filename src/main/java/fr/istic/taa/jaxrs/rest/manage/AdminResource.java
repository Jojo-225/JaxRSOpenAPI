package fr.istic.taa.jaxrs.rest.manage;

import java.net.URI;
import java.util.stream.Collectors;

import fr.istic.taa.jaxrs.dao.AdminDao;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.user.CreateUserDto;
import fr.istic.taa.jaxrs.dto.user.UpdateUserDto;
import fr.istic.taa.jaxrs.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/manage/admin")
@Produces({"application/json"})
@Tag(name = "Admin", description = "API for managing administrators")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed("ADMIN")
public class AdminResource {
    private final AdminDao dao = new AdminDao();
    private final AdminService adminService = new AdminService();

    @GET
    @Path("/{id}")
    @Operation(summary = "Get admin by ID", description = "Returns a single admin", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of admin"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public Response getById(@PathParam("id") Long id) {
        Admin admin = adminService.getById(id);
        if (admin == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ResponseMapper.toUserDto(admin)).build();
    }

    @GET
    @Path("/")
    @Operation(summary = "Get all admins", description = "Returns a list of all admins", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of admins")
    })
    public Response getAllAdmins() {
        try {
            var admins = dao.findAll();
            return Response.ok(admins.stream()
                    .map(ResponseMapper::toUserDto)
                    .collect(Collectors.toList())).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }
    }

    @POST
    @Path("/create")
    @Operation(summary = "Create a new admin", description = "Creates a new admin", responses = {
            @ApiResponse(responseCode = "201", description = "Admin created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Response createAdmin(CreateUserDto dto) {
        Admin admin = new Admin();
        admin.setLastName(dto.getLastname());
        admin.setFirstName(dto.getFirstname());
        admin.setDateOfBirth(dto.getBirthdate());
        admin.setMail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        adminService.create(admin);
        return Response.created(URI.create("/admin/" + admin.getId()))
                .entity(ResponseMapper.toUserDto(admin))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an admin", description = "Updates an existing admin", responses = {
            @ApiResponse(responseCode = "200", description = "Admin updated successfully"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public Response updateAdmin(@PathParam("id") Long id, UpdateUserDto dto) {
        Admin existingAdmin = adminService.getById(id);
        if (existingAdmin == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Admin not found for id: " + id)
                    .build();
        }
        existingAdmin.setLastName(dto.getLastname());
        existingAdmin.setFirstName(dto.getFirstname());
        existingAdmin.setDateOfBirth(dto.getBirthdate());
        existingAdmin.setPassword(dto.getPassword());
        adminService.update(existingAdmin);
        return Response.ok(ResponseMapper.toUserDto(existingAdmin)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an admin", description = "Deletes an existing admin", responses = {
            @ApiResponse(responseCode = "204", description = "Admin deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public Response deleteAdmin(@PathParam("id") Long id) {
        Admin existingAdmin = adminService.getById(id);
        if (existingAdmin == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Admin not found for id: " + id)
                    .build();
        }

        adminService.delete(existingAdmin);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
