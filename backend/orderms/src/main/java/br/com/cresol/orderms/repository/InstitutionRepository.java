package br.com.cresol.orderms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import br.com.cresol.orderms.model.Institution;

public interface InstitutionRepository extends CrudRepository<Institution, Integer>{
	Optional<Institution> findByNameAndType(String name, String type);
	Page<Institution> findAll(Pageable pageable);
}
