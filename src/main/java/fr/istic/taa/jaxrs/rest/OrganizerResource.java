package fr.istic.taa.jaxrs.rest;

import java.net.URI;

import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Organizer;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import fr.istic.taa.jaxrs.dto.CreateUserDto;
import fr.istic.taa.jaxrs.service.OrganizerService;
import fr.istic.taa.jaxrs.service.UserService;
import fr.istic.taa.jaxrs.dto.UpdateUserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/api/organizer")
@Produces({"application/json"})
@Tag(name = "Organizer", description = "API for managing organizers")
public class OrganizerResource {
    private final OrganizerDao dao = new OrganizerDao();
    private final OrganizerService organizerService = new OrganizerService();
    private final UserService userService = new UserService();
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get organizer by ID", description = "Returns a single organizer", responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval of organizer"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public Response getOrganizerById(@PathParam("id") Long id) {
        try{
            Organizer organizer = organizerService.findOne(id);
            return Response.ok(organizer).build();
            // throw new RuntimeException("Organizer not found for id: " + id);                    
        }catch (RuntimeException e) {            
            return Response.status(Response.Status.NOT_FOUND).entity("Organizer not found for id: " + id).build();
        }
    }

    @GET
    @Path("/")
    @Operation(summary = "Get all organizers", description = "Returns a list of all organizers", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of organizers")
    })
    public Response getAllOrganizers() {
        return Response.ok(organizerService.findAll()).build();
    }

    @POST
    @Path("/")
    @Operation(summary = "Create a new organizer", description = "Creates a new organizer", responses = {
        @ApiResponse(responseCode = "201", description = "Organizer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    public Response createOrganizer(CreateUserDto createOrganizerDto) {
        if (userService.findByEmail(createOrganizerDto.getEmail()) != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An organizer with this email already exists").build();
        }
        try {
            Organizer organizer = organizerService.createOrganizer(createOrganizerDto);
            return Response.created(URI.create("/organizer/"+organizer.getId())).entity(organizer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create organizer").build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing organizer", description = "Updates an existing organizer", responses = {
        @ApiResponse(responseCode = "200", description = "Organizer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public Response updateOrganizer(@PathParam("id") Long id, UpdateUserDto updateOrganizerDto) {
        Organizer existingOrganizer = organizerService.findOne(id);
        if (existingOrganizer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organizer not found for id: " + id).build();
        }
        try {
            Organizer updatedOrganizer = organizerService.update(updateOrganizerDto, existingOrganizer);
            return Response.ok(updatedOrganizer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update organizer").build();
        }
    }

    @PUT
    @Path("/{id}/updateEmail")
    @Operation(summary = "Update an existing organizer's email", description = "Updates an existing organizer's email", responses = {
        @ApiResponse(responseCode = "200", description = "Organizer email updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public Response updateOrganizerEmail(@PathParam("id") Long id, String newEmail) {
        Organizer existingOrganizer = organizerService.findOne(id);
        if (existingOrganizer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organizer not found for id: " + id).build();
        }
        try {
            Organizer updatedOrganizer = organizerService.updateEmail(newEmail, existingOrganizer);
            return Response.ok(updatedOrganizer).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /*
    * Note: Deletion of an organizer is not allowed if there are concerts associated with it.
    * This is to maintain data integrity and prevent orphaned concert records.
    * Before deleting an organizer, we check if there are any concerts linked to it. If there are, we return a BAD_REQUEST response indicating that the organizer cannot be deleted until all associated concerts are removed or reassigned.
    * This approach ensures that we do not end up with concerts that reference a non-existent organizer, which could lead to data inconsistencies and errors in the application.
    * If there are no concerts associated with the organizer, we proceed with the deletion and return a NO_CONTENT response to indicate that the organizer was successfully deleted.
    * This logic is implemented in the deleteOrganizer method, where we first check for the existence of the organizer and then verify if there are any associated concerts before performing the deletion.
    * If the organizer is not found, we return a NOT_FOUND response. If there are associated concerts, we return a BAD_REQUEST response. If the deletion is successful, we return a NO_CONTENT response with a success message.
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an organizer", description = "Deletes an organizer if there are no associated concerts", responses = {
        @ApiResponse(responseCode = "204", description = "Organizer deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete organizer with associated concerts"),
        @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public Response deleteOrganizer(@PathParam("id") Long id) {
        Organizer existingOrganizer = organizerService.findOne(id);
        if (existingOrganizer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
            // throw new RuntimeException("Organizer not found for id: " + id);    
        }
        if (!dao.findConcertsByOrganizerId(id).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot delete organizer with associated concerts. Please remove or reassign the concerts first.").build();
        }
        try {
            organizerService.delete(existingOrganizer);
            return Response.noContent().entity("Organizer deleted successfully").build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to delete organizer").build();
        }
    }
}