package br.com.cresol.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cresol.orderms.exception.InstitutionConflictException;
import br.com.cresol.orderms.exception.InstitutionNotFoundException;
import br.com.cresol.orderms.exception.InstitutionUsedException;
import br.com.cresol.orderms.exception.TypeInstitutionNotFoundException;
import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.model.TypeInstitution;
import br.com.cresol.orderms.repository.InstitutionRepository;
import br.com.cresol.orderms.repository.LocationRepository;
import br.com.cresol.orderms.repository.TypeInstitutionRepository;

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
