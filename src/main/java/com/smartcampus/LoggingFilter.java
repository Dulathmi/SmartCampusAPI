/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * JAX-RS filter for logging all API requests and responses.
 * Implements both ContainerRequestFilter and ContainerResponseFilter
 * to log incoming requests and outgoing responses as a cross-cutting concern.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // Logger instance for this filter
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Logs the HTTP method and URI of every incoming request.
     * @param requestContext the request context
     * @throws IOException if an error occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info("REQUEST: " + requestContext.getMethod()
                + " " + requestContext.getUriInfo().getRequestUri());
    }

    /**
     * Logs the HTTP status code of every outgoing response.
     * @param requestContext the request context
     * @param responseContext the response context
     * @throws IOException if an error occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("RESPONSE: Status " + responseContext.getStatus());
    }
}