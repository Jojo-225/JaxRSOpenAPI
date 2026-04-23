package fr.istic.taa.jaxrs.rest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
 
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/docs")
public class SwaggerResource {

    @GET
    public Response getIndex() {
        return serveFile(FileSystems.getDefault().getPath("src/main/webapp/swagger-ui/index.html"));
    }

    @GET
    @Path("/")
    public Response getIndexWithSlash() {
        return getIndex();
    }

    @GET
    @Path("{path:.*}")
    public Response getAsset(@PathParam("path") String path) {
        return serveFile(FileSystems.getDefault().getPath("src/main/webapp/swagger-ui/" + path));
    }

    private Response serveFile(java.nio.file.Path filePath) {
        try {
            byte[] content = Files.readAllBytes(filePath);
            String mediaType = Files.probeContentType(filePath);
            return Response.ok(content, mediaType != null ? mediaType : MediaType.APPLICATION_OCTET_STREAM).build();
        } catch (IOException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
