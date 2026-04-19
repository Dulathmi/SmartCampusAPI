/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Discovery endpoint for the Smart Campus API.
 * Provides API metadata and hypermedia links (HATEOAS) at GET /api/v1/
 */
@Path("/")
public class DiscoveryResource {

    /**
     * GET /api/v1/
     * Returns API metadata including version, contact info,
     * and links to primary resource collections (HATEOAS).
     * @return 200 OK with API discovery information
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> response = new HashMap<>();
        response.put("version", "1.0");
        response.put("name", "Smart Campus Sensor & Room Management API");
        response.put("contact", "admin@smartcampus.ac.uk");

        // HATEOAS links to primary resource collections
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        response.put("resources", links);

        return Response.ok(response).build();
    }
}