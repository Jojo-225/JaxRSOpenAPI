package fr.istic.taa.jaxrs.rest;

import java.net.URI;
import java.util.stream.Collectors;

import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.dto.concert.CreateConcertDto;
import fr.istic.taa.jaxrs.dto.concert.UpdateConcertDto;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.service.ConcertService;
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


@Path("/concerts")
@Produces("application/json")
@Tag(name = "Concert", description = "API for managing concerts")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed({"ADMIN", "ORGANIZER", "CUSTOMER"})
public class ConcertResource {

    private final ConcertService concertService = new ConcertService();

    // Get One concert by id
    @GET    
    @Path("/{id}")
    @Operation(summary = "Get concert by ID", description = "Returns a single concert", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concert"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getConcertById(@PathParam("id") Long id) {
       try{
         Concert concert = concertService.findOne(id);
         return Response.ok(ResponseMapper.toConcertDto(concert)).build();
        
        }catch(RuntimeException e ){
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
         }

    }

    @GET    
    @Path("/")
    @Operation(summary = "Get all concerts", description = "Returns a list of all concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getAllConcerts() {
        return Response.ok(concertService.findAll().stream()
                .map(ResponseMapper::toConcertDto)
                .collect(Collectors.toList())).build();
    }
 
    @POST
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Create a new concert", description = "Creates a new concert", responses = {
            @ApiResponse(responseCode = "201", description = "Concert created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Response createConcert(CreateConcertDto createConcertDto) {
        Concert concert= concertService.createConcert(createConcertDto);
        return Response.created(URI.create("/concerts/"+ concert.getId()))
        .entity(ResponseMapper.toConcertDto(concert)).build();
    }
    

    
    @PUT
    @Path("{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Update a concert", description = "Updates an existing concert", responses = {
            @ApiResponse(responseCode = "200", description = "Concert updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response updateConcert(@PathParam("id") Long id, UpdateConcertDto updateConcertDto) {
        Concert existingConcert = concertService.findOne(id);
        if (existingConcert == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + id).build();
        }try{
            Concert updateConcert= concertService.update(updateConcertDto, existingConcert);
            return Response.ok(ResponseMapper.toConcertDto(updateConcert)).build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update concert").build();
        }
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Delete a concert", description = "Deletes an existing concert", responses = {
            @ApiResponse(responseCode = "204", description = "Concert deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response deleteConcert(@PathParam("id") Long id) {
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
