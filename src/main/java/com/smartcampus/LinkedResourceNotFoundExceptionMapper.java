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
 * Exception mapper for LinkedResourceNotFoundException.
 * Returns HTTP 422 Unprocessable Entity when a sensor is created
 * with a roomId that does not exist in the system.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    /**
     * Maps LinkedResourceNotFoundException to a 422 HTTP response.
     * @param ex the exception thrown
     * @return HTTP 422 response with JSON error body
     */
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Linked Resource Not Found");
        error.put("message", ex.getMessage());
        error.put("status", "422");
        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}