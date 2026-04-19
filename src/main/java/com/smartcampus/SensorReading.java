/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */


package com.smartcampus;

import java.util.UUID;

/**
 * Represents a single sensor reading event.
 * Each reading captures a value at a specific point in time.
 */
public class SensorReading {
    private String id;        // Unique reading event ID (UUID)
    private long timestamp;   // Epoch time (ms) when reading was captured
    private double value;     // The actual metric value recorded

    public SensorReading() {}

    public SensorReading(double value) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}