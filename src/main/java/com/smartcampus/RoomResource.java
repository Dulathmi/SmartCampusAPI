/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * JAX-RS Resource class for managing Rooms in the Smart Campus API.
 * Handles all CRUD operations for the /api/v1/rooms endpoint.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    /**
     * GET /api/v1/rooms
     * Retrieves a list of all rooms in the system.
     * @return 200 OK with list of all rooms
     */
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    /**
     * POST /api/v1/rooms
     * Creates a new room in the system.
     * @param room the room object from request body
     * @return 201 Created with the new room, or 400/409 on error
     */
    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room ID is required\"}")
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Room with this ID already exists\"}")
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    /**
     * GET /api/v1/rooms/{roomId}
     * Retrieves a specific room by its ID.
     * @param roomId the ID of the room to retrieve
     * @return 200 OK with room data, or 404 if not found
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found: " + roomId + "\"}")
                    .build();
        }
        return Response.ok(room).build();
    }

    /**
     * DELETE /api/v1/rooms/{roomId}
     * Deletes a room from the system.
     * Business rule: Cannot delete a room that still has sensors assigned.
     * @param roomId the ID of the room to delete
     * @return 204 No Content on success, 404 if not found, 409 if room has sensors
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found: " + roomId + "\"}")
                    .build();
        }
        // Block deletion if room still has sensors assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room '" + roomId + "' cannot be deleted as it still has active sensors assigned."
            );
        }
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}