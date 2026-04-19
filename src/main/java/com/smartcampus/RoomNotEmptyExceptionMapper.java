package com.smartcampus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */




/**
 * Exception mapper for RoomNotEmptyException.
 * Returns HTTP 409 Conflict when a room deletion is attempted
 * while sensors are still assigned to the room.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    /**
     * Maps RoomNotEmptyException to a 409 Conflict HTTP response.
     * @param ex the exception thrown
     * @return HTTP 409 response with JSON error body
     */
    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Room Not Empty");
        error.put("message", ex.getMessage());
        error.put("status", "409");
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}