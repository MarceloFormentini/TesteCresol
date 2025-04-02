package fullstack.cresol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fullstack.cresol.model.TypeInstitution;

public interface TypeInstitutionRepository extends JpaRepository<TypeInstitution, Integer>{
	Optional<TypeInstitution> findByName(String name);
	public boolean existsByName(String name);
}
