package br.com.cresol.orderms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

	@PostMapping("/{institution}/{location}")
	public ResponseEntity<?> addNewEvent(@Valid @RequestBody EventDTO event,
			@PathVariable Integer institution,
			@PathVariable Integer location) {
		Event eventRecorded = service.addNewEvent(event, institution, location);

		return ResponseEntity.ok(new EventDTO(eventRecorded));
	}

	@GetMapping("/{institution}/{location}")
	public ResponseEntity<Page<EventDTO>> getEvents(
		@PageableDefault(size = 10, sort = "startDate") Pageable pageable,
		@PathVariable Integer institution,
		@PathVariable Integer location) {
		return ResponseEntity.ok(service.getEvent(pageable, institution, location));
	}

	@GetMapping("/{institution}/{location}/{id}")
	public ResponseEntity<?> getEventId(
			@PathVariable Integer institution, 
			@PathVariable Integer location,
			@PathVariable Integer id){
		Event event = service.getEventId(institution, location, id);

		return ResponseEntity.ok(new EventDTO(event));
	}

	@PutMapping("/{institution}/{location}")
	public ResponseEntity<?> updateEvent(@Valid @RequestBody EventDTO event,
			@PathVariable Integer institution,
			@PathVariable Integer location){
		Event eventChanged = service.updateEvent(event, institution, location);

		return ResponseEntity.ok(new EventDTO(eventChanged));
	}

	@PatchMapping("/{institution}/{location}/{id}/cancel")
	public ResponseEntity<?> cancelEvent(
			@PathVariable Integer institution,
			@PathVariable Integer location,
			@PathVariable Integer id){

		if (service.cancelEvent(institution, location, id)) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}
