package edu.home.registrationservice.rest;

import edu.home.registrationservice.dto.ErrorDTO;
import edu.home.registrationservice.dto.EventDTO;
import edu.home.registrationservice.dto.event.AddEventDTO;
import edu.home.registrationservice.exception.EntityDoesntExistException;
import edu.home.registrationservice.exception.EntityAlreadyExistsException;
import edu.home.registrationservice.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRest {

    private final EventService eventService;

    public EventRest(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() {
        return ResponseEntity
                .ok(eventService.getEvents());
    }

    @GetMapping("/{event_name}")
    public ResponseEntity<?> getEvent(
            @PathVariable("event_name") String eventName,
            @RequestParam("service_name") String domainAppName
    ) {
        try {
            return ResponseEntity
                    .ok(eventService.getEvent(eventName, domainAppName));
        }
        catch (EntityDoesntExistException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Event with this name doesn't exist"));
        }
    }


    @PostMapping
    public ResponseEntity<?> addEvent(HttpServletRequest httpRequest, AddEventDTO addEventDTO) {
        try {
            EventDTO eventDTO = eventService.addEvent(addEventDTO);

            return ResponseEntity
                    .created(URI.create(httpRequest.getRequestURL() + "/" + eventDTO.getName()))
                    .body(eventDTO);
        }
        catch (EntityAlreadyExistsException e) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorDTO(
                                "Event with the same name and parent service already exists"
                        )
                    );
        }
    }
}
