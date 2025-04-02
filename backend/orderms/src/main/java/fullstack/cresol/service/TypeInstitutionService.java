package fullstack.cresol.service;

import org.springframework.stereotype.Service;

import fullstack.cresol.exception.TypeInstituionConflictException;
import fullstack.cresol.exception.TypeInstitutionNotFoundException;
import fullstack.cresol.exception.TypeInstitutionUsedException;
import fullstack.cresol.model.TypeInstitution;
import fullstack.cresol.repository.InstitutionRepository;
import fullstack.cresol.repository.TypeInstitutionRepository;

@Service
public class TypeInstitutionService {
	
	private final TypeInstitutionRepository repository;
	private final InstitutionRepository institutionRepository;
	
	public TypeInstitutionService(TypeInstitutionRepository repository, InstitutionRepository institutionRepository) {
		this.repository = repository;
		this.institutionRepository = institutionRepository;
	}

	public TypeInstitution addNewType(TypeInstitution newTypeInstitution) {
		repository.findByName(
			newTypeInstitution.getName()
		).ifPresent(typeInstitution ->{
			throw new TypeInstituionConflictException("Tipo " + typeInstitution.getName() + " já cadastrado.");
		});
		
		return repository.save(newTypeInstitution);
	}

	public Object getAllTypeInstitution() {
		return repository.findAll();
	}
	
	public TypeInstitution getTypeInstitution(Integer id) {
		TypeInstitution typeInstitution = repository.findById(id)
				.orElseThrow(() -> new TypeInstitutionNotFoundException(
						String.format("Não existe tipo cadastrado com o código %d.", id)));

		return typeInstitution;
	}

	public TypeInstitution updateTypeInstitution(TypeInstitution putTypeInstitution) {
		TypeInstitution typeInstitution = repository.findById(putTypeInstitution.getId())
			.orElseThrow(() -> new TypeInstitutionNotFoundException(
					"Não existe tipo cadastrado com o código " + putTypeInstitution.getId()));

		if (repository.existsByName(putTypeInstitution.getName())){
			throw new TypeInstituionConflictException("Tipo " + putTypeInstitution.getName() + " já cadastrado.");
		}

		typeInstitution.setName(putTypeInstitution.getName());

		repository.save(typeInstitution);

		return typeInstitution;
	}

	public boolean removeTypeInstitution(Integer id) {
		if (!repository.existsById(id)) {
			throw new TypeInstitutionNotFoundException(
				"Não existe tipo cadastrado com o código " + id
			);
		}
		
		boolean ownTypeInstitution = institutionRepository.existsTypeByInstitution(id);
		if (ownTypeInstitution) {
			throw new TypeInstitutionUsedException("O tipo não pode ser excluído pois possui instituição vinculada.");
		}
		
		repository.deleteById(id);
		return true;
	}

}
