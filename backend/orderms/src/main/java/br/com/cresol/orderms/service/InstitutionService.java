package br.com.cresol.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.cresol.orderms.exception.InstitutionConflictException;
import br.com.cresol.orderms.exception.InstitutionNotFoundException;
import br.com.cresol.orderms.exception.InstitutionUsedException;
import br.com.cresol.orderms.model.Institution;
import br.com.cresol.orderms.repository.EventRepository;
import br.com.cresol.orderms.repository.InstitutionRepository;
import jakarta.validation.Valid;

@Service
public class InstitutionService {
	
	private final InstitutionRepository repository;
	private final EventRepository eventRepository;
	
	public InstitutionService(InstitutionRepository repository, EventRepository eventRepository) {
		this.repository = repository;
		this.eventRepository = eventRepository;
	}

	public Institution addNewInstitution(@Valid Institution newInstitution) {
		repository.findByNameAndType(
			newInstitution.getName(),
			newInstitution.getType()
		).ifPresent(institution -> {
			throw new RuntimeException("Instituição " + institution.getNameAndType() + " já cadastrada");
		});
	
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

		institution.setName(newInstitution.getName());
		institution.setType(newInstitution.getType());

		repository.findByNameAndType(
			newInstitution.getName(),
			newInstitution.getType()
		).ifPresent(upInstitution -> {
			throw new InstitutionConflictException(
				"Instituição " + newInstitution.getName() + " - " + newInstitution.getType() + " já cadastrada."
			);
		});

		repository.save(institution);

		return institution;
	}

	public boolean removeInstitution(Integer id) {

		if (!repository.existsById(id)) {
			throw new InstitutionNotFoundException("Não existe instituição cadastrada com o código " + id);
		}

		boolean ownEvents = eventRepository.existsEventByInstitution(id);
		if (ownEvents) {
			throw new InstitutionUsedException("A instituição não pode ser excluída pois possui eventos vinculados.");
		}

		repository.deleteById(id);
		return true;
	}
}
