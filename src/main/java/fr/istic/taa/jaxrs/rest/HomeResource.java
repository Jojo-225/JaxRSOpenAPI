package fr.istic.taa.jaxrs.rest;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.service.ConcertService;
import fr.istic.taa.jaxrs.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import java.util.stream.Collectors;
import jakarta.ws.rs.PathParam;
import java.util.Map;

import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;

@Path("/api/home")
@Produces("application/json")
@Tag(name = "Home", description = "Home page endpoints")
public class HomeResource {

    private final ConcertService concertService = new ConcertService();
    private final TicketService ticketService = new TicketService();

    @GET    
    @Path("/latestConcerts")
    @Operation(summary = "Get latest concerts", description = "Returns a list of the latest concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getLatestConcerts() {
        return Response.ok(concertService.getLatestConcerts().stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList())).build();
    }

    @GET    
    @Path("/incomingConcerts")
    @Operation(summary = "Get incoming concerts", description = "Returns a list of the incoming concerts", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getIncomingConcerts() {
        return Response.ok(concertService.getIncomingConcerts().stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList())).build();
    }

    @GET    
    @Path("/search")
    @Operation(summary = "Search concerts", description = "Returns a list of concerts matching the given criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getConcertsByCriteria(String topic, String date, String description, String artistName, String organizerName) {
        return Response.ok(concertService.findConcertsByCriteria(topic, date, description, artistName, organizerName).stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get concert by ID", description = "Returns a specific concert by its ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of concert"),
        @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getConcertById(@PathParam("id") Long id) {
        Concert concert = concertService.findOne(id);
        if (concert == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Concert not found")).build();
        }
        return Response.ok(ResponseMapper.toConcertDto(concert)).build();
    }

    @GET    
    @Path("/mytickets")
    @Operation(summary = "Get my tickets", description = "Returns a list of the concerts for which the user has tickets", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of tickets"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response getMyTickets() {
        return Response.ok(ticketService.getMyTickets().stream()
            .map(ResponseMapper::toTicketDto)
            .collect(Collectors.toList())).build();
    }
    

}
