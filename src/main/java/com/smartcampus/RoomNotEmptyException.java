/**
 * Smart Campus Sensor & Room Management API
 * Module: 5COSC022W - Client-Server Architectures
 * Author: Dulathmi Hettige
 * Date: April 2026
 */

package com.smartcampus;

/**
 * Custom exception thrown when attempting to delete a room
 * that still has sensors assigned to it.
 * Mapped to HTTP 409 Conflict by RoomNotEmptyExceptionMapper.
 */
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}