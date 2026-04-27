package fr.istic.taa.jaxrs.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String ALLOW_ORIGIN = "*";
    private static final String ALLOW_METHODS = "GET, POST, PUT, DELETE, OPTIONS, PATCH";
    private static final String ALLOW_HEADERS = "Origin, Content-Type, Accept, Authorization";
    private static final String MAX_AGE = "3600";
    private static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String HEADER_MAX_AGE = "Access-Control-Max-Age";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            requestContext.abortWith(Response.ok()
                    .header(HEADER_ALLOW_ORIGIN, ALLOW_ORIGIN)
                    .header(HEADER_ALLOW_METHODS, ALLOW_METHODS)
                    .header(HEADER_ALLOW_HEADERS, ALLOW_HEADERS)
                    .header(HEADER_MAX_AGE, MAX_AGE)
                    .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().putSingle(HEADER_ALLOW_ORIGIN, ALLOW_ORIGIN);
        responseContext.getHeaders().putSingle(HEADER_ALLOW_METHODS, ALLOW_METHODS);
        responseContext.getHeaders().putSingle(HEADER_ALLOW_HEADERS, ALLOW_HEADERS);
        responseContext.getHeaders().putSingle(HEADER_MAX_AGE, MAX_AGE);
    }
}
