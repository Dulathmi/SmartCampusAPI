/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory data store for the Smart Campus API.
 * Uses ConcurrentHashMap to ensure thread safety across concurrent requests.
 * Acts as a shared singleton data layer for all resource classes.
 */
public class DataStore {

    // Stores all rooms keyed by room ID
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    // Stores all sensors keyed by sensor ID
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Stores sensor readings keyed by sensor ID
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    // Static initializer to pre-load sample data
    static {
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        r1.getSensorIds().add("TEMP-001");
        r2.getSensorIds().add("CO2-001");

        readings.put("TEMP-001", new ArrayList<>());
        readings.put("CO2-001", new ArrayList<>());
    }
}