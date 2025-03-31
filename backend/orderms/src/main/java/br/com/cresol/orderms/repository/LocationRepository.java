package br.com.cresol.orderms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.cresol.orderms.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer>{
	Optional<Location> findByName(String name);
	
	Page<Location> findAllLocation(@Param("institution") Integer institution, Pageable pageable);
}
