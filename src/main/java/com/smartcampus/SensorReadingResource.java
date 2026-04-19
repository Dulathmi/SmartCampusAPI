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
import java.util.List;

/**
 * Sub-resource class for managing Sensor Readings.
 * Handles reading history for a specific sensor at:
 * /api/v1/sensors/{sensorId}/readings
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // The sensor ID this resource is scoped to
    private final String sensorId;

    /**
     * Constructor called by the sub-resource locator in SensorResource.
     * @param sensorId the ID of the parent sensor
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * GET /api/v1/sensors/{sensorId}/readings
     * Retrieves all historical readings for a specific sensor.
     * @return 200 OK with list of readings, or 404 if sensor not found
     */
    @GET
    public Response getReadings() {
        List<SensorReading> list = DataStore.readings.get(sensorId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"No readings found for sensor: " + sensorId + "\"}")
                    .build();
        }
        return Response.ok(list).build();
    }

    /**
     * POST /api/v1/sensors/{sensorId}/readings
     * Appends a new reading to the sensor's history.
     * Also updates the sensor's currentValue field.
     * Throws SensorUnavailableException if sensor is under MAINTENANCE.
     * @param reading the reading object from request body
     * @return 201 Created with new reading, or 403/404 on error
     */
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found: " + sensorId + "\"}")
                    .build();
        }
        // Block readings if sensor is under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is under MAINTENANCE and cannot accept readings."
            );
        }
        SensorReading newReading = new SensorReading(reading.getValue());
        DataStore.readings.get(sensorId).add(newReading);
        // Update parent sensor's currentValue for data consistency
        sensor.setCurrentValue(reading.getValue());
        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}