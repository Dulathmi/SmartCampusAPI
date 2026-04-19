/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Global catch-all exception mapper for the Smart Campus API.
 * Intercepts any unexpected runtime errors and returns a generic
 * HTTP 500 Internal Server Error response instead of exposing
 * raw Java stack traces to API consumers.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    /**
     * Maps any unhandled Throwable to a 500 Internal Server Error response.
     * @param ex the unexpected exception
     * @return HTTP 500 response with generic JSON error body
     */
    @Override
    public Response toResponse(Throwable ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred. Please try again later.");
        error.put("status", "500");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}