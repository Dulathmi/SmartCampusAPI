package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * JAX-RS Resource class for managing Sensors in the Smart Campus API.
 * Handles CRUD operations and filtering for the /api/v1/sensors endpoint.
 * Also acts as a sub-resource locator for sensor readings.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    /**
     * GET /api/v1/sensors
     * GET /api/v1/sensors?type=CO2
     * Retrieves all sensors, with optional filtering by type.
     * @param type optional query parameter to filter sensors by type
     * @return 200 OK with list of sensors
     */
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> result = new ArrayList<>(DataStore.sensors.values());
        if (type != null && !type.isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor s : result) {
                if (s.getType().equalsIgnoreCase(type)) {
                    filtered.add(s);
                }
            }
            return Response.ok(filtered).build();
        }
        return Response.ok(result).build();
    }

    /**
     * POST /api/v1/sensors
     * Registers a new sensor in the system.
     * Validates that the referenced roomId exists before creating.
     * @param sensor the sensor object from request body
     * @return 201 Created with new sensor, or 400/409/422 on error
     */
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor ID is required\"}")
                    .build();
        }
        // Validate that the referenced room exists
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room with ID '" + sensor.getRoomId() + "' does not exist."
            );
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Sensor with this ID already exists\"}")
                    .build();
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        // Add sensor ID to the room's sensor list
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        // Initialize empty readings list for this sensor
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    /**
     * GET /api/v1/sensors/{sensorId}
     * Retrieves a specific sensor by its ID.
     * @param sensorId the ID of the sensor to retrieve
     * @return 200 OK with sensor data, or 404 if not found
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found: " + sensorId + "\"}")
                    .build();
        }
        return Response.ok(sensor).build();
    }

    
    
    
    
    
    
    
    
    /**
     * Sub-resource locator for sensor readings.
     * Delegates handling of /api/v1/sensors/{sensorId}/readings
     * to the SensorReadingResource class.
     * @param sensorId the ID of the sensor
     * @return SensorReadingResource instance for the given sensor
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        return new SensorReadingResource(sensorId);
    }
}