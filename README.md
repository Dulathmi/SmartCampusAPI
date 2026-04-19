# Smart Campus Sensor & Room Management API
### Module: 5COSC022W - Client-Server Architectures
### Author: Dulathmi Hettige
### Date: April 2026

## API Overview
This is a RESTful API I built using JAX-RS (Jersey) for the University's Smart Campus initiative. It manages Rooms and Sensors across campus. You can create, retrieve, and delete rooms and sensors, and also log sensor readings. I used an embedded Grizzly HTTP server and stored all data in ConcurrentHashMaps since we are not allowed to use a database.

## Technology Stack
- Java 21
- JAX-RS (Jersey 2.41)
- Grizzly HTTP Server (Embedded)
- Jackson (JSON Processing)
- Maven

## Project Structure

```
src/main/java/com/smartcampus/
├── Main.java
├── SmartCampusApplication.java
├── DataStore.java
├── Room.java
├── Sensor.java
├── SensorReading.java
├── DiscoveryResource.java
├── RoomResource.java
├── SensorResource.java
├── SensorReadingResource.java
├── RoomNotEmptyException.java
├── RoomNotEmptyExceptionMapper.java
├── LinkedResourceNotFoundException.java
├── LinkedResourceNotFoundExceptionMapper.java
├── SensorUnavailableException.java
├── SensorUnavailableExceptionMapper.java
├── GlobalExceptionMapper.java
└── LoggingFilter.java
```

## How to Build and Run

### Prerequisites
- JDK 21 or higher
- Maven 3.x
- Apache NetBeans IDE 28

### Steps

1. Clone the repository:
```bash
git clone https://github.com/Dulathmi/SmartCampusAPI.git
```

2. Open the project in NetBeans

3. Build the project:
```bash
mvn clean install
```

4. Run Main.java directly from NetBeans by right-clicking it and selecting Run File

5. The API will start at:http://localhost:8080/api/v1/


## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/ | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a new room |
| GET | /api/v1/rooms/{roomId} | Get a specific room |
| DELETE | /api/v1/rooms/{roomId} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| GET | /api/v1/sensors?type={type} | Filter sensors by type |
| POST | /api/v1/sensors | Create a new sensor |
| GET | /api/v1/sensors/{sensorId} | Get a specific sensor |
| GET | /api/v1/sensors/{sensorId}/readings | Get all readings |
| POST | /api/v1/sensors/{sensorId}/readings | Add a new reading |

## Sample curl Commands

These are some example curl commands you can run in the terminal to test the API:

**1. Get API Discovery**
```bash
curl -X GET http://localhost:8080/api/v1/
```

**2. Get All Rooms**
```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

**3. Create a New Room**
```bash
curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"HALL-101\",\"name\":\"Main Hall\",\"capacity\":100}"
```

**4. Create a New Sensor**
```bash
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-001\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"LAB-101\"}"
```

**5. Add a Sensor Reading**
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{\"value\":25.5}"
```

**6. Filter Sensors by Type**
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```

**7. Delete a Room**
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/HALL-101
```


## Report - Question Answers

---

Q: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

By default JAX-RS creates a brand new instance of a resource class for every single HTTP request that comes in. So it's per-request, not a singleton. 
This means each request gets its own fresh object which is good because threads don't interfere with each other through shared instance variables.
The problem is that any data stored inside the resource class itself would be lost the moment the request finishes. That's exactly why I used a separate 
DataStore class with static ConcurrentHashMaps. Since they're static they live for the entire lifetime of the application and are shared across all requests. 
ConcurrentHashMap handles thread safety automatically so two requests trying to write at the same time won't corrupt each other's data or cause race conditions.

------

Q: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS means your API responses include links that tell the client where they can go next, rather than the client having to guess or memorise URLs from a document.
In this project the discovery endpoint at GET /api/v1/ returns links to /api/v1/rooms and /api/v1/sensors so a client just needs to know the entry point and can navigate from there.
Compared to static documentation this is much better because if URLs ever change the clients get the updated links directly from the API responses and nothing breaks. With static docs 
you'd have to update the documentation and hope every developer notices the change.

-------

Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

Returning only IDs keeps the response small and uses less bandwidth which is great when you have hundreds of rooms. 
But then the client has to make a separate GET request for each room ID to get the actual details — this is called 
the N+1 problem and it means a lot more network calls.Returning full room objects uses more bandwidth in one go but 
the client gets everything they need in a single request without any extra processing on their side. For a campus 
management system where admins need to see names, capacities and sensor lists all at once, I think returning full
objects makes more sense.

-------

Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Yes it is idempotent. The first DELETE request on a room like DELETE /api/v1/rooms/HALL-101 removes it and returns 204 No Content.
If the client accidentally sends the same request again the room is already gone so they get back 404 Not Found.The response code 
is different on the second call but the important thing is the server state is exactly the same — the room doesn't exist either way.
Idempotency is about the end state on the server being consistent regardless of how many times the request is repeated, and that's 
exactly what happens here.

--------

Q: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

When @Consumes(MediaType.APPLICATION_JSON) is on the method, JAX-RS checks the Content-Type header of every incoming request before doing anything else. 
If a client sends text/plain or application/xml instead of application/json, JAX-RS immediately returns HTTP 415 Unsupported Media Type and the resource
method never even gets called.This is handled entirely by the framework so I didn't need to write any content type checking code myself. It keeps the resource 
methods clean and ensures the API only ever receives data in the format it expects.

---------

Q: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

With @QueryParam the filter is optional — /api/v1/sensors works perfectly on its own and returns all sensors, and /api/v1/sensors?type=CO2 filters them.
The base URL stays clean and meaningful either way.If I used a path like /api/v1/sensors/type/CO2 it starts to look like CO2 is a specific resource rather 
than a filter criteria, which is confusing and misleading. It also makes the URL unnecessarily long and harder to extend with multiple filters. 
Query parameters are the REST standard for filtering, searching and sorting collections because they're optional by nature and don't change the identity of 
the resource being accessed.

---------

Q: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

If I put everything in one class, SensorResource would end up handling sensors, readings, individual readings and who knows what else as the API grows.
It would become really hard to read and maintain.With the sub-resource locator pattern, SensorResource only handles sensor-level operations and when it 
sees a /readings path it just creates and returns a SensorReadingResource instance to handle the rest. Each class has one clear responsibility. 
This makes the code much easier to navigate, test independently and extend later. Adding new sub-resources just means creating a new class rather than
adding more methods to an already bloated controller.

--------

Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

A 404 Not Found tells the client that the URL they requested doesn't exist on the server. But when someone POSTs a sensor with a roomId that doesn't exist, 
the URL /api/v1/sensors is completely valid and working fine the problem is inside the request body, not the URL.HTTP 422 Unprocessable Entity is more 
accurate because it says "I received your request, the URL is fine, the format is fine, but I can't process it because the data inside has a semantic problem"
in this case referencing a room that doesn't exist. This gives the client much more useful information about what they actually need to fix.

---------

Q: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

A stack trace basically hands an attacker a detailed map of your application. From it they can see your package and class names which reveals how the code is structured.
They can see which third party libraries you're using and their versions, which lets them look up known vulnerabilities for those exact versions. They might also see file
paths on the server, database query details or internal business logic that helps them craft more targeted attacks.This is why I implemented the GlobalExceptionMapper 
it catches every unhandled exception and returns a clean generic 500 message with no internal details. The real error still gets logged on the server where developers can see it, 
but the client gets nothing useful for an attack.

---------

Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

If I manually added logging to every resource method i will be writing the same code dozens of times across the whole project.
That's a lot of repetition and  to change the log format I'd have to update every single method individually. It's also easy 
to forget to add it to new methods as the API grows.With a JAX-RS filter I wrote the logging logic once in LoggingFilter.java
and it automatically runs for every single request and response without me touching the resource classes at all. It's cleaner,
easier to maintain and guarantees that nothing ever gets missed.




