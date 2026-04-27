package fr.istic.taa.jaxrs.rest.organizer;

import fr.istic.taa.jaxrs.dao.ArtistDao;
import fr.istic.taa.jaxrs.dao.ConcertDao;
import fr.istic.taa.jaxrs.dao.OrganizerDao;
import fr.istic.taa.jaxrs.domain.Artist;
import fr.istic.taa.jaxrs.domain.Concert;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.artist.CreateArtistDto;
import fr.istic.taa.jaxrs.dto.artist.UpdateArtistDto;
import fr.istic.taa.jaxrs.dto.mapper.ResponseMapper;
import fr.istic.taa.jaxrs.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.net.URI;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.stream.Collectors;

@Path("/organise/artists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Organizer - Artists", description = "Organizer back-office for artists linked to organizer concerts")
@SecurityRequirement(name = "bearerAuth")
@RolesAllowed("ORGANIZER")
public class ArtistResource {

    private final ArtistDao artistDao = new ArtistDao();
    private final ConcertDao concertDao = new ConcertDao();
    private final OrganizerDao organizerDao = new OrganizerDao();
    private final CurrentUserService currentUserService = new CurrentUserService();

    @GET
    @Path("/")
    @Operation(summary = "Get my artists", description = "Returns distinct artists linked to organizer concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Artists returned")
    })
    public Response getMyArtists(@Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Map<Long, Artist> byId = new LinkedHashMap<>();
        for (Concert concert : organizerDao.findConcertsByOrganizerId(organizer.getId())) {
            for (Artist artist : concert.getArtists()) {
                byId.put(artist.getId(), artist);
            }
        }

        return Response.ok(byId.values().stream()
                .map(ResponseMapper::toArtistDto)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one artist", description = "Returns an artist if linked to organizer concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Artist returned"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    public Response getArtistById(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Artist artist = artistDao.findOne(id);
        if (artist == null || !isArtistOwnedByOrganizer(artist, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id: " + id).build();
        }

        return Response.ok(ResponseMapper.toArtistDto(artist)).build();
    }

    @GET
    @Path("/concert/{concertId}")
    @Operation(summary = "Get artists by concert", description = "Returns artists linked to one organizer concert", responses = {
            @ApiResponse(responseCode = "200", description = "Artists returned"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response getArtistsByConcert(@PathParam("concertId") Long concertId,
                                        @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        if (concert == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + concertId).build();
        }

        return Response.ok(concert.getArtists().stream()
                .map(ResponseMapper::toArtistDto)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Path("/concert/{concertId}")
    @Operation(summary = "Create artist for concert", description = "Creates a new artist and links it to one organizer concert", responses = {
            @ApiResponse(responseCode = "201", description = "Artist created and linked"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    public Response createArtistForConcert(@PathParam("concertId") Long concertId,
                                           CreateArtistDto dto,
                                           @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        if (concert == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Concert not found for id: " + concertId).build();
        }

        if (dto == null || dto.getName() == null || dto.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("artist name is required").build();
        }

        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.addConcert(concert);
        artistDao.save(artist);
        concert.addArtist(artist);
        concertDao.update(concert);

        return Response.created(URI.create("/organise/artists/" + artist.getId()))
                .entity(ResponseMapper.toArtistDto(artist))
                .build();
    }

    @POST
    @Path("/{artistId}/concert/{concertId}")
    @Operation(summary = "Link existing artist to concert", description = "Links an existing artist to one organizer concert", responses = {
            @ApiResponse(responseCode = "200", description = "Artist linked"),
            @ApiResponse(responseCode = "404", description = "Artist or concert not found")
    })
    public Response linkArtistToConcert(@PathParam("artistId") Long artistId,
                                        @PathParam("concertId") Long concertId,
                                        @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        Artist artist = artistDao.findOne(artistId);
        if (concert == null || artist == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean alreadyLinked = artist.getConcerts().stream().anyMatch(c -> c.getId().equals(concertId));
        if (!alreadyLinked) {
            artist.addConcert(concert);
            artist = artistDao.update(artist);
        }

        return Response.ok(ResponseMapper.toArtistDto(artist)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update artist", description = "Updates an artist linked to organizer concerts", responses = {
            @ApiResponse(responseCode = "200", description = "Artist updated"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    public Response updateArtist(@PathParam("id") Long id,
                                 UpdateArtistDto dto,
                                 @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Artist artist = artistDao.findOne(id);
        if (artist == null || !isArtistOwnedByOrganizer(artist, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Artist not found for id: " + id).build();
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            artist.setName(dto.getName());
        }

        Artist updated = artistDao.update(artist);
        return Response.ok(ResponseMapper.toArtistDto(updated)).build();
    }

    @DELETE
    @Path("/{artistId}/concert/{concertId}")
    @Operation(summary = "Unlink artist from concert", description = "Unlinks an artist from one organizer concert", responses = {
            @ApiResponse(responseCode = "204", description = "Artist unlinked"),
            @ApiResponse(responseCode = "404", description = "Artist or concert not found")
    })
    public Response unlinkArtistFromConcert(@PathParam("artistId") Long artistId,
                                            @PathParam("concertId") Long concertId,
                                            @Context SecurityContext securityContext) {
        Organizer organizer = getAuthenticatedOrganizer(securityContext);
        if (organizer == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Concert concert = concertDao.findOne(concertId);
        Artist artist = artistDao.findOne(artistId);
        if (concert == null || artist == null || !isConcertOwnedByOrganizer(concert, organizer.getId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        artist.removeConcert(concert);
        artistDao.update(artist);
        return Response.noContent().build();
    }

    private Organizer getAuthenticatedOrganizer(SecurityContext securityContext) {
        User currentUser = currentUserService.getCurrentUser(securityContext);
        if (!(currentUser instanceof Organizer)) {
            return null;
        }
        return organizerDao.findOne(currentUser.getId());
    }

    private boolean isConcertOwnedByOrganizer(Concert concert, Long organizerId) {
        return concert.getOrganizer() != null
                && concert.getOrganizer().getId() != null
                && concert.getOrganizer().getId().equals(organizerId);
    }

    private boolean isArtistOwnedByOrganizer(Artist artist, Long organizerId) {
        return artist.getConcerts().stream().anyMatch(c -> isConcertOwnedByOrganizer(c, organizerId));
    }
}
