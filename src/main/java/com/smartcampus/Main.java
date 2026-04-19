/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

/**
 * Main entry point for the Smart Campus API.
 * Starts an embedded Grizzly HTTP server with Jersey JAX-RS.
 */
public class Main {
    
    // Base URI where the API will be available
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    /**
     * Main method to start the embedded server.
     * @param args command line arguments
     * @throws Exception if server fails to start
     */
    public static void main(String[] args) throws Exception {
        // Configure Jersey to scan com.smartcampus package for resources
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus")
                .register(org.glassfish.jersey.jackson.JacksonFeature.class);

        // Create and start the Grizzly HTTP server
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI), config);

        System.out.println("Smart Campus API running at: " + BASE_URI);
        System.out.println("Press ENTER to stop...");
        System.in.read();
        server.stop();
    }
}