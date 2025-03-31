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

import br.com.cresol.orderms.dto.LocationDTO;
import br.com.cresol.orderms.model.Location;
import br.com.cresol.orderms.service.LocationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/location")
public class LocationController {

	private final LocationService service;
	
	public LocationController(LocationService service) {
		this.service = service;
	}
	
	@PostMapping("/{institution}")
	public ResponseEntity<?> addNewLocation(@Valid @RequestBody Location location,
			@PathVariable Integer institution){
		Location locationRecorded = service.addNewLocation(location, institution);
		
		return ResponseEntity.ok(new LocationDTO(locationRecorded));
	}
	
	@GetMapping("/{institution}")
	public ResponseEntity<Page<LocationDTO>> getAllLocation(
			@PageableDefault(size = 10, sort = "id") Pageable pageable,
			@PathVariable Integer institution){
		return ResponseEntity.ok(service.getAllLocation(pageable, institution));
	}
	
	@GetMapping("/{institution}/{id}")
	public ResponseEntity<?> getLocationById(@PathVariable Integer institution, @PathVariable Integer id){
		Location location = service.getLocationById(institution, id);
		
		return ResponseEntity.ok(new LocationDTO(location));
	}
	
	@PutMapping("/{institution}")
	public ResponseEntity<?> updateLocation(@Valid @RequestBody Location location,
			@PathVariable Integer institution){
		Location locationChanged = service.updateLocation(location, institution);
		
		return ResponseEntity.ok(new LocationDTO(locationChanged));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeLocation(@PathVariable Integer id){
		if (service.removeLocation(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
