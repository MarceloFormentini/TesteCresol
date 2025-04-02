package br.com.cresol.orderms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cresol.orderms.model.TypeInstitution;

public interface TypeInstitutionRepository extends JpaRepository<TypeInstitution, Integer>{
	Optional<TypeInstitution> findByName(String name);
	public boolean existsByName(String name);
}
