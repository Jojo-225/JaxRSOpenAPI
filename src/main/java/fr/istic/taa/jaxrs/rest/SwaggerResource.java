package fr.istic.taa.jaxrs.rest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/docs")
// @Produces(MediaType.TEXT_HTML)
public class SwaggerResource {

    private static final Logger logger = Logger.getLogger(SwaggerResource.class.getName());

    // @GET
    // @Path("/")
    // public byte[] Get1() {

    //     try {
    //         return Files.readAllBytes(FileSystems.getDefault().getPath("src/main/webapp/swagger-ui/index.html"));
    //     } catch (IOException e) {
    //         return null;
    //     }
    // }

    // @GET
    // @Path("{path:.*}")
    // public byte[] Get(@PathParam("path") String path) {
    //     try {
    //         return Files.readAllBytes(FileSystems.getDefault().getPath("src/main/webapp/swagger/"+path));
    //     } catch (IOException e) {
    //         return null;
    //     }
    // }

    private static final String SWAGGER_HTML = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>API Documentation</title>
              <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5/swagger-ui.css">
            </head>
            <body>
              <div id="swagger-ui"></div>
              <script src="https://unpkg.com/swagger-ui-dist@5/swagger-ui-bundle.js"></script>
              <script>
                window.ui = SwaggerUIBundle({
                  url: '/openapi.json',
                  dom_id: '#swagger-ui',
                  deepLinking: true,
                  presets: [SwaggerUIBundle.presets.apis],
                  layout: 'BaseLayout'
                });
              </script>
            </body>
            </html>
            """;

    /**
     * Returns the Swagger UI HTML page.
     *
     * @return HTTP 200 with Swagger UI page
     */
    @GET
    @Path("")
    @Produces(MediaType.TEXT_HTML)
    public Response docs() {
        return Response.ok(SWAGGER_HTML).build();
    }

}