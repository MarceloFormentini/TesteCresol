package fullstack.cresol.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fullstack.cresol.exception.InstitutionConflictException;
import fullstack.cresol.exception.InstitutionNotFoundException;
import fullstack.cresol.exception.InstitutionUsedException;
import fullstack.cresol.exception.TypeInstitutionNotFoundException;
import fullstack.cresol.model.Institution;
import fullstack.cresol.model.TypeInstitution;
import fullstack.cresol.repository.InstitutionRepository;
import fullstack.cresol.repository.LocationRepository;
import fullstack.cresol.repository.TypeInstitutionRepository;

@Service
public class InstitutionService {
	
	private final InstitutionRepository repository;
	private final LocationRepository locationRepository;
	private final TypeInstitutionRepository typeRepository;
	
	public InstitutionService(InstitutionRepository repository, LocationRepository locationRepository, TypeInstitutionRepository typeRepository) {
		this.repository = repository;
		this.locationRepository = locationRepository;
		this.typeRepository = typeRepository; 
	}

	public Institution addNewInstitution(Institution newInstitution) {
		if (repository.existsByNameAndTypeInstitution(
			newInstitution.getName(),
			newInstitution.getTypeInstitution())){
			throw new InstitutionConflictException("Instituição " + newInstitution.getNameAndType() + " já cadastrada");
		}
	
		return repository.save(newInstitution);
	}

	public Page<Institution> getAllInstitution(Pageable pageable){
		return repository.findAll(pageable);
	}

	public Institution getInstitution(Integer id) {
		Institution institution = repository.findById(id)
			.orElseThrow(() -> new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + id));

		return institution;
	}

	public Institution updateInstitution(Institution newInstitution) {
		
		Institution institution = repository.findById(newInstitution.getId())
				.orElseThrow(() -> new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + newInstitution.getId()));

		TypeInstitution typeInstitution = typeRepository.findById(newInstitution.getTypeInstitution().getId())
				.orElseThrow(() -> new TypeInstitutionNotFoundException("Não existe tipo cadastrado com o código " + newInstitution.getTypeInstitution().getId()));

		institution.setName(newInstitution.getName());
		institution.setTypeInstitution(typeInstitution);

		System.out.println(institution.toString());

		if (repository.existsByNameAndTypeInstitution(
			newInstitution.getName(),
			newInstitution.getTypeInstitution())){
			throw new InstitutionConflictException(
				"Instituição " + newInstitution.getNameAndType() + " já cadastrada."
			);
		}

		repository.save(institution);

		return institution;
	}

	public boolean removeInstitution(Integer id) {

		if (!repository.existsById(id)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + id);
		}

		boolean ownEvents = locationRepository.findByInstitution(id);
		if (ownEvents) {
			throw new InstitutionUsedException("A instituição não pode ser excluída pois possui local vinculado.");
		}

		repository.deleteById(id);
		return true;
	}
}
