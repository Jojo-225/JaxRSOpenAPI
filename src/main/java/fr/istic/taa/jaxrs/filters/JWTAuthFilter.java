package fr.istic.taa.jaxrs.filters;

import fr.istic.taa.jaxrs.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String BEARER_PREFIX = "Bearer ";

    private boolean isPermitAll() {
        return resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)
                || resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class);
    }

    private boolean isDenyAll() {
        return resourceInfo.getResourceMethod().isAnnotationPresent(DenyAll.class)
                || resourceInfo.getResourceClass().isAnnotationPresent(DenyAll.class);
    }

    private RolesAllowed getRolesAllowed() {
        RolesAllowed m = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
        return (m != null) ? m : resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo == null || resourceInfo.getResourceMethod() == null) {
            return;
        }

        if (isPermitAll()) {
            return;
        }

        if (isDenyAll()) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        RolesAllowed rolesAllowed = getRolesAllowed();

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
                return;
            }

            SecurityContext originalContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return payload;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return payload.roles().contains(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContext != null && originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

        } catch (JwtException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
