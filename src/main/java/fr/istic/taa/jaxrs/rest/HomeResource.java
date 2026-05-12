package fr.istic.taa.jaxrs.rest;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Ticket;
import fr.istic.taa.jaxrs.service.ConcertService;
import fr.istic.taa.jaxrs.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.stream.Collectors;
import jakarta.ws.rs.PathParam;
import java.util.Map;

import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/api")
@Produces("application/json")
@Tag(name = "Home guess", description = "Home page endpoints")
public class HomeResource {

    private final ConcertService concertService = new ConcertService();
    private final TicketService ticketService = new TicketService();

    @GET    
    @Path("/latestConcerts")
    @Operation(summary = "Get latest concerts", description = "Returns a list of the latest concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getLatestConcerts() {
        return withCors(Response.ok(concertService.getLatestConcerts().stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList()))).build();
    }

    @GET    
    @Path("/incomingConcerts")
    @Operation(summary = "Get incoming concerts", description = "Returns a list of the incoming concerts", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getIncomingConcerts() {
        return withCors(Response.ok(concertService.getIncomingConcerts().stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList()))).build();
    }

    @GET    
    @Path("/search")
    @Operation(summary = "Search concerts", description = "Returns a list of concerts matching the given criteria", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of concerts")
    })
    public Response getConcertsByCriteria(@QueryParam("topic") String topic, @QueryParam("date") String date, @QueryParam("description") String description, @QueryParam("artistName") String artistName, @QueryParam("organizerName") String organizerName, @QueryParam("priceMin") Double priceMin, @QueryParam("priceMax") Double priceMax) {
        return withCors(Response.ok(concertService.findConcertsByCriteria(topic, date, description, artistName, organizerName, priceMin, priceMax).stream()
            .map(ResponseMapper::toConcertDto)
            .collect(Collectors.toList()))).build();
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
            return withCors(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Concert not found"))).build();
        }
        return withCors(Response.ok(ResponseMapper.toConcertDto(concert))).build();
    }

    @GET
    @Path("/tickets/{id}")
    @Operation(summary = "Get ticket by ID", description = "Returns a specific ticket by its ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of ticket"),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public Response getTicketById(@PathParam("id") Long id) {
        Ticket ticket = ticketService.findOne(id);
        if (ticket == null) {
            return withCors(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Ticket not found"))).build();
        }
        return withCors(Response.ok(ResponseMapper.toTicketDto(ticket))).build();
    }

    @GET    
    @Path("/mytickets")
    @Operation(summary = "Get my tickets", description = "Returns a list of the concerts for which the user has tickets", responses = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of tickets"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response getMyTickets() {
        return withCors(Response.ok(ticketService.getMyTickets().stream()
            .map(ResponseMapper::toTicketDto)
            .collect(Collectors.toList()))).build();
    }

    private ResponseBuilder withCors(ResponseBuilder builder) {
        return builder
                .header("Access-Control-Allow-Origin", "http://localhost:4200")
                .header("Vary", "Origin")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
    }
    
}
