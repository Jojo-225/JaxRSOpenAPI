package fr.istic.taa.jaxrs.rest;

import java.net.URI;
import java.util.stream.Collectors;

import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.dto.artist.CreateArtistDto;
import fr.istic.taa.jaxrs.dto.artist.UpdateArtistDto;
import fr.istic.taa.jaxrs.service.ArtistService;
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

@Path("/artists")
@Produces({"application/json"})
@Tag(name = "Artist", description = "API for managing artists")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed({"ADMIN", "ORGANIZER", "CUSTOMER"})
public class ArtistResource {
    private final ArtistService artistService = new ArtistService();
    
    @GET    
    @Path("/{id}")
    @Operation(summary = "Get artist by ID", description = "Returns a single artist", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of artist"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    public Response getArtistById(@PathParam("id") Long id) {
        try {
            Artist artist = artistService.findOne(id);
            return Response.ok(ResponseMapper.toArtistDto(artist)).build();
        }catch(RuntimeException e)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id"+id).build();
        }
    }

    @GET
    @Path("/")
    @Operation(summary = "Get all artists", description = "Returns a list of all artists", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of artists")
    })
    public Response getAllArtists() {
        return Response.ok(artistService.findAll().stream()
                .map(ResponseMapper::toArtistDto)
                .collect(Collectors.toList())).build();
    }
    
    @POST   
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Create a new artist", description = "Creates a new artist", responses = {
            @ApiResponse(responseCode = "201", description = "Artist created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Response createArtist(CreateArtistDto createArtistDto) {
        Artist artist = artistService.createArtist(createArtistDto);
        return Response.created(URI.create("/artists/"+artist.getId()))
                .entity(ResponseMapper.toArtistDto(artist))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Update an artist", description = "Updates an existing artist", responses = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    public Response updateArtist(@PathParam("id") Long id, UpdateArtistDto updateArtistDto) {
        Artist existingArtist = artistService.findOne(id);
        if (existingArtist == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id: "+ id).build();
        }
        try{
            Artist updateArtist = artistService.update(updateArtistDto, existingArtist);
            return Response.ok(ResponseMapper.toArtistDto(updateArtist)).build();
        }catch(RuntimeException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update artist").build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "ORGANIZER"})
    @Operation(summary = "Delete an artist", description = "Deletes an existing artist", responses = {
            @ApiResponse(responseCode = "204", description = "Artist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
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
