package fr.istic.taa.jaxrs.filters;

import fr.istic.taa.jaxrs.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String BEARER_PREFIX = "Bearer ";

    private RolesAllowed getRolesAllowed() {
        RolesAllowed m = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
        return (m != null) ? m : resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {

        RolesAllowed rolesAllowed = getRolesAllowed();

        // Si la route n'a pas @RolesAllowed => pas besoin de token
        if (rolesAllowed == null) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();

        try {
            JwtUtil.TokenPayload payload = JwtUtil.validateToken(token);

            boolean hasRole = Arrays.stream(rolesAllowed.value())
                    .anyMatch(payload.roles()::contains);

            if (!hasRole) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }

        } catch (JwtException e) {
            // token invalide / expiré
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}