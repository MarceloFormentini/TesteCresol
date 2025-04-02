package fullstack.cresol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fullstack.cresol.dto.TypeInstitutionDTO;
import fullstack.cresol.model.TypeInstitution;
import fullstack.cresol.service.TypeInstitutionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/typeinstitution")
public class TypeInstitutionController {

	private final TypeInstitutionService service;
	
	public TypeInstitutionController(TypeInstitutionService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<?> addNewType(@Valid @RequestBody TypeInstitution newTypeInstitution){
		TypeInstitution typeInstitution = service.addNewType(newTypeInstitution);
		return ResponseEntity.ok(new TypeInstitutionDTO(typeInstitution));
	}
	
	@GetMapping
	public ResponseEntity<?> getAllTypeInstitution(){
		return ResponseEntity.ok(service.getAllTypeInstitution());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTypeInstitution(@PathVariable Integer id){
		TypeInstitution typeInstitution = service.getTypeInstitution(id);
		return ResponseEntity.ok(new TypeInstitutionDTO(typeInstitution));
	}
	
	@PutMapping
	public ResponseEntity<?> updateTypeInstitution(@Valid @RequestBody TypeInstitution putTypeInstitution){

		TypeInstitution typeInstitution = service.updateTypeInstitution(putTypeInstitution);

		return ResponseEntity.ok(new TypeInstitutionDTO(typeInstitution));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeTypeInstitution(@PathVariable Integer id){

		if (service.removeTypeInstitution(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
