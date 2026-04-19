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
 * Exception mapper for SensorUnavailableException.
 * Returns HTTP 403 Forbidden when a POST reading is attempted
 * on a sensor that is currently under MAINTENANCE.
 */
@Provider
public class SensorUnavailableExceptionMapper
        implements ExceptionMapper<SensorUnavailableException> {

    /**
     * Maps SensorUnavailableException to a 403 Forbidden HTTP response.
     * @param ex the exception thrown
     * @return HTTP 403 response with JSON error body
     */
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Sensor Unavailable");
        error.put("message", ex.getMessage());
        error.put("status", "403");
        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}