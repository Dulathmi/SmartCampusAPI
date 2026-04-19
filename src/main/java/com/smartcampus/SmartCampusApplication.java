/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */




package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * JAX-RS Application configuration class for the Smart Campus API.
 * Sets the base URI path for all API endpoints to /api/v1.
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    // Jersey auto-scans for resource and provider classes
}