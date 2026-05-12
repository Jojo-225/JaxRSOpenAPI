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

import fr.istic.taa.jaxrs.filters.JWTAuthFilter;
import fr.istic.taa.jaxrs.filters.CorsFilter;
import fr.istic.taa.jaxrs.rest.AuthResource;
import fr.istic.taa.jaxrs.rest.ConcertAlertResource;
import fr.istic.taa.jaxrs.rest.CustomerActionResource;
import fr.istic.taa.jaxrs.rest.HomeResource;
import fr.istic.taa.jaxrs.rest.NotificationResource;
import fr.istic.taa.jaxrs.rest.SwaggerResource;
import fr.istic.taa.jaxrs.rest.organizer.ArtistResource;
import fr.istic.taa.jaxrs.rest.organizer.ConcertResource;
import fr.istic.taa.jaxrs.rest.organizer.DashboardResource;
import fr.istic.taa.jaxrs.rest.organizer.TicketResource;
import fr.istic.taa.jaxrs.rest.manage.AdminResource;
import fr.istic.taa.jaxrs.rest.manage.OrganizerResource;
import fr.istic.taa.jaxrs.service.ConcertAlertScheduler;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class TestApplication extends Application {
	
    public TestApplication() {
        // Initialisation automatique au démarrage de l'app
        DataInitializer.initializeIfEmpty();
        ConcertAlertScheduler.getInstance().start();

        // Configuration de Swagger / OpenAPI
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("Concert Management API")
                .version("1.0.0")
                .description("Documentation de l'API de gestion de concerts");

        // Base URL for API is set to / to match the servlet mapping in web.xml
        oas.addServersItem(new Server().url("/").description("Base URL for API"));

        oas.components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));

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
        clazzes.add(HomeResource.class);
        clazzes.add(CustomerActionResource.class);
        clazzes.add(ConcertAlertResource.class);
        clazzes.add(NotificationResource.class);
        clazzes.add(ConcertResource.class);
        clazzes.add(DashboardResource.class);
        clazzes.add(ArtistResource.class);
        clazzes.add(TicketResource.class);
        clazzes.add(JacksonConfig.class);
        clazzes.add(SwaggerResource.class);
        clazzes.add(AuthResource.class);
        clazzes.add(JWTAuthFilter.class);
        clazzes.add(CorsFilter.class);
//        clazzes.add(AcceptHeaderOpenApiResource.class);
         

        return clazzes;
    }

}
