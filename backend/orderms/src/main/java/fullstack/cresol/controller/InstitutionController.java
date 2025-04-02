package fullstack.cresol.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fullstack.cresol.dto.InstitutionDTO;
import fullstack.cresol.model.Institution;
import fullstack.cresol.service.InstitutionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/institution")
public class InstitutionController {

	private final InstitutionService service;

	public InstitutionController(InstitutionService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> addNewInstitution(@Valid @RequestBody Institution newInstitution){
		Institution institution = service.addNewInstitution(newInstitution);
		return ResponseEntity.ok(new InstitutionDTO(institution));
	}

	@GetMapping
	public ResponseEntity<Page<Institution>> getAllInstitution(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "id") String sort){

		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return ResponseEntity.ok(service.getAllInstitution(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getInstitution(@PathVariable Integer id){
		Institution institution = service.getInstitution(id);

		return ResponseEntity.ok(new InstitutionDTO(institution));
	}

	@PutMapping
	public ResponseEntity<?> updateInstitution(@Valid @RequestBody Institution putInstitution){

		Institution institution = service.updateInstitution(putInstitution);

		return ResponseEntity.ok(new InstitutionDTO(institution));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeInstitution(@PathVariable Integer id){

		if (service.removeInstitution(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
