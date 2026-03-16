/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.istic.taa.jaxrs;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.istic.taa.jaxrs.rest.AdminResource;
import fr.istic.taa.jaxrs.rest.ArtistResource;
import fr.istic.taa.jaxrs.rest.ConcertResource;
import fr.istic.taa.jaxrs.rest.CustomerResource;
import fr.istic.taa.jaxrs.rest.OrganizerResource;
import fr.istic.taa.jaxrs.rest.TicketResource;
// import fr.istic.taa.jaxrs.JacksonConfig;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@ApplicationPath("/")
public class TestApplication extends Application {
	
    public TestApplication() {
        // Initialisation automatique au démarrage de l'app
        // if (DataInitializer.isEmpty()) {
            DataInitializer.initialize();
        // }

        // Configuration de Swagger / OpenAPI
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("Concert Management API")
                .version("1.0.0")
                .description("Documentation de l'API de gestion de concerts");

        // Base URL for API is set to /api/ to match the servlet mapping in web.xml
        oas.addServersItem(new Server().url("/api").description("Base URL for API"));

        oas.info(info);

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Stream.of("fr.istic.taa.jaxrs.rest").collect(Collectors.toSet()));

        try {
            new JaxrsOpenApiContextBuilder<>()
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Class<?>> getClasses() {

        final Set<Class<?>> clazzes = new HashSet<>();

        clazzes.add(OpenApiResource.class);
        clazzes.add(AdminResource.class);
        clazzes.add(OrganizerResource.class);
        clazzes.add(CustomerResource.class);
        clazzes.add(ConcertResource.class);
        clazzes.add(ArtistResource.class);
        clazzes.add(TicketResource.class);
        clazzes.add(JacksonConfig.class);
//        clazzes.add(AcceptHeaderOpenApiResource.class);
         

        return clazzes;
    }

}
