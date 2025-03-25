package br.com.cresol.orderms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cresol.orderms.dto.EventDTO;
import br.com.cresol.orderms.model.Event;
import br.com.cresol.orderms.service.EventService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

	private final EventService service;

	public EventController(EventService service) {
		this.service = service;
	}

	@PostMapping("/{institution}")
	public ResponseEntity<?> addNewEvent(@Valid @RequestBody EventDTO event,
			@PathVariable Integer institution) {
		Event eventRecorded = service.addNewEvent(institution, event);

		return ResponseEntity.ok(new EventDTO(eventRecorded));
	}

	@GetMapping("/{institution}")
	public ResponseEntity<Page<EventDTO>> getEvents(
		@PageableDefault(size = 10, sort = "startDate") Pageable pageable,
		@PathVariable Integer institution) {
		return ResponseEntity.ok(service.getEvent(pageable, institution));
	}

	@GetMapping("/{institution}/{id}")
	public ResponseEntity<?> getEventId(@PathVariable Integer institution, @PathVariable Integer id){
		Event event = service.getEventId(institution, id);

		return ResponseEntity.ok(new EventDTO(event));
	}

	@PutMapping("/{institution}")
	public ResponseEntity<?> updateEvent(@Valid @RequestBody EventDTO event,
			@PathVariable Integer institution){
		Event eventChanged = service.updateEvent(institution, event);

		return ResponseEntity.ok(new EventDTO(eventChanged));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeEvent(@PathVariable Integer id){
		if (service.removeEvent(id)) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}
